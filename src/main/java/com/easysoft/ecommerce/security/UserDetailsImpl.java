package com.easysoft.ecommerce.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.easysoft.ecommerce.model.Site;

public class UserDetailsImpl extends User {

    private Site site;

    public UserDetailsImpl(String username, String password, boolean enabled, boolean accountNonExpired,
            boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
            Site site) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.site = site;
    }

    public Site getSite() {
        return site;
    }

}
