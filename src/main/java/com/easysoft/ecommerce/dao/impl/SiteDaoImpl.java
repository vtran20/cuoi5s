package com.easysoft.ecommerce.dao.impl;

import com.easysoft.ecommerce.dao.SiteDao;
import com.easysoft.ecommerce.model.Product;
import com.easysoft.ecommerce.model.Site;
import com.easysoft.ecommerce.model.Template;
import com.easysoft.ecommerce.util.WebUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.*;

/**
 * TODO: Cache Site Object into Cache Manager.
 */
@Repository
public class SiteDaoImpl extends GenericDaoImpl<Site, Long> implements SiteDao {

    /**
     * This method will get the site object base on server name (domain). It will base on domain column or sub domain column.
     * Also, it have to be activated and the current date between date range (start and end date)
     *
     * @param serverName domain of site.
     *
     * @return
     */
    @Override
    public Site getSiteByServerName(String serverName) {
        Site site  = (Site) this.getSessionFactory().getCurrentSession().
                createQuery("select o from Site o where o.active = 'Y' and (o.domain = :serverName or o.subDomain = :serverName)").
                setString("serverName", serverName).
                uniqueResult();
        if (site != null) {
            //Check if customers are using their own domain and end date was expired
            if (StringUtils.isNotEmpty(serverName) && serverName.equalsIgnoreCase(site.getDomain())) {
                if (site.getEndDate() != null && site.getEndDate().before(new Date())) {
                    return null;
                }
            }
        }

        return site;
    }
    @Override
    public Site getSiteByServerNameIncludeExpiredDomain(String serverName) {
        if (StringUtils.isNotEmpty(serverName)) {
            return (Site) this.getSessionFactory().getCurrentSession().
                    createQuery("select o from Site o where o.active = 'Y' and (o.domain = :serverName or o.subDomain = :serverName)").
                    setString("serverName", serverName).
                    uniqueResult();
        } else {
            return null;
        }
    }

    @Override
    public Site getSiteSample(Template selectedTemplate) {
        return getSiteSample(selectedTemplate, "Y");
    }
    @Override
    public Site getSiteSample(Template selectedTemplate, String active) {
        if ("Y".equals(active)) {
            return (Site) this.getSessionFactory().getCurrentSession().
                    createQuery("select o from Template t join t.siteSample o where t.active = 'Y' and t.id = :id").
                    setLong("id", selectedTemplate.getId()).
                    uniqueResult();
        } else {
            return (Site) this.getSessionFactory().getCurrentSession().
                    createQuery("select o from Template t join t.siteSample o where t.id = :id").
                    setLong("id", selectedTemplate.getId()).
                    uniqueResult();
        }
    }
    @Override
    public Template getTemplateFromSite(Site site) {
        List<Template> templates =  this.getSessionFactory().getCurrentSession().
                createQuery("select t from Template t join t.siteSample o where t.active = 'Y' and o.id = :id").
                setLong("id", site.getId()).list();
        if (templates != null && templates.size() > 0) {
            return templates.get(0);
        } else {
            return null;
        }
    }

    @Override
    public List <Map> searchClientSite(Map input, Site site) {
        String keyword = (String) input.get("keyword");
        List<Object[]> rs = null;
        if (StringUtils.isEmpty(keyword)) {
            rs = (List<Object[]>) getSessionFactory().getCurrentSession().createSQLQuery(
                    "select id, name, domain, subdomain, active, endDate from site where parentSite_id = :siteId and siteType = 2")
                    .setParameter("siteId", site.getId())
                    .list();
        } else {
            rs = (List<Object[]>) getSessionFactory().getCurrentSession().createSQLQuery(
                    "select id, name, domain, subdomain, active, endDate from site where parentSite_id = :siteId and siteType = 2 and (domain like :name or subdomain like :name)")
                    .setParameter("siteId", site.getId())
                    .setParameter("name", "%"+keyword+"%")
                    .list();
        }
        List<Map> results = new ArrayList<Map>();
        if (rs != null && rs.size() > 0) {
            for (Object[] obj : rs) {
                Long siteId = ((BigInteger)obj[0]).longValue();
                String name = (String) obj[1];
                String domain = (String) obj[2];
                String subDomain = (String) obj[3];
                String active = (String) obj[4];
                Date endDate = (Date) obj[5];
                Map clientSite = new HashMap();
                clientSite.put("id",siteId);
                clientSite.put("name",name);
                clientSite.put("domain",domain);
                clientSite.put("subDomain",subDomain);
                clientSite.put("active",active);
                clientSite.put("endDate", WebUtil.dateToString(endDate, site.getSiteParam("DATE_FORMAT")));
                results.add(clientSite);
            }
        }
        return results;
    }

    @Override
    public List<Map> searchTemplateSite(Map input, Site site) {
        String keyword = (String) input.get("keyword");
        List<Object[]> rs = null;
        if (StringUtils.isEmpty(keyword)) {
            rs = (List<Object[]>) getSessionFactory().getCurrentSession().createSQLQuery(
                    "select t.id, t.name, t.templateModel, t.active, t.siteSample_id, s.subDomain from template t, site s where t.siteSample_id = s.id and t.site_id = :siteId ")
                    .setParameter("siteId", site.getId())
                    .list();
        } else {
            rs = (List<Object[]>) getSessionFactory().getCurrentSession().createSQLQuery(
                    "select t.id, t.name, t.templateModel, t.active, t.siteSample_id, s.subDomain from template t, site s where t.siteSample_id = s.id and t.site_id = :siteId and s.subDomain like :name ")
                    .setParameter("siteId", site.getId())
                    .setParameter("name", "%"+keyword+"%")
                    .list();
        }
        List<Map> results = new ArrayList<Map>();
        if (rs != null && rs.size() > 0) {
            for (Object[] obj : rs) {
                Long id = ((BigInteger)obj[0]).longValue();
                String name = (String) obj[1];
                String templateModel = (String) obj[2];
                String active = (String) obj[3];
                Long sampleSiteId = ((BigInteger)obj[4]).longValue();
                String subDomain = (String) obj[5];
                Map siteTemplate = new HashMap();
                siteTemplate.put("id", id);
                siteTemplate.put("name", name);
                siteTemplate.put("templateModel", templateModel);
                siteTemplate.put("active", active);
                siteTemplate.put("sampleSiteId", sampleSiteId);
                siteTemplate.put("subDomain", subDomain);
                results.add(siteTemplate);
            }
        }
        return results;
    }

    @Override
    public void updateExpiredDateForDemoSite(Date endDate) {
        this.getSessionFactory().getCurrentSession().
                createQuery("update Site s set s.endDate = :endDate where s.id in (select t.siteSample.id from Template t)").setDate("endDate", endDate).executeUpdate();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Site> findAll() {
        List<Site> results = this.getSessionFactory().getCurrentSession().createQuery(
                "select o from Site o order by o.id").list();
        return results;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Site getSite(String siteCd) {
        Site result = null;
        List<Site> results = this.getSessionFactory().getCurrentSession().createQuery("select o from Site o")
                .setMaxResults(1).list();
        if (results.size() == 1) result = results.get(0);
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Site getDefaultSite() {
        Site result = null;
        List<Site> results = this.getSessionFactory().getCurrentSession().createQuery("select o from Site o where o.defaultSite = 'Y'")
                .setMaxResults(1).list();
        if (results.size() == 1) result = results.get(0);
        return result;
    }
    @SuppressWarnings("unchecked")
    @Override
    public List<Site> getSitesByUser(Long userId) {
        return this.getSessionFactory().getCurrentSession().createQuery("select distinct s from UserRole ur join ur.site s where ur.user.id = :userId").setParameter("userId", userId).list();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Site> getPartnerSitesByUser(Long userId) {
        return this.getSessionFactory().getCurrentSession().createQuery("select distinct s from UserRole ur join ur.site s where s.siteType = 3 and ur.user.id = :userId").setParameter("userId", userId).list();
    }

}
