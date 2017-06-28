<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <title>数据实例</title>
    <link href="https://cdn.bootcss.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/font-awesome/4.6.3/css/font-awesome.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.css" rel="stylesheet">
    <link href="https://cdn.bootcss.com/datatables/1.10.15/css/dataTables.bootstrap.min.css" rel="stylesheet">
    <!-- bootstrap-progressbar -->
<#--<link href="../styles/bootstrap-progressbar/bootstrap-progressbar-3.3.4.min.css" rel="stylesheet">-->
    <!-- bootstrap-daterangepicker -->
<#--<link href="https://cdn.bootcss.com/bootstrap-daterangepicker/2.1.25/daterangepicker.min.css" rel="stylesheet">-->
    <!-- Custom Theme Style -->
    <link href="../styles/custom/custom.min.css" rel="stylesheet">
    <style type="text/css">
        tr.loading td {
            text-align: center;
        }

        .nav.child_menu > li > a.active {
            color: #1abb9c;
        }

        #dataInstance-schemaList, #dataInstance-table-detail {
            height: 400px;
            overflow: auto;
        }

        #dataInstance-table-detail {
            background: #2A3F54;
        }

        #dataTable-form .checkbox-inline {
            color: rgba(255, 255, 255, 0.75);
        }

    </style>
</head>

<body class="nav-md">
<div class="container body">
    <div class="main_container">
        <div class="col-md-3 left_col">
            <div class="left_col scroll-view">
                <div class="navbar nav_title" style="border: 0;">
                    <a href="javascript:void(0)" class="site_title"><i class="fa fa-paw"></i>
                        <span>Data flow!</span></a>
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
                            <li class="active"><a href="../dataInstance/"><i class="fa fa-random"></i> 数据实例 <span
                                    class="fa fa-chevron-right"></span></a>
                            </li>
                            <li><a href="../dataSourceOutput/"><i class="fa fa-database"></i>
                                输出数据源 <span class="fa fa-chevron-right"></span></a>
                            </li>
                        <#--<li><a href="../nodeMonitor/"><i class="fa fa-eye"></i> 节点监控 <span-->
                        <#--class="fa fa-chevron-right"></span></a>-->
                        <#--</li>-->
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

                        <li role="presentation" class="dropdown">
                            <a href="javascript:;" class="dropdown-toggle info-number" data-toggle="dropdown"
                               aria-expanded="false">
                                <i class="fa fa-envelope-o"></i>
                                <span class="badge bg-green">6</span>
                            </a>
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
                    <div class="x_panel">
                        <div class="x_content">
                            <div class="row">
                                <span class="section">实例搜索</span>
                                <form id="dataInstance-searchForm"
                                      class="form-horizontal form-label-left">
                                    <div class="item form-group">
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-name">name
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataInstance-name" name="name"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-name">host
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataInstance-name" name="host"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                    </div>
                                    <div class="item form-group">
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-name">nodePath
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataInstance-name" name="nodePath"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-name">status
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <select class="form-control col-md-7 col-xs-12" name="status">
                                                <option value="">全部</option>
                                                <option value="-1">创建中</option>
                                                <option value="0">已创建</option>
                                                <option value="1">启动</option>
                                                <option value="2">关停</option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="item form-group">
                                        <div class="col-md-offset-2 col-sm-offset-2 col-md-4 col-sm-4 col-xs-12">
                                            <button id="dataInstance-searchForm-submit" type="button"
                                                    class="btn btn-primary">搜索
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>

                            <span class="section">实例列表</span>
                            <a href="#dataInstanceModal" class="btn btn-default"
                               id="add"><i class="fa fa-plus-square-o"></i> 新增
                            </a>
                            <a href="#dataInstanceModal" class="btn btn-default"
                               id="edit"><i class="fa fa-edit"></i> 编辑
                            </a>
                            <a class="btn btn-default" data-toggle="modal"
                               id="delete"><i class="fa fa-minus-square-o"></i> 删除
                            </a>
                            <table id="dataInstanceTable" class="table table-hover jambo_table" style="width: 100%">
                            </table>

                            <div id="dataInstanceModal" class="modal fade bs-example-modal-lg" tabindex="-1"
                                 role="dialog" aria-hidden="true">
                                <div class="modal-dialog modal-lg">
                                    <div class="modal-content">

                                        <div class="modal-header">
                                            <button type="button" class="close" data-dismiss="modal"><span
                                                    aria-hidden="true">×</span>
                                            </button>
                                            <h4 class="modal-title" id="myModalLabel">数据实例</h4>
                                        </div>
                                        <div class="modal-body">
                                            <div id="wizard" class="form_wizard wizard_horizontal">
                                                <ul class="wizard_steps">
                                                    <li>
                                                        <a href="#step-1" id="step1">
                                                            <span class="step_no">1</span>
                                                            <span class="step_descr">
                                                                              Step 1<br/>
                                                                              <small>Step 1 description</small>
                                                                          </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#step-2">
                                                            <span class="step_no">2</span>
                                                            <span class="step_descr">
                                                                  Step 2<br/>
                                                                  <small>Step 2 description</small>
                                                              </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#step-3">
                                                            <span class="step_no">3</span>
                                                            <span class="step_descr">
                                                                              Step 3<br/>
                                                                <small>Step 3 description</small>
                                                            </span>
                                                        </a>
                                                    </li>
                                                </ul>
                                                <div id="step-1">
                                                    <form id="dataInstance-form"
                                                          class="form-horizontal form-label-left">

                                                        <div class="item form-group">
                                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                                   for="dataInstance-name">name <span
                                                                    class="required">*</span>
                                                            </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input type="text" id="dataInstance-name" name="name"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                                   for="dataInstance-slaveId">slaveId <span
                                                                    class="required">*</span>
                                                            </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input type="number" id="dataInstance-slaveId"
                                                                       name="slaveId" required="required"
                                                                       class="form-control col-md-7 col-xs-12">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="middle-name"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">type <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <select class="form-control col-md-7 col-xs-12"
                                                                        name="type" required="required">
                                                                    <option value="1">mysql</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label class="control-label col-md-3 col-sm-3 col-xs-12"
                                                                   for="dataInstance-host">host <span
                                                                    class="required">*</span>
                                                            </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input type="text" id="dataInstance-host" name="host"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="dataInstance-port"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">port <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-port"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="number" name="port">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="dataInstance-jdbcUrl"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">jdbcUrl <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-jdbcUrl"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="text" name="jdbcUrl">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="dataInstance-username"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">username <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-username"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="text" name="username">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="dataInstance-password"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">password <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-password"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="password" name="password">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="middle-name"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">whiteFilter </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-whiteFilter"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="text" name="whiteFilter">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="middle-name"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">blackFilter </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-blackFilter"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="text" name="blackFilter">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="middle-name"
                                                                   class="control-label col-md-3 col-sm-3 col-xs-12">transformScript </label>
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <textarea id="dataInstance-transformScript"
                                                                          class="form-control col-md-7 col-xs-12"
                                                                          rows="5"
                                                                          name="transformScript"></textarea>
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-id"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="hidden" name="id">
                                                            </div>
                                                        </div>
                                                    </form>

                                                </div>
                                                <div id="step-2" style="display: none">
                                                    <div class="col-md-4 col-sm-4 col-xs-6">
                                                        <div id="dataInstance-schemaList"
                                                             class="main_menu_side hidden-print main_menu left_col"
                                                             style="max-height: 400px; overflow: auto"
                                                             class="x-content">

                                                        </div>
                                                    </div>
                                                    <div class="col-md-8 col-sm-8 col-xs-6">
                                                        <div id="dataInstance-table-detail" class="x-content">
                                                            <div style="margin: 0 auto; margin-top: 50px; text-align: center">
                                                                <h3>配置过滤的表字段</h3>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div id="step-3" style="height: 450px">
                                                    <div class="accordion" id="accordion" role="tablist"
                                                         aria-multiselectable="false">
                                                        <div class="panel">
                                                            <a class="panel-heading" role="tab" id="headingOne"
                                                               data-toggle="collapse" data-parent="#accordion"
                                                               href="#collapseOne" aria-expanded="true"
                                                               aria-controls="collapseOne">
                                                                <h4 class="panel-title">输出映射列表</h4>
                                                            </a>
                                                            <div id="collapseOne" class="panel-collapse collapse in"
                                                                 role="tabpanel" aria-labelledby="headingOne">
                                                                <div class="panel-body" style="background: white">
                                                                    <table id="dataOutputMappingTable"
                                                                           class="table table-hover jambo_table"
                                                                           style="width: 100%">
                                                                    </table>
                                                                </div>
                                                            </div>
                                                        </div>
                                                        <div class="panel">
                                                            <a class="panel-heading collapsed" role="tab"
                                                               id="headingTwo" data-toggle="collapse"
                                                               data-parent="#accordion" href="#collapseTwo"
                                                               aria-expanded="false" aria-controls="collapseTwo">
                                                                <h4 class="panel-title">输出映射新增</h4>
                                                            </a>
                                                            <div id="collapseTwo" class="panel-collapse collapse"
                                                                 role="tabpanel" aria-labelledby="headingTwo">
                                                                <div class="panel-body" style="background: white">
                                                                    <form id="dataOutputMapping-form"
                                                                          class="form-horizontal form-label-left">

                                                                        <div class="item form-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataOutputMapping-schemaName">schemaName <span
                                                                                    class="required">*</span>
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12">
                                                                                <select id="dataOutputMapping-schemaName"
                                                                                        class="form-control col-md-7 col-xs-12"
                                                                                        name="schemaName"
                                                                                        required="required">
                                                                                </select>
                                                                            </div>
                                                                        </div>
                                                                        <div class="item form-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataOutputMapping-options">topic <span
                                                                                    class="required">*</span>
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12">
                                                                                <input id="dataOutputMapping-topic"
                                                                                       class="form-control col-md-7 col-xs-12"
                                                                                       type="text" name="topic"
                                                                                       required="required">
                                                                            </div>
                                                                        </div>
                                                                        <div class="item form-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataInstance-name">dataSourceOutput <span
                                                                                    class="required">*</span>
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12">
                                                                                <table id="dataSourceOutputTable"
                                                                                       class="table table-hover jambo_table"
                                                                                       style="width: 100%">
                                                                                </table>
                                                                            </div>
                                                                        </div>
                                                                        <input id="dataOutputMapping-dataSourceOutputId"
                                                                               type="hidden"
                                                                               name="dataSourceOutputId">
                                                                        <input id="dataOutputMapping-dataInstanceId"
                                                                               type="hidden"
                                                                               name="dataInstanceId">
                                                                        <input id="dataOutputMapping-id"
                                                                               type="hidden"
                                                                               name="id">
                                                                        <div class="ln_solid"></div>
                                                                        <div class="form-group">
                                                                            <div class="col-md-offset-2 col-sm-offset-2 col-md-4 col-sm-4 col-xs-8">
                                                                                <button id="dataOutputMapping-submit"
                                                                                        type="button"
                                                                                        class="btn btn-success">保存
                                                                                </button>
                                                                            </div>
                                                                        </div>
                                                                    </form>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
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
            </div>
        </div>
        <!-- /page content -->

        <!-- footer content -->
        <footer>
            <div class="pull-right">
                Github <a href="">Dataflow</a>
            </div>
            <div class="clearfix"></div>
        </footer>
        <!-- /footer content -->
    </div>
</div>

<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/bootstrap/3.3.6/js/bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.js"></script>
<#--<script src="https://cdn.bootcss.com/bootstrap-progressbar/0.9.0/bootstrap-progressbar.min.js"></script>-->
<#--<script src="https://cdn.bootcss.com/bootstrap-daterangepicker/2.1.25/moment.min.js"></script>-->
<#--<script src="https://cdn.bootcss.com/bootstrap-daterangepicker/2.1.25/daterangepicker.min.js"></script>-->

<script src="https://cdn.bootcss.com/datatables/1.10.15/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.bootcss.com/datatables/1.10.15/js/dataTables.bootstrap.min.js"></script>
<script src="https://cdn.bootcss.com/jquery.form/4.2.1/jquery.form.min.js"></script>
<script src="https://cdn.bootcss.com/jquery.nicescroll/3.7.0/jquery.nicescroll.min.js"></script>
<script src="../javascript/jquery/jquery.smartWizard.min.js"></script>
<script src="../javascript/validator/validator.min.js"></script>
<!-- Custom Theme Scripts -->
<script src="../javascript/custom/dataInstance.js"></script>

</body>
</html>
