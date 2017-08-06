<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="../images/favicon.ico" type="image/x-icon" />
    <title>节点监控</title>
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
                            <li><a href="../dataSourceOutput/"><i class="fa fa-database"></i>
                                输出数据源 <span class="fa fa-chevron-right"></span></a>
                            </li>
                            <li class="active"><a href="../dataNode/"><i class="fa fa-eye"></i> 节点监控 <span
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
                            <span class="section">节点列表</span>
                            <div class="col-md-6">
                                <h4>生产者</h4>
                                <div id="producerList" class="col-md-12"></div>
                            </div>
                            <div class="col-md-6">
                                <h4>消费者</h4>
                                <div id="consumerList" class="col-md-12"></div>
                            </div>
                        </div>
                    </div>
                    <div class="row" id="dataInstance" style="display: none">
                        <div class="x_panel">
                            <span class="section" id="nodeHeader"></span>
                            <table id="dataInstanceTable" class="table table-hover jambo_table" style="width: 100%">
                            </table>
                        </div>
                    </div>
                    <div class="row" id="logList" style="display: none">
                            <div class="col-md-4" style="padding-left: 0px">
                                <div class="x_panel">
                                    <span class="section">日志列表</span>
                                    <table id="dataLogTable" class="table table-hover jambo_table" style="width: 100%">
                                    </table>
                                </div>
                            </div>
                            <div class="col-md-8" style="padding-right: 0px">
                                <div class="x_panel">
                                    <span class="section">日志详情</span>
                                    <div id="logDetail"></div>
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
        <!-- /page content -->

        <!-- footer content -->
        <footer>
            <div class="pull-right github-ico">
                <a href="https://github.com/kevinKaiF/data-flow" target="_blank">Github Dataflow</a>
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
<script src="https://cdn.bootcss.com/jquery.nicescroll/3.7.0/jquery.nicescroll.min.js"></script>
<script src="https://cdn.bootcss.com/easy-pie-chart/2.1.6/jquery.easypiechart.min.js"></script>
<script src="../javascript/validator/validator.min.js"></script>
<script src="../javascript/custom/global.js"></script>
<script src="../javascript/custom/dataNode.js"></script>

</body>
</html>
