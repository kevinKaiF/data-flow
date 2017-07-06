<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="../images/favicon.ico" type="image/x-icon" />

    <title>输出数据源</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/datatables/1.10.15/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <!-- Custom Theme Style -->
    <link href="../styles/custom/custom.min.css" rel="stylesheet">
    <style type="text/css">
        tr.loading td {
            text-align: center;
        }

        .nav.child_menu > li > a.active {
            color: #1abb9c;
        }

        #dataSourceOutput-schemaList, #dataSourceOutput-table-detail {
            height: 400px;
            overflow: auto;
        }

        #dataSourceOutput-table-detail {
            background: #2A3F54;
        }

        #dataTable-form .checkbox-inline {
            color: rgba(255, 255, 255, 0.75);
        }

        #dataSourceOutput-options-doc {
            position: absolute;
            right: 10px;
            top: -1px;
            border: 1px solid #e5e5e5;
            padding: 3px 5px;
            border-radius: 3px;
            background: #e5e5e5;
            color: #1abb9c;
            font-weight: 800;
        }

    </style>
</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container">
        <div class="col-md-3 left_col">
            <div class="left_col scroll-view">
                <div class="navbar nav_title" style="border: 0;">
                    <a href="javascript:void(0)" class="site_title"><i class="fa fa-fire" style="border: none; color:#1abb9c;font-size: 1.5em"></i>
                        <span>Data flow</span></a>
                </div>

                <div class="clearfix"></div>

                <!-- menu profile quick info -->
                <div class="profile clearfix">
                    <div class="profile_pic">
                        <img src="../images/img.jpg" alt="..." class="img-circle profile_img">
                    </div>
                    <div class="profile_info">
                        <span>欢迎，</span>
                        <h2>${Session.user.username}</h2>
                    </div>
                </div>
                <!-- /menu profile quick info -->

                <br/>

                <!-- sidebar menu -->
                <div id="sidebar-menu" class="main_menu_side hidden-print main_menu">
                    <div class="menu_section">
                        <h3></h3>
                        <ul class="nav side-menu">
                            <li><a href="../dataInstance/"><i class="fa fa-random"></i> 数据实例 <span
                                    class="fa fa-chevron-right"></span></a>
                            </li>
                            <li class="active"><a href="../dataSourceOutput/"><i class="fa fa-database"></i>
                                输出数据源 <span class="fa fa-chevron-right"></span></a>
                            </li>
                            <li><a href="../dataNode/"><i class="fa fa-eye"></i> 节点监控 <span
                                    class="fa fa-chevron-right"></span></a>
                            </li>
                            <li><a href="../dataNodeConfiguration/"><i class="fa fa-cog"></i> 系统配置 <span
                                    class="fa fa-chevron-right"></span></a>
                            </li>
                        </ul>
                    </div>
                </div>
                <!-- /sidebar menu -->
            </div>
        </div>

        <!-- top navigation -->
        <div class="top_nav">
            <div class="nav_menu">
                <nav>
                    <div class="nav toggle">
                        <a id="menu_toggle"><i class="fa fa-bars"></i></a>
                    </div>

                    <ul class="nav navbar-nav navbar-right">
                        <li class="">
                            <a href="javascript:;" class="user-profile dropdown-toggle" data-toggle="dropdown"
                               aria-expanded="false">
                                <img src="../images/img.jpg" alt="">${Session.user.username}
                                <span class=" fa fa-angle-down"></span>
                            </a>
                            <ul class="dropdown-menu dropdown-usermenu pull-right">
                                <li><a href="../logout.html"><i class="fa fa-sign-out pull-right"></i> Log Out</a></li>
                            </ul>
                        </li>


                    </ul>
                </nav>
            </div>
        </div>
        <!-- /top navigation -->

        <!-- page content -->
        <div class="right_col" role="main">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="row">
                        <div class="x_panel">
                            <div class="x_title" style="border-bottom-width: 1px; padding: 0;" id="search_panel_header">
                                <h2>输出数据源搜索</h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
                                    </li>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content" style="display: none">
                                <form id="dataSourceOutput-searchForm"
                                      class="form-horizontal form-label-left">
                                    <div class="item form-group">
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataSourceOutput-search-type">类型
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <select class="form-control col-md-7 col-xs-12"
                                                    id="dataSourceOutput-search-type" name="type">
                                                <option value="">全部</option>
                                                <option value="10">MySQL</option>
                                                <option value="11">Oracle</option>
                                                <option value="12">PostGreSQL</option>
                                                <option value="13">SQLServer</option>
                                                <option value="20">Kafka</option>
                                                <option value="21">metaQ</option>
                                                <option value="22">rabbitMQ</option>
                                                <option value="23">activeMQ</option>
                                            </select>
                                        </div>
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataSourceOutput-search-options">配置
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataSourceOutput-search-options" name="options"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="item form-group">
                                        <div class="col-md-offset-2 col-sm-offset-2 col-md-4 col-sm-4 col-xs-12">
                                            <button id="dataSourceOutput-searchForm-submit" type="button"
                                                    class="btn btn-primary">搜索
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="x_panel">
                            <span class="section">输出数据源列表</span>
                            <a href="#dataSourceOutputModal" class="btn btn-default"
                               id="add"><i class="fa fa-plus-square-o"></i> 新增
                            </a>
                            <a href="#dataSourceOutputModal" class="btn btn-default"
                               id="edit"><i class="fa fa-edit"></i> 编辑
                            </a>
                            <a class="btn btn-default" data-toggle="modal"
                               id="delete"><i class="fa fa-minus-square-o"></i> 删除
                            </a>
                            <table id="dataSourceOutputTable" class="table table-hover jambo_table"
                                   style="width: 100%">
                            </table>

                            <div id="dataSourceOutputModal" class="modal fade bs-example-modal-lg" tabindex="-1"
                                 role="dialog" aria-hidden="true">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span
                                                    aria-hidden="true">×</span>
                                            </button>
                                            <h4 class="modal-title" id="myModalLabel">输出数据源</h4>
                                        </div>
                                        <div class="modal-body">
                                            <form id="dataSourceOutput-form"
                                                  class="form-horizontal form-label-left">
                                                <div class="item form-group">
                                                    <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                           for="dataSourceOutput-name">名称 <span
                                                            class="required">*</span>
                                                    </label>
                                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                                        <input class="form-control col-md-7 col-xs-12"
                                                                id="dataSourceOutput-name" name="name" required="required" data-validate-length-range="1,20">
                                                        </input>
                                                    </div>
                                                </div>
                                                <div class="item form-group">
                                                    <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                           for="dataSourceOutput-type">类型 <span
                                                            class="required">*</span>
                                                    </label>
                                                    <div class="col-md-6 col-sm-6 col-xs-12">
                                                        <select class="form-control col-md-7 col-xs-12"
                                                                id="dataSourceOutput-type" name="type">
                                                            <option value="10">MySQL</option>
                                                            <option value="11">Oracle</option>
                                                            <option value="12">PostGreSQL</option>
                                                            <option value="13">SQLServer</option>
                                                            <option value="20">Kafka</option>
                                                            <option value="21">metaQ</option>
                                                            <option value="22">rabbitMQ</option>
                                                            <option value="23">activeMQ</option>
                                                        </select>
                                                    </div>
                                                </div>
                                                <div class="item form-group">
                                                    <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                           for="dataSourceOutput-options">配置 <span
                                                            class="required">*</span>
                                                    </label>
                                                    <div class="col-md-6 col-sm-6 col-xs-12" style="position: relative">
                                                <textarea id="dataSourceOutput-options"
                                                          class="form-control col-md-7 col-xs-12"
                                                          rows="8"
                                                          name="options" required="required"></textarea>
                                                        <span id="dataSourceOutput-options-doc">doc</span>
                                                    </div>
                                                </div>
                                                <input type="hidden" name="id">
                                            </form>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-default antoclose"
                                                    data-dismiss="modal">关闭
                                            </button>
                                            <button type="button" id="dataSourceOutput-form-submit"
                                                    class="btn btn-primary antosubmit">保存
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div id="alertMessage" class="modal fade bs-example-modal-sm" tabindex="-1"
                 role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-sm">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">×</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">提示</h4>
                        </div>
                        <div class="modal-body">
                            <div id="alert-content" style="padding: 5px 20px"></div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="alert-close" class="btn btn-default antoclose"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="alert-submit" class="btn btn-primary antosubmit">确定</button>
                        </div>
                    </div>
                </div>
            </div>

            <div id="dataSourceOutput-options-modal" class="modal fade bs-example-modal-lg" tabindex="-1"
                 role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-md">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">×</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">模板</h4>
                        </div>
                        <div class="modal-body">
                            <ul id="myTab" class="nav nav-tabs bar_tabs" role="tablist">
                                <li role="presentation" class="active"><a href="#tab_content1" role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Mysql</a>
                                </li>
                            <#--<li role="presentation" class=""><a href="#tab_content1" role="tab" id="profile-tab" data-toggle="tab" aria-expanded="false">Oracle</a>-->
                            <#--</li>-->
                                <li role="presentation" class=""><a href="#tab_content5" role="tab" id="profile-tab2" data-toggle="tab" aria-expanded="false">Kafka</a>
                                </li>
                            </ul>
                            <div id="myTabContent" class="tab-content">
                                <div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="profile-tab">
                                        <textarea style="width: 100%;" rows="8" readonly>{
    "username":"",
    "password":"",
    "host":"",
    "port":"",
    "jdbcUrl":""
}</textarea>
                                </div>
                                <div role="tabpanel" class="tab-pane fade" id="tab_content5" aria-labelledby="profile-tab">
                                        <textarea style="width: 100%;" rows="8" readonly>{
    "bootstrap.servers":"",
} </textarea>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer">
                            <button type="button" id="alert-close" class="btn btn-default antoclose"
                                    data-dismiss="modal">关闭
                            </button>
                            <button type="button" id="alert-submit" class="btn btn-primary antosubmit">确定</button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
            <div class="pull-right github-ico">
                Github <a href="https://github.com/kevinKaiF/data-flow" target="_blank">Dataflow</a>
            </div>
            <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
    </div>
</div>

<!-- jQuery -->
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.js"></script>
<script src="https://cdn.bootcss.com/datatables/1.10.15/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.bootcss.com/datatables/1.10.15/js/dataTables.bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/jquery.form/4.2.1/jquery.form.min.js"></script>
<script src="../javascript/validator/validator.min.js"></script>
<script src="../javascript/custom/global.js"></script>
<script src="../javascript/custom/dataSourceOutput.js"></script>

</body>
</html>
