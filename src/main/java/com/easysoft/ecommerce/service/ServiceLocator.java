package com.easysoft.ecommerce.service;

import com.easysoft.ecommerce.dao.*;
import com.easysoft.ecommerce.model.SiteProductService;
import com.easysoft.ecommerce.util.CacheData;
import com.easysoft.ecommerce.web.cache.CacheKeyGenerator;
import net.sf.ehcache.CacheManager;
import org.apache.velocity.app.VelocityEngine;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class ServiceLocator {
    public static final String SERVICE_LOCATOR_BEAN_NAME = "serviceLocator";

    @Autowired
    private UserSessionDao userSessionDao;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private GlobalConfigDao globalConfigDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserBalanceHistoryDao userBalanceHistoryDao;
    @Autowired
    private ProductDao productDao;
    @Autowired
    private ProductToProductDao productToProductDao;
    @Autowired
    private ProductVariantDao productVariantDao;
    @Autowired
    private ProductFileDao productFileDao;
    @Autowired
    private ProductAttributeValueDao productAttributeValueDao;
    @Autowired
    private CatalogDao catalogDao;
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private CmsAreaDao cmsAreaDao;
    @Autowired
    private CmsAreaContentDao cmsAreaContentDao;
    @Autowired
    private RefinementDao refinementDao;
    @Autowired
    private RefinementValueDao refinementValueDao;
    @Autowired
    private KeywordDao keywordDao;
    @Autowired
    private StringParamDao stringParamDao;
    @Autowired
    private StringParamValueDao stringParamValueDao;
    @Autowired
    private PromotionDao promotionDao;
    @Autowired
    private PromotionConditionDao promotionConditionDao;
    @Autowired
    private ConditionClassDao conditionClassDao;
    @Autowired
    private PromotionClassDao promotionClassDao;
    @Autowired
    private ShippingLocationDao shippingLocationDao;
    @Autowired
    private ShippingTypeDao shippingTypeDao;
    @Autowired
    private ShippingFeeDao shippingFeeDao;
    @Autowired
    private ShippingConfigDao shippingConfigDao;
    @Autowired
    private ShippingSiteDao shippingSiteDao;
    @Autowired
    private OrderSessionDao orderSessionDao;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private PaymentProviderDao paymentProviderDao;
    @Autowired
    private PaymentProviderSiteDao paymentProviderSiteDao;
    @Autowired
    private PaymentProviderParamDao paymentProviderParamDao;
    @Autowired
    private EmailTemplateDao emailTemplateDao;
    @Autowired
    private EmailSiteDao emailSiteDao;
    @Autowired
    private EmailPlanDao emailPlanDao;
    @Autowired
    private ContactUsDao contactUsDao;
    @Autowired
    private EmailMarketingDao emailMarketingDao;
    @Autowired
    private RowDao rowDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private MenuTemplateDao menuTemplateDao;
    @Autowired
    private NewsDao newsDao;
    @Autowired
    private NewsCategoryDao newsCategoryDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private SiteTemplateDao siteTemplateDao;
    @Autowired
    private SiteHeaderFooterDao siteHeaderFooterDao;
    @Autowired
    private SiteContactDao siteContactDao;
    @Autowired
    private SiteSupportDao siteSupportDao;
    @Autowired
    private SiteQuestionAnswerDao siteQuestionAnswerDao;
    @Autowired
    private WidgetTemplateDao widgetTemplateDao;
    @Autowired
    private VideoDao videoDao;
    @Autowired
    private SiteMenuPartContentDao siteMenuPartContentDao;
    @Autowired
    private AlbumDao albumDao;
    @Autowired
    private AlbumImageDao albumImageDao;
    @Autowired
    private VideoImportClassDao videoImportClassDao;
    @Autowired
    private SiteProductServiceDao siteProductServiceDao;

    @Autowired
    private ConfigurationService configurationService;
    @Autowired
    private SiteService siteService;
    @Autowired
    private JobsService jobsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRoleDao userRoleDao;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private FileSystemService fileSystemService;
    @Autowired
    private SearchService searchService;
    @Autowired
    private IndexService indexService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MailService mailService;
    @Autowired
    private ContentService contentService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private CacheManager cacheManager;
    @Autowired
    private CacheData cacheData;
    @Autowired
    private CacheKeyGenerator cacheKeyGenerator;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private VelocityEngine velocityEngine;
    @Autowired
    private StandardPBEStringEncryptor strongEncryptor;

    public MessageSource getMessageSource() {
        return messageSource;
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public TimeZone getTimeZone() {
        return TimeZoneHolder.getTimeZone();
    }

    public String getThemeName() {
        return ThemeNameHolder.getThemeName();
    }

    public ConfigurationService getConfigurationService() {
        return configurationService;
    }

    public SiteService getSiteService() {
        return siteService;
    }

    public JobsService getJobsService() {
        return jobsService;
    }

    public IndexService getIndexService() {
        return indexService;
    }

    public UserService getUserService() {
        return userService;
    }

    public UserRoleDao getUserRoleDao() {
        return userRoleDao;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public FileSystemService getFileSystemService() {
        return fileSystemService;
    }

    public SearchService getSearchService() {
        return searchService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public MailService getMailService() {
        return mailService;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public SiteDao getSiteDao() {
        return siteDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public UserBalanceHistoryDao getUserBalanceHistoryDao() {
        return userBalanceHistoryDao;
    }

    public UserSessionDao getUserSessionDao() {
        return userSessionDao;
    }

    public void setUserSessionDao(UserSessionDao userSessionDao) {
        this.userSessionDao = userSessionDao;
    }

    public ProductDao getProductDao() {
        return productDao;
    }

    public ProductToProductDao getProductToProductDao() {
        return productToProductDao;
    }

    public void setProductToProductDao(ProductToProductDao productToProductDao) {
        this.productToProductDao = productToProductDao;
    }

    public ProductVariantDao getProductVariantDao() {
        return productVariantDao;
    }

    public ProductAttributeValueDao getProductAttributeValueDao() {
        return productAttributeValueDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    public CatalogDao getCatalogDao() {
        return catalogDao;
    }

    public void setCatalogDao(CatalogDao catalogDao) {
        this.catalogDao = catalogDao;
    }

    public CmsAreaDao getCmsAreaDao() {
        return cmsAreaDao;
    }

    public void setCmsAreaDao(CmsAreaDao cmsAreaDao) {
        this.cmsAreaDao = cmsAreaDao;
    }

    public CmsAreaContentDao getCmsAreaContentDao() {
        return cmsAreaContentDao;
    }

    public void setCmsAreaContentDao(CmsAreaContentDao cmsAreaContentDao) {
        this.cmsAreaContentDao = cmsAreaContentDao;
    }

    public RefinementDao getRefinementDao() {
        return refinementDao;
    }

    public void setRefinementDao(RefinementDao refinementDao) {
        this.refinementDao = refinementDao;
    }

    public KeywordDao getKeywordDao() {
        return keywordDao;
    }

    public void setKeywordDao(KeywordDao keywordDao) {
        this.keywordDao = keywordDao;
    }

    public StringParamDao getStringParamDao() {
        return stringParamDao;
    }

    public StringParamValueDao getStringParamValueDao() {
        return stringParamValueDao;
    }

    public PromotionDao getPromotionDao() {
        return promotionDao;
    }

    public PromotionConditionDao getPromotionConditionDao() {
        return promotionConditionDao;
    }

    public ConditionClassDao getConditionClassDao() {
        return conditionClassDao;
    }

    public PromotionClassDao getPromotionClassDao() {
        return promotionClassDao;
    }

    public SystemContext getSystemContext() {
        SystemContext systemContext = (SystemContext) applicationContext.getBean(SystemContext.SYSTEM_CONTEXT_BEAN_NAME);
        return systemContext;
    }

    public ServletContext getServletContext() {
        return servletContext;
    }

    public ShippingLocationDao getShippingLocationDao() {
        return shippingLocationDao;
    }

    public ShippingTypeDao getShippingTypeDao() {
        return shippingTypeDao;
    }

    public ShippingFeeDao getShippingFeeDao() {
        return shippingFeeDao;
    }

    public ShippingConfigDao getShippingConfigDao() {
        return shippingConfigDao;
    }

    public OrderSessionDao getOrderSessionDao() {
        return orderSessionDao;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public PaymentProviderDao getPaymentProviderDao() {
        return paymentProviderDao;
    }

    public PaymentProviderParamDao getPaymentProviderParamDao() {
        return paymentProviderParamDao;
    }

    public EmailTemplateDao getEmailTemplateDao() {
        return emailTemplateDao;
    }

    public EmailSiteDao getEmailSiteDao() {
        return emailSiteDao;
    }

    public ContactUsDao getContactUsDao() {
        return contactUsDao;
    }

    public ProductFileDao getProductFileDao() {
        return productFileDao;
    }

    public EmailMarketingDao getEmailMarketingDao() {
        return emailMarketingDao;
    }

    public EmailPlanDao getEmailPlanDao() {
        return emailPlanDao;
    }

    public VelocityEngine getVelocityEngine() {
        return velocityEngine;
    }

    public StandardPBEStringEncryptor getStrongEncryptor() {
        return strongEncryptor;
    }

    public RowDao getRowDao() {
        return rowDao;
    }

    public MenuDao getMenuDao() {
        return menuDao;
    }

    public NewsCategoryDao getNewsCategoryDao() {
        return newsCategoryDao;
    }

    public AdminService getAdminService() {
        return adminService;
    }

    public TemplateDao getTemplateDao() {
        return templateDao;
    }

    public SiteTemplateDao getSiteTemplateDao() {
        return siteTemplateDao;
    }

    public SiteHeaderFooterDao getSiteHeaderFooterDao() {
        return siteHeaderFooterDao;
    }

    public CacheData getCacheData() {
        return cacheData;
    }

    public WidgetTemplateDao getWidgetTemplateDao() {
        return widgetTemplateDao;
    }

    public MenuTemplateDao getMenuTemplateDao() {
        return menuTemplateDao;
    }

    public NewsDao getNewsDao() {
        return newsDao;
    }

    public GlobalConfigDao getGlobalConfigDao() {
        return globalConfigDao;
    }

    public SiteSupportDao getSiteSupportDao() {
        return siteSupportDao;
    }

    public SiteContactDao getSiteContactDao() {
        return siteContactDao;
    }

    public SiteQuestionAnswerDao getSiteQuestionAnswerDao() {
        return siteQuestionAnswerDao;
    }

    public VideoDao getVideoDao() {
        return videoDao;
    }

    public VideoImportClassDao getVideoImportClassDao() {
        return videoImportClassDao;
    }

    public AlbumDao getAlbumDao() {
        return albumDao;
    }

    public AlbumImageDao getAlbumImageDao() {
        return albumImageDao;
    }

    public SiteMenuPartContentDao getSiteMenuPartContentDao() {
        return siteMenuPartContentDao;
    }

    public PaymentProviderSiteDao getPaymentProviderSiteDao() {
        return paymentProviderSiteDao;
    }

    public ShippingSiteDao getShippingSiteDao() {
        return shippingSiteDao;
    }

    public ContentService getContentService() {
        return contentService;
    }

    public RefinementValueDao getRefinementValueDao() {
        return refinementValueDao;
    }

    public SiteProductServiceDao getSiteProductServiceDao() {
        return siteProductServiceDao;
    }

    public CacheKeyGenerator getCacheKeyGenerator() {
        return cacheKeyGenerator;
    }
}
