package com.easysoft.ecommerce.security;

import com.easysoft.ecommerce.dao.RoleDao;
import com.easysoft.ecommerce.dao.SiteDao;
import com.easysoft.ecommerce.dao.UserDao;
import com.easysoft.ecommerce.dao.UserRoleDao;
import com.easysoft.ecommerce.model.Role;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.User;
import com.easysoft.ecommerce.model.UserRole;
import com.easysoft.ecommerce.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.authentication.encoding.PlaintextPasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.*;

@Transactional
public class AuthenticationProviderImpl extends AbstractUserDetailsAuthenticationProvider {

    // ~ Instance fields
    // ================================================================================================

    private PasswordEncoder passwordEncoder = new PlaintextPasswordEncoder();
    private UserService userService;

    private SaltSource saltSource;

    private SiteDao siteDao;
    private UserDao userDao;
    private UserRoleDao userRoleDao;
    private RoleDao roleDao;

    private boolean includeDetailsObject = true;

    // ~ Methods
    // ========================================================================================================

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        Object salt = null;

        if (this.saltSource != null) {
            salt = this.saltSource.getSalt(userDetails);
        }

        if (authentication.getCredentials() == null) {
            throw new BadCredentialsException(messages.getMessage("usernameinvalid.badCredentials", "Username không hợp lệ"), includeDetailsObject ? userDetails : null);
        }

        String presentedPassword = authentication.getCredentials().toString();

        if (!userService.isValidPassword(presentedPassword, userDetails.getPassword())) {
            if (!passwordEncoder.isPasswordValid(userDetails.getPassword(), presentedPassword, salt)) {
                throw new BadCredentialsException(messages.getMessage("usernamepassworddoesnotmatch.badCredentials", "Username hoặc password không hợp lệ"), includeDetailsObject ? userDetails : null);
            }
        }
    }

    @Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails loadedUser = null;
        WebAuthenticationDetailsImpl details = (WebAuthenticationDetailsImpl) authentication.getDetails();

        List<User> users;
        Map map = details.getParameterMap();
        String domains[] = (String[]) map.get("domain");
        if (domains == null || domains.length == 0) {
            throw new AuthenticationServiceException(messages.getMessage("domain.invalid", "Domain không hợp lệ. Vui lòng kiểm tra lại"));
        }
        Site site = siteDao.getSiteByServerName(domains[0]);
        if (site == null) {
            site = siteDao.getSiteByServerNameIncludeExpiredDomain(domains[0]);
            if (site == null) {
                throw new AuthenticationServiceException(messages.getMessage("domain.invalid", "Tên miền không tồn tại. Vui lòng kiểm tra lại"));
            } else {
                throw new AuthenticationServiceException(messages.getMessage("domain.invalid", "Hosting hết hạn sử dụng. Vui lòng nạp tiền để tiếp tục sử dụng. Bạn có thể dùng tên miền miễn phí để truy cập website của bạn http://"+site.getSubDomain()+"/admin"));
            }
        }
        try {
            users = userDao.findClientUserByUsername(username, site);
        } catch (DataAccessException repositoryProblem) {
            throw new AuthenticationServiceException(repositoryProblem.getMessage(), repositoryProblem);
        }

        for (User user : users) {
            loadedUser = getUserDetails(user, site);
            break;
        }

        if (loadedUser == null) {
            throw new UsernameNotFoundException(messages.getMessage("usernamepassworddoesnotmatch.badCredentials", "Tài khoản đã bị khóa"));
        }
        return loadedUser;
    }

    private UserDetails getUserDetails(User user, Site site) {
        String username = user.getUsername();
        String password = user.getPassword();
        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = "N".equals(user.getBlocked());
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

        List<UserRole> userRoles = userRoleDao.findUserRoles(user.getId(), site);

        List<Role> roles = new ArrayList<Role>();
        //Get all the roles of the user.
        for (UserRole userRole : userRoles) {
            getRecursiveRoles(userRole.getRole(), roles);
        }

        //Add all the roles to authority
        for (Role role : roles) {
            authorities.add(new GrantedAuthorityImpl(role.getRole()));
        }

        org.springframework.security.core.userdetails.User userDetails = new UserDetailsImpl(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities, site);

        return userDetails;
    }

    @Override
    protected Authentication createSuccessAuthentication(Object principal, Authentication authentication, UserDetails user) {
        Authentication loggedInAUthentication = super.createSuccessAuthentication(principal, authentication, user);
        WebAuthenticationDetailsImpl details = (WebAuthenticationDetailsImpl) loggedInAUthentication.getDetails();
//        try {
//            details.setCsrf(Long.toString(SecureRandom.getInstance("SHA1PRNG").nextLong()));
        //Fix URL Encoding issue. Don't need encoding url
        details.setCsrf(Long.toString(System.currentTimeMillis()));
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
        return loggedInAUthentication;
    }

    /**
     * Sets the PasswordEncoder instance to be used to encode and validate
     * passwords. If not set, {@link PlaintextPasswordEncoder} will be used by
     * default.
     *
     * @param passwordEncoder The passwordEncoder to use
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    protected PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    /**
     * The source of salts to use when decoding passwords. <code>null</code> is
     * a valid value, meaning the <code>DaoAuthenticationProvider</code> will
     * present <code>null</code> to the relevant <code>PasswordEncoder</code>.
     *
     * @param saltSource to use when attempting to decode passwords via the
     *                   <code>PasswordEncoder</code>
     */
    public void setSaltSource(SaltSource saltSource) {
        this.saltSource = saltSource;
    }

    protected SaltSource getSaltSource() {
        return saltSource;
    }

    @Autowired
    public void setSiteDao(SiteDao siteDao) {
        this.siteDao = siteDao;
    }

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Autowired
    public void setUserRoleDao(UserRoleDao userRoleDao) {
        this.userRoleDao = userRoleDao;
    }

    @Autowired
    public void setRoleDao(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    protected boolean isIncludeDetailsObject() {
        return includeDetailsObject;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    /**
     * Determines whether the UserDetails will be included in the
     * <tt>extraInformation</tt> field of a thrown BadCredentialsException.
     * Defaults to true, but can be set to false if the exception will be used
     * with a remoting protocol, for example.
     *
     * @deprecated use
     *             {@link org.springframework.security.authentication.ProviderManager#setClearExtraInformation(boolean)}
     */
    public void setIncludeDetailsObject(boolean includeDetailsObject) {
        this.includeDetailsObject = includeDetailsObject;
    }

    /**
     * Get recursive roles
     *
     * @param role
     * @param result
     */
    private void getRecursiveRoles(String role, List<Role> result) {
        if (result == null) {
            result = new ArrayList<Role>();
        }
        // get all children roles
        List<Role> roles = roleDao.findChildrenRoles(role);
        for (Role r : roles) {
            if (!result.contains(r)) {
                result.add(r);
                if (!"NONE".equals(r.getChildRole())) {
                    getRecursiveRoles(r.getChildRole(), result);
                }
            } else {
                //ignore because the role and children already added.
            }
        }
    }
}
