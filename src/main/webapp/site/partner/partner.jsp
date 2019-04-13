<!DOCTYPE html>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<html>
<head>
    <title>Đối tác | Partner</title>
    <meta name="description" content=""/>
    <meta name="keywords" content=""/>
    <meta name="decorator" content="${template.templateCode}"/>
</head>

<body>
<h:cmscontent name="partner-conten-1"/>
<link rel="stylesheet" href="/themes/m3x/css/pages/page_job_inner.css" type="text/css"/>
<style>
    .list-inline > li {
        display: inline-block;
        text-align: justify;
        padding-left: 0;
        padding-right: 0;
    }
</style>
<div class="image-block margin-bottom-20">
    <div class="container">
        <div class="company-description">
            <h2>Trở Thành Đối Tác Của Chúng Tôi</h2>
            <ul class="list-inline benefits">
                <li><i class="rounded-x fa fa-money"></i> Bạn muốn tìm kiếm một công việc với thu nhập hàng tháng ổn định và kéo dài những năm tiếp theo.</li>
                <li><i class="rounded-x fa fa-home"></i> Bạn muốn làm việc ở đâu tùy thích - tại quán cà phê, hay sân vườn nhà bạn và thoải mái về giờ giấc.</li>
                <li><i class="rounded-x fa fa-simplybuilt"></i> Bạn không cần phải phải xây dựng website từ đầu, sử dụng hệ thống và cơ sở hạ tầng của chúng tôi, việc xây dựng website cho khách hàng của bạn trở nên đơn giản và nhanh chóng.</li>
                <li><i class="rounded-x fa fa-server"></i> Bạn không cần tốn chi phí xây dựng và quản lý cơ sở hạ tầng phục vụ cho việc Hosting website và các dịch vụ khác</li>
            </ul>
            <div class="margin-bottom-20"></div>
            <%--<button class="btn-u btn-bordered btn-u-dark" type="button"> Learn More</button>--%>
        </div>
    </div>
</div>

<spring:eval expression="T(com.easysoft.ecommerce.controller.SessionUtil).isLoggedIn(pageContext.request, pageContext.response)" var="isLoggedIn"/>
    <c:choose>
        <c:when test="${isLoggedIn == 'true'}">
            <spring:eval expression="serviceLocator.userDao.findById(T(java.lang.Long).valueOf(sessionObject.USER_ID))" var="currentUser"/>
            <c:choose>
                <c:when test="${currentUser.partnerStatus != 'Y'}">
                    <div class="container content-sm">
                        <div class="title-v1 no-margin-bottom">
                            <h3 class="no-margin-bottom">Chúng tôi mong muốn hợp tác với các bạn nhằm mang lại chất lượng dịch vụ, sự hỗ trợ tốt nhất cho khách hàng.</h3>
                        </div>
                    </div>
                    <div class="service-block-v4">
                        <div class="container content-sm">
                            <div class="row">
                                <div class="col-md-6 service-desc md-margin-bottom-40">
                                    <i class="fa fa-user"></i>
                                    <h3>Personal Partner</h3>
                                    <p class="no-margin-bottom">Đối tác là những cá nhân, các sinh viên, kỹ sư ngành công nghệ thông tin muốn có một công việc thêm và thu nhập cao, ổn định. </p>
                                    <div class="margin-top-30"><a class="btn-u btn-bordered btn-u-dark show-confirm" href="#" hreflang="/site/partner/upgrade_personal_partner.html">Upgrade Personal Partner</a></div>
                                </div>
                                <div class="col-md-6 service-desc md-margin-bottom-40">
                                    <i class="fa fa-building"></i>
                                    <h3>Business Partner</h3>
                                    <p class="no-margin-bottom">Đối tác là những công ty Công Nghệ Thông Tin muốn cung cấp website cho khách hàng của mình hoặc muốn mở rộng sang lĩnh vực thiết kế website. </p>
                                    <div class="margin-top-30"><a class="btn-u btn-bordered btn-u-dark show-confirm" href="#" hreflang="/site/partner/upgrade_business_partner.html">Upgrade Business Partner</a></div>
                                </div>
                            </div><!--/end row-->
                        </div><!--/end container-->
                    </div>
                </c:when>
                <c:otherwise>
                    <c:choose>
                        <c:when test="${currentUser.partner == '1'}">
                            <div class="container content-sm">
                                <div class="title-v1 no-margin-bottom">
                                    <h3 class="no-margin-bottom">Chúng tôi mong muốn hợp tác với các bạn nhằm mang lại chất lượng dịch vụ, sự hỗ trợ tốt nhất cho khách hàng.</h3>
                                </div>
                            </div>
                            <div class="service-block-v4">
                                <div class="container content-sm">
                                    <div class="row">
                                        <div class="col-md-12 service-desc md-margin-bottom-40">
                                            <i class="fa fa-building"></i>
                                            <h3>Business Partner</h3>
                                            <p class="no-margin-bottom">Đối tác là những công ty Công Nghệ Thông Tin muốn cung cấp website cho khách hàng của mình hoặc muốn mở rộng sang lĩnh vực thiết kế website. </p>
                                            <div class="margin-top-30"><a class="btn-u btn-bordered btn-u-dark show-confirm" href="#" hreflang="/site/partner/upgrade_business_partner.html">Upgrade Business Partner</a></div>
                                        </div>
                                    </div><!--/end row-->
                                </div><!--/end container-->
                            </div>
                        </c:when>
                        <c:otherwise>
                        <div class="container content-sm">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="panel panel-red margin-bottom-40">
                                        <div class="panel-heading">
                                            <h3 class="panel-title"><i class="fa fa-tasks"></i> <fmt:message key="site.partner.site"/></h3>
                                        </div>
                                        <div class="panel-body">
                                            <form class="form-horizontal" role="form" action="/site/partner/create-partner-website.html" id="form" method="post">
                                                <h:frontendmessage _messages="${messages}"/>
                                                <spring:eval expression="serviceLocator.templateDao.findUniqueBy('templateModel', 'WEB_FOR_PARTNER')" var="template"/>
                                                <input name="templateId" type="hidden" value="${template.id}">
                                                <div class="form-group">
                                                    <label for="siteCode" class="col-lg-3 control-label"><fmt:message key="site.register.sitecode"/></label>
                                                    <div class="col-lg-9">
                                                        <input type="text" class="form-control" name="siteCode" id="siteCode" maxlength="50" placeholder='<fmt:message key="site.subdomain.example"/>' value="${sessionObject.siteCode}" autofocus>
                                                        <spring:eval expression="site.getSiteParam('FREE_SITE_DOMAIN')" var="siteUrl"/>
                                                        <c:if test="${empty siteUrl}">
                                                            <spring:eval expression="systemContext.getGlobalConfig('free.site.domain')" var="siteUrl"/>
                                                        </c:if>
                                                        <p class="help-block" id="subdomain"><fmt:message key="your.website"/>: <b></b>.${siteUrl}</p>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <label class="col-lg-3 control-label"></label>
                                                    <div class="col-lg-9">
                                                        <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SITE_KEY')" var="siteKey"/>
                                                        <spring:eval expression="systemContext.getGlobalConfig('CAPTCHA_GOOGLE_SECRET_KEY')" var="siteSecret"/>
                                                        <spring:eval expression="T(com.easysoft.ecommerce.util.WebUtil).createSToken(siteSecret)" var="encryptedString"/>
                                                        <div class="g-recaptcha" data-sitekey="${siteKey}" data-stoken="${encryptedString}"></div>
                                                    </div>
                                                </div>
                                                <div class="form-group">
                                                    <div class="col-lg-offset-3 col-lg-9">
                                                        <button type="submit" class="btn-u btn-u-red"><fmt:message key="site.create.new.business.website"/></button>
                                                    </div>
                                                </div>
                                            </form>
                                        </div>
                                        <spring:eval expression="serviceLocator.siteDao.getPartnerSitesByUser(sessionObject.USER_ID)" var="partnerSites"/>
                                        <c:if test="${!empty partnerSites && fn:length(partnerSites) > 0}">
                                            <div class="panel-body">
                                                <table class="table">
                                                    <thead>
                                                    <tr>
                                                        <th>#</th>
                                                        <th><fmt:message key="site.primary.url"/></th>
                                                        <th class="hidden-xs"><fmt:message key="site.website.info"/></th>
                                                        <th><fmt:message key="site.website.expired"/></th>
                                                        <th class="hidden-xs"></th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    <c:forEach varStatus="thisSite" items="${partnerSites}">
                                                        <tr>
                                                            <td>${thisSite.index+1}</td>
                                                            <c:choose>
                                                                <c:when test="${!empty thisSite.current.domain}">
                                                                    <td><a target="_blank" href="http://${thisSite.current.domain}">${thisSite.current.domain}</a><br>
                                                                        <a target="_blank" href="http://${thisSite.current.subDomain}">${thisSite.current.subDomain}</a></td>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <td><a target="_blank" href="http://${thisSite.current.subDomain}">${thisSite.current.subDomain}</a></td>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            <td class="hidden-xs">${thisSite.current.name}</td>
                                                            <spring:eval expression="serviceLocator.getStrongEncryptor().decrypt(currentUser.password)" var="password" />
                                                            <c:set var="code" value="domain=${thisSite.current.subDomain}&j_username=${currentUser.username}&j_password=${password}"/>
                                                            <spring:eval expression="T(com.easysoft.ecommerce.service.impl.URLUTF8Encoder).encode(serviceLocator.getStrongEncryptor().encrypt(code))" var="encryptText" />
                                                            <spring:eval expression="serviceLocator.getProductDao().findUniqueBy('model','HOSTING', site.id)" var="product"/>
                                                            <td>
                                                                <spring:eval expression="site.siteParamsMap.get('DATE_FORMAT')" var="dateFormat"/>
                                                                <fmt:formatDate pattern="${dateFormat}" value="${thisSite.current.endDate}"/>
                                                                <div class="btn-group hidden-sm hidden-md hidden-lg">
                                                                    <button data-toggle="dropdown" class="btn-u dropdown-toggle" type="button" aria-expanded="true">
                                                                        Action
                                                                        <i class="fa fa-angle-down"></i>
                                                                    </button>
                                                                    <ul role="menu" class="dropdown-menu">
                                                                        <li><a href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a></li>
                                                                        <li class="divider"></li>
                                                                        <li><a target="_blank" href="http://${thisSite.current.subDomain}/site/go-to-sitemanager.html?code=${encryptText}"><i class="fa fa-globe"></i> <fmt:message key="site.site.admin"/></a></li>
                                                                        <li><a href="/site/modules.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-cubes"></i> <fmt:message key="site.module.list"/></a></li>
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                            <td class="hidden-xs">
                                                                <div class="btn-group">
                                                                    <button data-toggle="dropdown" class="btn-u dropdown-toggle" type="button" aria-expanded="true">
                                                                        Action
                                                                        <i class="fa fa-angle-down"></i>
                                                                    </button>
                                                                    <ul role="menu" class="dropdown-menu">
                                                                        <li><a href="/site/checkout/addtocart.html?productId=${product.id}&thisSiteId=${thisSite.current.id}"><i class="fa fa-refresh"></i> <fmt:message key="site.site.renew"/></a></li>
                                                                        <li class="divider"></li>
                                                                        <li><a target="_blank" href="http://${thisSite.current.subDomain}/site/go-to-sitemanager.html?code=${encryptText}"><i class="fa fa-globe"></i> <fmt:message key="site.site.admin"/></a></li>
                                                                        <li><a href="/site/modules.html?thisSiteId=${thisSite.current.id}"><i class="fa fa-cubes"></i> <fmt:message key="site.module.list"/></a></li>
                                                                    </ul>
                                                                </div>
                                                            </td>
                                                        </tr>
                                                    </c:forEach>
                                                    </tbody>
                                                </table>
                                            </div>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </div>
                        </c:otherwise>
                    </c:choose>
                </c:otherwise>
            </c:choose>
        </c:when>
        <c:otherwise>
            <div class="container content-sm">
                <div class="title-v1 no-margin-bottom">
                    <h3 class="no-margin-bottom">Chúng tôi mong muốn hợp tác với các bạn nhằm mang lại chất lượng dịch vụ, sự hỗ trợ tốt nhất cho khách hàng.</h3>
                </div>
            </div>
            <div class="service-block-v4">
                <div class="container content-sm">
                    <div class="row">
                        <div class="col-md-6 service-desc md-margin-bottom-40">
                            <i class="fa fa-user"></i>
                            <h3>Personal Partner</h3>
                            <p class="no-margin-bottom">Đối tác là những cá nhân, các sinh viên, kỹ sư ngành công nghệ thông tin muốn có một công việc thêm và thu nhập cao, ổn định. </p>
                            <div class="margin-top-30"><a class="btn-u btn-bordered btn-u-dark" href="/site/partner/partner_register.html?partner=1">Đăng Ký Personal Partner</a></div>
                        </div>
                        <div class="col-md-6 service-desc md-margin-bottom-40">
                            <i class="fa fa-building"></i>
                            <h3>Business Partner</h3>
                            <p class="no-margin-bottom">Đối tác là những công ty Công Nghệ Thông Tin muốn cung cấp website cho khách hàng của mình hoặc muốn mở rộng sang lĩnh vực thiết kế website. </p>
                            <div class="margin-top-30"><a class="btn-u btn-bordered btn-u-dark" href="/site/partner/partner_register.html?partner=2">Đăng Ký Business Partner</a></div>
                        </div>

                    </div><!--/end row-->
                </div><!--/end container-->
            </div>
        </c:otherwise>
    </c:choose>
<div class="container content">
    <div class="heading margin-bottom-20">
        <h2>Chi tiết về chương trình đối tác</h2>
        <%--<p>Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged</p>--%>
    </div>
    <style>
        td ol {
            padding: 0 0 0 13px;
        }
        td ol li {
            margin-bottom: 3px;
            line-height: 25px;
        }
        table.align-center tr th, table.align-center tr td {
            text-align: center;
        }
    </style>
    <table class="table">
        <thead>
        <tr>
            <th width="20%"></th>
            <th>Personal Partner</th>
            <th>Business Partner</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Yêu Cầu Và Nghĩa Vụ</td>
            <td>
                <ol>
                    <li> Tìm kiếm khách hàng</li>
                    <li> Tư vấn hỗ trợ cho khách hàng của bạn</li>
                    <li> Cung cấp cho chúng tôi các lỗi phát sinh trong quá trình hỗ trợ, thiết kế website cho khách hàng</li>
                    <li> Cung cấp cho chúng tôi, các chức năng và modules mà khách hàng của bạn yêu cầu.</li>
                </ol>
            </td>
            <td>
                <ol>
                    <li> Tìm kiếm khách hàng</li>
                    <li> Tư vấn hỗ trợ cho khách hàng của bạn</li>
                    <li> Cung cấp cho chúng tôi các lỗi phát sinh trong quá trình hỗ trợ, thiết kế website cho khách hàng</li>
                    <li> Cung cấp cho chúng tôi, các chức năng và modules mà khách hàng của bạn yêu cầu.</li>
                </ol>
            </td>
        </tr>
        <tr>
            <td>Website Riêng</td>
            <td>Không</td>
            <td>Có website riêng để marketing, giới thiệu, giao dịch và quản lý khách hàng của mình.</td>
        </tr>
        <tr>
            <td>Chi Phí</td>
            <td>Miễn Phí</td>
            <td>Phí duy trì website. Chú ý: sẽ miễn phí nếu có tối thiểu 10 khách hàng từ website của đối tác.</td>
        </tr>
        <tr>
            <td>Các bước để trở thành đối tác</td>
            <td>
                <ol>
                    <li>Đăng ký tài khoản đối tác cá nhân</li>
                    <li>Chúng tôi sẽ liên hệ (gởi email hoặc điện thoại) và sau đó xác nhận bạn đã trở thành đối tác của chúng tôi</li>
                    <li>Sau khi xác nhận trở thành đối tác, các website tạo từ tài khoản của bạn sẽ được sẽ được hưởng quyền lợi như là đối tác theo quy định.<br>Chú ý: Sau khi tạo website và bàn giao cho khách hàng, bạn vẫn hưởng quyền lợi từ website bạn đã tạo ra.</li>
                </ol>
            </td>
            <td>
                <ol>
                    <li>Đăng ký tài khoản đối tác doanh nghiệp (hoặc nâng cấp từ tài khoản cá nhân nếu có)</li>
                    <li>Chúng tôi sẽ liên hệ (gởi email hoặc điện thoại) và sau đó xác nhận doanh nghiệp đã trở thành đối tác của chúng tôi.</li>
                    <li>Doanh nghiệp sẽ tạo website và kinh doanh độc lập trên website của mình</li>
                    <li>Các website tạo từ website của công ty sẽ được hưởng quyền lợi như là đối tác doanh nghiệp theo quy định.</li>
                </ol>
            </td>
        </tr>
        <tr>
            <td>Thanh Toán</td>
            <td>
                <ol>
                    <li>Nếu bạn trả tiền từ tài khoản của bạn, tổng chi phí sẽ được khấu trừ trực tiếp trên đơn hàng. Có
                        nghĩa là nếu bạn là đối tác và trả tiền cho khách hàng của bạn, tổng đơn hàng 500.000 VND, bạn
                        được hoa hồng 40% có nghĩa là bạn chỉ phải trả 300.000 VND trên đơn hàng của bạn. Hệ thống sẽ tự
                        động hiển thị chiết khấu trên đơn hàng.
                    </li>
                    <li>Nếu người trả tiền là khách hàng của bạn, khoảng hoa hồng 40% sẽ được chuyển vào tài khoản của
                        bạn sau khi giao dịch hoàn tất (tùy theo trả trước hay trả sau). Bạn có thể dùng số tiền này để
                        thanh toán cho khách hàng khác của bạn ở những đơn hàng sau.
                    </li>
                </ol>
            </td>
            <td>
                <ol>
                    <li>Tất cả mọi thanh toán đối với khách hàng sẽ được thông qua website của bạn và độc lập. Trong trường hợp bạn
                        trả tiền từ tài khoản của bạn, tổng chi phí sẽ được khấu trừ trực tiếp trên đơn hàng. Có nghĩa
                        là nếu bạn là đối tác và trả tiền cho khách hàng của bạn, tổng đơn hàng 500.000 VND, bạn
                        được hoa hồng 50% có nghĩa là bạn chỉ phải trả 250.000 VND trên đơn hàng này. Hệ thống sẽ tự
                        động hiển thị chiếc khấu trên đơn hàng.
                    </li>
                    <li>Trong trường hợp khách hàng trả trực tiếp trên website của bạn, nếu khách hàng chọn trả sau (trả
                        qua tài khoản ngân hàng, hoặc mang đến công ty), đơn đặt hàng cần bước xác nhận là giao dịch đã
                        thành công. Sau khi xác nhận khách hàng đã thanh toán, bạn cần chuyển trạng thái đơn hàng sang
                        "Đã Thanh Toán", đơn hàng sẽ có hiệu lực ngay sau đó.<br>
                        Sau khi bạn chuyển trạng thái đơn hàng của khách hàng sang "Đã Thanh Toán", hê thống sẽ tự động
                        trừ một khoản tiền trong tài khoản partner tương ứng với số tiền trong đơn hàng sau khi trừ tiền
                        hoa hồng.
                        <br>
                        Ví dụ: Khách hàng của bạn thanh toán 1.000.000 VND, bạn được hưởng 40% trên khoảng tiền này
                        (400.000 VND). Sau khi xác nhận "Đã Thanh Toán", hệ thống sẽ trừ 600.000 VND trong tài khoản
                        Partner của bạn.
                    </li>
                    <li>
                        Trong trường hợp chọn trả trước (thanh toán bằng thẻ tín dụng), quá trình xử lý giống như mục 2.
                        Tuy nhiên bạn không cần phải xác nhận đơn hàng đã thanh toán. Mọi thứ sẽ tự động.
                    </li>
                </ol>
            </td>
        </tr>
        <tr>
            <td></td>
            <td></td>
            <td></td>
        </tr>
        </tbody>
    </table>

    <div class="headline"><h2>Bảng Chiếc Khấu</h2></div>
    <table class="table table-bordered align-center">
        <thead>
        <tr>
            <th></th>
            <th colspan="2">Personal Partner</th>
            <th colspan="2">Business Partner</th>
        </tr>
        <tr>
            <th>Level</th>
            <th>Năm Đầu Tiên</th>
            <th>Các Năm Tiếp Theo</th>
            <th>Năm Đầu Tiên</th>
            <th>Các Năm Tiếp Theo</th>
        </tr>
        </thead>
        <tbody>
        <tr>
            <td>Level 1 (0-10 khách hàng)</td>
            <td>40%</td>
            <td>10%</td>
            <td>50%</td>
            <td>20%</td>
        </tr>
        <tr>
            <td>Level 2 (11-110 khách hàng)</td>
            <td>40%</td>
            <td>15%</td>
            <td>50%</td>
            <td>25%</td>
        </tr>
        <tr>
            <td>Level 3 (101-500 khách hàng)</td>
            <td>40%</td>
            <td>20%</td>
            <td>50%</td>
            <td>30%</td>
        </tr>
        <tr>
            <td>Level 4 (501-1000 khách hàng)</td>
            <td>40%</td>
            <td>25%</td>
            <td>50%</td>
            <td>35%</td>
        </tr>
        <tr>
            <td>Level 5 (Trên 1000 khách hàng)</td>
            <td>40%</td>
            <td>30%</td>
            <td>50%</td>
            <td>40%</td>
        </tr>
        </tbody>
    </table>
</div>
<h:cmscontent name="partner-conten-1"/>
<script type="text/javascript">
    $(document).ready(function () {
        $(".show-confirm").click(function () {
            var object = $(this);
            BootstrapDialog.show({
                title: '<fmt:message key="common.confirm.title"/>',
                message: '<fmt:message key='site.do.you.want.upgrade.partner'/>',
                buttons: [{
                    label: 'Yes',
                    action: function(dialog) {
                        dialog.close();
                        window.location.href = object.attr("hreflang");
                    }
                }, {
                    label: 'No',
                    action: function(dialog) {
                        dialog.close();
                    }
                }]
            });

        });

        $.validator.addMethod("siteCodeFormat", function(value, element) {
            reg = /^[a-z0-9._-]+$/i;
            return reg.test(value)
        }, "<fmt:message key="site.register.sitecode.format.invalid"/>");

        $("#form").validate({
            rules:{
                siteCode:{
                    required:true,
                    siteCodeFormat:true,
                    remote:"/site/checkwebsite.html"
                },
                captcha:"required"

            },

            messages:{
                siteCode:{
                    required:"<span><fmt:message key="site.register.sitecodeisrequired"/></span>",
                    remote:jQuery.format("<fmt:message key="site.register.sitecodeinused"/>")
                },
                captcha:"<fmt:message key="site.register.captchaisrequired"/>"

            },
            highlight:function (label) {
                $(label).closest('.form-group').removeClass('has-success');
                $(label).closest('.form-group').addClass('has-error');
            },
            success:function (label) {
                $(label).closest('.form-group').removeClass('has-error');
                $(label).closest('.form-group').addClass('has-success');
            }
        });

        $('#form input[name="siteCode"]').blur(function () {
            $("p#subdomain b").text($(this).attr("value"));
        });

    });

    //onload call.
    $(document).ready(function() {
        var code = $('#form input[name="siteCode"]');
        $("p#subdomain b").text($(code).attr("value"));
    });
</script>

</body>
</html>
