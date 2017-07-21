<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <!-- Meta, title, CSS, favicons, etc. -->
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" href="../images/favicon.ico" type="image/x-icon" />
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
                            <li class="active"><a href="../dataInstance/"><i class="fa fa-random"></i> 数据实例 <span
                                    class="fa fa-chevron-right"></span></a>
                            <#--<ul class="nav child_menu" style="display: block">-->
                            <#--<li class="current-page"><a href="../dataInstance/producer">生产者</a></li>-->
                            <#--<li><a href="../dataInstance/consumer">消费者</a></li>-->
                            <#--</ul>-->
                            </li>
                            <li><a href="../dataSourceOutput/"><i class="fa fa-database"></i>
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
        <div class="right_col" role="main" style="relative">
            <div class="row">
                <div class="col-md-12 col-sm-12 col-xs-12">
                    <div class="row">
                        <div class="x_panel">
                            <div class="x_title" style="border-bottom-width: 1px; padding: 0;" id="search_panel_header">
                                <h2>实例搜索</h2>
                                <ul class="nav navbar-right panel_toolbox">
                                    <li><a class="collapse-link"><i class="fa fa-chevron-down"></i></a>
                                    </li>
                                </ul>
                                <div class="clearfix"></div>
                            </div>
                            <div class="x_content" style="display: none">
                                <form id="dataInstance-searchForm"
                                      class="form-horizontal form-label-left">
                                    <div class="item form-group">
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-tag">标签
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataInstance-tag" name="tag"
                                                   class="form-control col-md-7 col-xs-12">
                                        </div>
                                        <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                               for="dataInstance-name">节点路径
                                        </label>
                                        <div class="col-md-4 col-sm-4 col-xs-12">
                                            <input type="text" id="dataInstance-name" name="nodePath"
                                                   class="form-control col-md-7 col-xs-12">
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
                        </div>
                    </div>

                    <div class="row">
                        <div class="x_panel">
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
                                <div class="modal-dialog modal-lg" style="width: 65%">
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
                                                                      步骤 1<br/>
                                                                      <small>输入</small>
                                                                  </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#step-2">
                                                            <span class="step_no">2</span>
                                                    <span class="step_descr">
                                                          步骤 2<br/>
                                                          <small>过滤</small>
                                                      </span>
                                                        </a>
                                                    </li>
                                                    <li>
                                                        <a href="#step-3">
                                                            <span class="step_no">3</span>
                                                    <span class="step_descr">
                                                                      步骤 3<br/>
                                                        <small>输出</small>
                                                    </span>
                                                        </a>
                                                    </li>
                                                </ul>
                                                <div id="step-1">
                                                    <form id="dataInstance-form"
                                                          class="form-horizontal form-label-left">
                                                        <div class="item form-group">
                                                            <label for=""
                                                                   class="control-label col-md-2 col-sm-2 col-xs-12">模式 <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-8 col-sm-8 col-xs-12">
                                                                <select class="form-control col-md-7 col-xs-12"
                                                                        name="producerOrConsumer" required="required">
                                                                    <option value="0">生产者</option>
                                                                    <option value="1">消费者</option>
                                                                </select>
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                   for="dataInstance-tag">标签 <span
                                                                    class="required">*</span>
                                                            </label>
                                                            <div class="col-md-8 col-sm-8 col-xs-12">
                                                                <input type="text" id="dataInstance-tag" name="tag"
                                                                       required="required"
                                                                       class="form-control col-md-7 col-xs-12" data-validate-length-range="1,20">
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for=""
                                                                   class="control-label col-md-2 col-sm-2 col-xs-12">类型 <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-8 col-sm-8 col-xs-12">
                                                                <select id="dataInstance-type" class="form-control col-md-7 col-xs-12"
                                                                        name="type" required="required">
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
                                                            <label for="dataInstance-options"
                                                                   class="control-label col-md-2 col-sm-2 col-xs-12">参数 <span
                                                                    class="required">*</span></label>
                                                            <div class="col-md-8 col-sm-8 col-xs-12" style="position: relative">
                                                        <textarea id="dataInstance-options"
                                                                  class="form-control col-md-7 col-xs-12"
                                                                  rows="9" required="required"
                                                                  name="options"></textarea>
                                                                <span id="dataInstance-options-doc" class="doc">doc</span>
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <label for="dataInstance-transformScript"
                                                                   class="control-label col-md-2 col-sm-2 col-xs-12">转换脚本 </label>
                                                            <div class="col-md-8 col-sm-8 col-xs-12" style="position: relative">
                                                        <textarea id="dataInstance-transformScript"
                                                                  class="form-control col-md-7 col-xs-12"
                                                                  rows="7"
                                                                  name="transformScript"></textarea>
                                                                <span id="dataInstance-transformScript-doc" class="doc">doc</span>
                                                            </div>
                                                        </div>
                                                        <div class="item form-group">
                                                            <div class="col-md-6 col-sm-6 col-xs-12">
                                                                <input id="dataInstance-id"
                                                                       class="form-control col-md-7 col-xs-12"
                                                                       type="hidden" name="id">
                                                            </div>
                                                        </div>
                                                        <input type="hidden" name="name" value="-1" id="dataInstance-name">
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
                                                <div id="step-3" style="height: 550px">
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

                                                                        <div class="item form-group" id="dataOutputMapping-schemaName-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataOutputMapping-schemaName">库名 <span
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
                                                                                   for="dataOutputMapping-options">配置
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12" style="position: relative;">
                                                                                 <textarea id="dataOutputMapping-options"
                                                                                           class="form-control col-md-7 col-xs-12"
                                                                                           rows="5"
                                                                                           name="options"></textarea>
                                                                                <span id="dataOutputMapping-options-doc" class="doc">doc</span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="item form-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataOutputMapping-transformScript">转换脚本
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12" style="position: relative">
                                                                                 <textarea id="dataOutputMapping-transformScript"
                                                                                           class="form-control col-md-7 col-xs-12"
                                                                                           rows="5"
                                                                                           name="transformScript"></textarea>
                                                                                <span id="dataOutputMapping-transformScript-doc" class="doc">doc</span>
                                                                            </div>
                                                                        </div>
                                                                        <div class="item form-group">
                                                                            <label class="control-label col-md-2 col-sm-2 col-xs-12"
                                                                                   for="dataInstance-name">输出数据源 <span
                                                                                    class="required">*</span>
                                                                            </label>
                                                                            <div class="col-md-9 col-sm-9 col-xs-12">
                                                                                <table id="dataSourceOutputTable"
                                                                                       class="table table-hover jambo_table"
                                                                                       style="width: 100%">
                                                                                </table>
                                                                            </div>
                                                                        </div>
                                                                        <input id="dataOutputMapping-dataSourceOutputType"
                                                                               type="hidden">
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

            <#--dataInstance options-->
                <div id="dataInstance-options-modal" class="modal fade bs-example-modal-lg" tabindex="-1"
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
                                    <li role="presentation" class=""><a href="#tab_content5" role="tab" id="profile-tab5" data-toggle="tab" aria-expanded="false">Kafka</a>
                                    </li>
                                    <li role="presentation" class=""><a href="#tab_content6" role="tab" id="profile-tab6" data-toggle="tab" aria-expanded="false">ActiveMQ</a>
                                    </li>
                                </ul>
                                <div id="myTabContent" class="tab-content">
                                    <div role="tabpanel" class="tab-pane fade active in" id="tab_content1" aria-labelledby="profile-tab">
                                        <textarea style="width: 100%;" rows="12" readonly>{
    "username":"",
    "password":"",
    "host":"",
    "port":"",
    "jdbcUrl":"",
    "slaveId":"",
    "whiteFilter":"white\\..*",
    "blackFilter":"black\\..*"
}</textarea>
                                    </div>
                                    <div role="tabpanel" class="tab-pane fade" id="tab_content5" aria-labelledby="profile-tab5">
                                        <textarea style="width: 100%;" rows="12" readonly>{
    "bootstrap.servers":"",
    "topic":"",
    "group.id":""
} </textarea>
                                    </div>
                                    <div role="tabpanel" class="tab-pane fade" id="tab_content6" aria-labelledby="profile-tab6">
                                        <textarea style="width: 100%;" rows="12" readonly>{
    "type":0,
    "topic":"",
    "queue":"",
    "brokeUrl":""
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

                <div id="dataInstance-transformScript-modal" class="modal fade bs-example-modal-lg" tabindex="-1"
                     role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md">
                        <div class="modal-content">

                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span
                                        aria-hidden="true">×</span>
                                </button>
                                <h4 class="modal-title" id="myModalLabel">脚本</h4>
                            </div>
                            <div class="modal-body">
                                <textarea style="width: 100%;" rows="15" readonly>
    /**
     * 该转换方法主要用于数据库库名，表名，更改以及字段的添加、删除、修改
     * 注意：如果你使用了第三方的jar包请import需要的类名，并在maven依赖中
     * 添加相应的依赖
     *
     * RowMetaData的字段
     * @param {String}           tableName       表名
     * @param {String}           schemaName      库名
     * @param {enum}             eventType       DDL类型
     * @param {List<ColumnMeta>} beforeColumns   变化前的字段
     * @param {List<ColumnMeta>} afterColumns    变化后的字段
     *
     * EventType枚举
     * @param {enum}             INSERT          插入
     * @param {enum}             UPDATE          更新
     * @param {enum}             DELETE          删除
     *
     * ColumnMeta的字段
     * @param {String}           columnName      字段名称
     * @param {int}              jdbcType        jdbc类型
     * @param {boolean}          isKey           是否是主键
     * @param {String}           value           字段值
     *
     * @see cn.bidlink.dataflow.common.model.RowMetaData
     */
    public List<RowMetaData> transform(List<RowMetaData> rowMetaDataList) {
        // 自定义的处理转换
        for (RowMetaData rowMetaData : rowMetaDatas) {
            if ("testTable".equals(rowMetaData.getTableName()) && "testSchema".equals(rowMetaData.getSchemaName())) {
                // 更换表名
                rowMetaData.setTableName("newTestTable");
                // 添加字段
                if (rowMetaData.getEventType().equals(EventType.INSERT)) {
                    rowMetaData.getAfterColumns().add(new ColumnMeta("test", 4, false, "10"));
                }
            }

            if ("testSchema".equals(rowMetaData.getSchemaName())) {
                // 更换库名
                rowMetaData.setSchemaName("newTestSchema");
            }
        }
        return rowMetaDataList;
    }
                                </textarea>
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

            <#--dataOutputMapping-->
                <div id="dataOutputMapping-options-modal" class="modal fade bs-example-modal-lg" tabindex="-1"
                     role="dialog" aria-hidden="true">
                    <div class="modal-dialog modal-md">
                        <div class="modal-content">

                            <div class="modal-header">
                                <button type="button" class="close" data-dismiss="modal"><span
                                        aria-hidden="true">×</span>
                                </button>
                                <h4 class="modal-title" id="myModalLabel1">模板</h4>
                            </div>
                            <div class="modal-body">
                                <ul id="myTab1" class="nav nav-tabs bar_tabs" role="tablist">
                                    <li role="presentation" class="active"><a href="#tab_content11" role="tab" id="profile-tab11" data-toggle="tab" aria-expanded="false">Kafka</a>
                                    </li>
                                    <li role="presentation" class=""><a href="#tab_content21" role="tab" id="profile-tab21" data-toggle="tab" aria-expanded="false">ActiveMq</a>
                                    </li>
                                </ul>
                                <div id="myTabContent1" class="tab-content">
                                    <div role="tabpanel" class="tab-pane fade active in" id="tab_content11" aria-labelledby="profile-tab11">
                                        <textarea style="width: 100%;" rows="12" readonly>{
    "topic":""
}</textarea>
                                    </div>
                                    <div role="tabpanel" class="tab-pane fade" id="tab_content21" aria-labelledby="profile-tab21">
                                        <textarea style="width: 100%;" rows="12" readonly>{
    "type":0=queue,1=topic,
    "topic":"",
    "queue":""
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

            <div id="dataOutputMapping-transformScript-modal" class="modal fade bs-example-modal-lg" tabindex="-1"
                 role="dialog" aria-hidden="true">
                <div class="modal-dialog modal-md">
                    <div class="modal-content">

                        <div class="modal-header">
                            <button type="button" class="close" data-dismiss="modal"><span
                                    aria-hidden="true">×</span>
                            </button>
                            <h4 class="modal-title" id="myModalLabel">脚本</h4>
                        </div>
                        <div class="modal-body">
                                <textarea style="width: 100%;" rows="15" readonly>
    /**
     * 该转换方法主要用于将rowMetaDataList转换为JSON字符串的一些处理
     * 注意：如果你使用了第三方的jar包请import需要的类名，并在maven依
     * 赖中添加相应的依赖
     *
     * RowMetaData的字段
     * @param {String}           tableName       表名
     * @param {String}           schemaName      库名
     * @param {enum}             eventType       DDL类型
     * @param {List<ColumnMeta>} beforeColumns   变化前的字段
     * @param {List<ColumnMeta>} afterColumns    变化后的字段
     *
     * EventType枚举
     * @param {enum}             INSERT          插入
     * @param {enum}             UPDATE          更新
     * @param {enum}             DELETE          删除
     *
     * ColumnMeta的字段
     * @param {String}           columnName      字段名称
     * @param {int}              jdbcType        jdbc类型
     * @param {boolean}          isKey           是否是主键
     * @param {String}           value           字段值
     *
     * @see cn.bidlink.dataflow.common.model.RowMetaData
     */

    import com.alibaba.fastjson.JSON;

    public String transform(List<RowMetaData> rowMetaDataList) {
        // 自定义的处理转换
        return JSON.toJSONString(rowMetaDataList);
    }
                                </textarea>
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

            <div id="loading" class="text-center" style="display:none;z-index:1000;position: absolute;left: 0;bottom: 0;right: 0;top: 0;background: #000;opacity: 0.5">
                <span class="glyphicon glyphicon-refresh glyphicon-refresh-animate" style="font-size: 4em;color: #13bebf;margin: 20%;"></span>
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
<script src="../javascript/custom/global.js"></script>
<script src="../javascript/custom/dataInstance.js"></script>

</body>
</html>
