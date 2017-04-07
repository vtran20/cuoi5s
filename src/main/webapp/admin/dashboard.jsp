<%--<!DOCTYPE html>--%>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=utf-8" pageEncoding="UTF-8" %>
<%@include file="/common.jspf" %>

<title>Admin Home</title>
<div class="page-header">
    <h4>Dashboard
    </h4>
</div><!-- /.page-header -->

<div class="row">
    <div class="col-xs-12">
        <div class="row">
            <div class="col-xs-6 col-sm-4 widget-container-col">
                <!-- #section:custom/widget-box -->
                <div class="widget-box">
                    <div class="widget-header">
                        <h5 class="widget-title">Cấu Hình</h5>
                        <!-- #section:custom/widget-box.toolbar -->
                        <div class="widget-toolbar">
                            <a href="#" data-action="fullscreen" class="orange2">
                                <i class="ace-icon fa fa-expand"></i>
                            </a>

                            <a href="#" data-action="reload">
                                <i class="ace-icon fa fa-refresh"></i>
                            </a>

                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>

                            <a href="#" data-action="close">
                                <i class="ace-icon fa fa-times"></i>
                            </a>
                        </div>

                        <!-- /section:custom/widget-box.toolbar -->
                    </div>

                    <div class="widget-body">
                        <div class="widget-main">
                            <ul class="submenu">
                                <li class="">
                                    <a href="#/admin/sites/form" data-url="/admin/sites/form"><i class="menu-icon fa fa-caret-right"></i>Thông Tin WebSite</a>
                                </li>
                                <li class="">
                                    <a href="#/admin/sites/site_setting" data-url="/admin/sites/site_setting"><i class="menu-icon fa fa-caret-right"></i>Thiết Lập Cấu Hình</a>
                                </li>
                                <li class="">
                                    <a href="#/admin/sites/contactus_index" data-url="/admin/sites/contactus_index"><i class="menu-icon fa fa-caret-right"></i>Thông Tin Liên Hệ</a>
                                </li>
                                <li class="">
                                    <a href="#/admin/sites/support_index" data-url="/admin/sites/support_index"><i class="menu-icon fa fa-caret-right"></i>Hỗ Trợ Trực Tuyến</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- /section:custom/widget-box -->
            </div>
            <div class="col-xs-6 col-sm-4 widget-container-col">
                <!-- #section:custom/widget-box -->
                <div class="widget-box">
                    <div class="widget-header">
                        <h5 class="widget-title">Thiết Kết Website</h5>
                        <!-- #section:custom/widget-box.toolbar -->
                        <div class="widget-toolbar">
                            <a href="#" data-action="fullscreen" class="orange2">
                                <i class="ace-icon fa fa-expand"></i>
                            </a>

                            <a href="#" data-action="reload">
                                <i class="ace-icon fa fa-refresh"></i>
                            </a>

                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>

                            <a href="#" data-action="close">
                                <i class="ace-icon fa fa-times"></i>
                            </a>
                        </div>

                        <!-- /section:custom/widget-box.toolbar -->
                    </div>

                    <div class="widget-body">
                        <div class="widget-main">
                            <ul class="submenu">
                                <li class="">
                                    <a href="#/admin/sites/header" data-url="/admin/sites/header">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Header (Logo)
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/menu/index" data-url="/admin/menu/index">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Menu và Trang Web
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/sites/footer" data-url="/admin/sites/footer">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Footer
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/sites/support_index" data-url="/admin/sites/support_index"></a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- /section:custom/widget-box -->
            </div>
        </div>
        <div class="row">
            <div class="col-xs-6 col-sm-4 widget-container-col">
                <!-- #section:custom/widget-box -->
                <div class="widget-box">
                    <div class="widget-header">
                        <h5 class="widget-title">Nội Dung</h5>
                        <!-- #section:custom/widget-box.toolbar -->
                        <div class="widget-toolbar">
                            <a href="#" data-action="fullscreen" class="orange2">
                                <i class="ace-icon fa fa-expand"></i>
                            </a>

                            <a href="#" data-action="reload">
                                <i class="ace-icon fa fa-refresh"></i>
                            </a>

                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>

                            <a href="#" data-action="close">
                                <i class="ace-icon fa fa-times"></i>
                            </a>
                        </div>

                        <!-- /section:custom/widget-box.toolbar -->
                    </div>

                    <div class="widget-body">
                        <div class="widget-main">
                            <ul class="submenu">
                                <li class="">
                                    <a href="#/admin/news/create" data-url="/admin/news/create">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Tạo Tin Mới
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/news/news" data-url="/admin/news/news">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Danh Sách Tin
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/news/index" data-url="/admin/news/index">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Danh Mục Tin
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/gallery/index" data-url="/admin/gallery/index">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Gallery (Album)
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/images/view" data-url="/admin/images/view">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Thư Viện Hình Ảnh
                                    </a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>

                <!-- /section:custom/widget-box -->
            </div>
            <div class="col-xs-6 col-sm-4 widget-container-col">
                <!-- #section:custom/widget-box -->
                <div class="widget-box">
                    <div class="widget-header">
                        <h5 class="widget-title">Sản Phẩm</h5>
                        <!-- #section:custom/widget-box.toolbar -->
                        <div class="widget-toolbar">
                            <a href="#" data-action="fullscreen" class="orange2">
                                <i class="ace-icon fa fa-expand"></i>
                            </a>

                            <a href="#" data-action="reload">
                                <i class="ace-icon fa fa-refresh"></i>
                            </a>

                            <a href="#" data-action="collapse">
                                <i class="ace-icon fa fa-chevron-up"></i>
                            </a>

                            <a href="#" data-action="close">
                                <i class="ace-icon fa fa-times"></i>
                            </a>
                        </div>
                    </div>
                    <div class="widget-body">
                        <div class="widget-main">
                            <ul class="submenu">
                                <li class="">
                                    <a href="#/admin/catalog/products" data-url="/admin/catalog/products">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Danh Sách Sản Phẩm
                                    </a>
                                </li>
                                <li class="">
                                    <a href="#/admin/catalog/categories" data-url="/admin/catalog/categories">
                                        <i class="menu-icon fa fa-caret-right"></i>
                                        Danh Mục Sản Phẩm
                                    </a>
                                </li>
                                <li class="">
                                </li>
                                <li class="">
                                </li>
                                <li class="">
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
