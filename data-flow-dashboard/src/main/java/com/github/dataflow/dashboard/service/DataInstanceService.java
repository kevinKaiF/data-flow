package com.github.dataflow.dashboard.service;

import com.github.dataflow.common.utils.Closer;
import com.github.dataflow.dashboard.exception.DataFlowException;
import com.github.dataflow.dashboard.utils.Constants;
import com.github.dataflow.dashboard.utils.DataSourceUtil;
import com.github.dataflow.dubbo.common.enums.DataInstanceStatus;
import com.github.dataflow.dubbo.model.*;
import com.github.dataflow.dubbo.service.DubboDataInstanceService;
import com.github.dataflow.dubbo.service.DubboDataOutputMappingService;
import com.github.dataflow.dubbo.service.DubboDataTableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/4
 */
@Service
public class DataInstanceService {
    private static final Logger logger = LoggerFactory.getLogger(DataInstanceService.class);

    @Autowired
    private DubboDataInstanceService dubboDataInstanceService;

    @Autowired
    private DubboDataTableService dubboDataTableService;

    @Autowired
    private DubboDataOutputMappingService dubboDataOutputMappingService;

    public Long insert(DataInstance dataInstance) {
        if (dataInstance.getId() != null) {
            return update(dataInstance);
        }

        dataInstance.setName(createName(dataInstance.getTag()));
        dataInstance.setCreateTime(new Date());
        dataInstance.setStatus(DataInstanceStatus.CREATING.getStatus());
        ServiceResult<Long> serviceResult = dubboDataInstanceService.insert(dataInstance);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.insert",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return serviceResult.getResult();
    }

    private String createName(String tag) {
        return createNamePrefix() + "-" + tag;
    }

    private String updateName(String name, String tag) {
        return name.substring(0, name.indexOf("-") + 1) + tag;
    }

    private String createNamePrefix() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public Long update(DataInstance dataInstance) {
        dataInstance.setName(updateName(dataInstance.getName(), dataInstance.getTag()));
        dataInstance.setUpdateTime(new Date());
        ServiceResult<Integer> serviceResult = dubboDataInstanceService.update(dataInstance);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.update",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }

        return dataInstance.getId();
    }

    public void delete(Long[] ids) {
        ServiceResult<Integer> serviceResult = dubboDataInstanceService.deleteBatch(Arrays.asList(ids));
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.delete",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }

    public DataInstance getById(Long id) {
        ServiceResult<DataInstance> serviceResult = dubboDataInstanceService.getById(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.getById",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        DataInstance dataInstance = serviceResult.getResult();
        if (dataInstance == null) {
            logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.getById时未获取到结果");
        }

        return dataInstance;
    }

    public DataInstance getByIdFully(Long id) {
        ServiceResult<DataInstance> serviceResult = dubboDataInstanceService.getByIdFully(id);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.getByIdFully",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        DataInstance dataInstance = serviceResult.getResult();
        if (dataInstance == null) {
            logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.getByIdFully时未获取到结果");
        }

        return dataInstance;
    }

    public Map findByCondition(DataInstance dataInstance, Integer start, Integer length) {
        if (dataInstance == null) {
            dataInstance = new DataInstance();
        }

        ServiceResult<Long> longServiceResult = dubboDataInstanceService.countByCondition(dataInstance);
        if (!longServiceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.findByCondition",
                         "longServiceResult", longServiceResult.getErrorMessage());
            throw new DataFlowException();
        }
        Long total = longServiceResult.getResult();
        Map map = new HashMap();
        if (total == null || total == 0) {
            logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.findByCondition时未获取到结果");
            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, 0);
            map.put(Constants.DATA, Collections.EMPTY_LIST);
            return map;
        } else {
            PageSet pageSet = new PageSet(start, length);
            ServiceResult<List<DataInstance>> listServiceResult = dubboDataInstanceService.findByCondition(dataInstance, pageSet);
            if (!listServiceResult.isSuccess()) {
                logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.findByCondition",
                             "listServiceResult", listServiceResult.getErrorMessage());
                throw new DataFlowException();
            }
            List<DataInstance> dataInstances = listServiceResult.getResult();
            if (CollectionUtils.isEmpty(dataInstances)) {
                logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.findByCondition时未获取到结果");
                dataInstances = Collections.EMPTY_LIST;
            }
            map.put(Constants.RECORDS_TOTAL, length);
            map.put(Constants.RECORDS_FILTERED, total);
            map.put(Constants.DATA, dataInstances);
            return map;
        }
    }

    public Map getTableDetail(Long id, String schemaName, String tableName) {
        DataTable condition = new DataTable();
        condition.setDataInstanceId(id);
        condition.setSchemaName(schemaName);
        condition.setTableName(tableName);
        ServiceResult<List<DataTable>> listServiceResult = dubboDataTableService.findByCondition(condition, new PageSet());
        if (!listServiceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.getTableDetail",
                         "listServiceResult", listServiceResult.getErrorMessage());
            throw new DataFlowException();
        }
        List<DataTable> dataTableList = listServiceResult.getResult();
        Map<String, List<String>> columnListMap = getColumn(id, schemaName, tableName);
        if (CollectionUtils.isEmpty(dataTableList)) {
            logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.getTableDetail时未获取到结果");
            Map<String, Object> map = new HashMap<>();
            map.put("tableName", condition.getTableName());
            map.put("schemaName", condition.getSchemaName());
            map.put("dataInstaceId", condition.getDataInstanceId());
            map.put("primaryKeys", columnListMap.get("primaryKeys"));
            map.put("columns", columnListMap.get("columns"));
            return map;
        } else {
            DataTable dataTable = dataTableList.get(0);
            Map<String, Object> map = new HashMap<>();
            map.put("id", dataTable.getId());
            map.put("tableName", dataTable.getTableName());
            map.put("schemaName", dataTable.getSchemaName());
            map.put("dataInstaceId", dataTable.getDataInstanceId());
            map.put("primaryKeys", columnListMap.get("primaryKeys"));
            map.put("columns", columnListMap.get("columns"));
            return map;
        }
    }

    public void updateStatus(Long id, Integer status) {
        ServiceResult<Void> serviceResult = dubboDataInstanceService.updateStatusById(id, status);
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.updateStatus",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
    }

    /**
     * 获取该数据源下的所有schemaName
     *
     * @param id     dataInstance的id
     * @param filter 是否过滤已映射的schema
     * @return
     */
    public List<String> getSchema(Long id, Boolean filter) {
        if (filter) {
            List<String> existedSchemaNames = getDataOutputMappings(id);
            // 如果包含全部schema，直接return
            if (existedSchemaNames.contains("*")) {
                return new ArrayList<>();
            }

            return filterExistedSchemaNames(id, existedSchemaNames);
        } else {
            return filterExistedSchemaNames(id, null);
        }

    }

    private List<String> filterExistedSchemaNames(Long id, List<String> existedSchemaNames) {
        DataInstance dataInstance = getById(id);
        List<String> schemaNameList = new ArrayList<>();
        if (dataInstance != null) {
            try {
                Connection connection = DataSourceUtil.getConnection(dataInstance);
                schemaNameList = getSchemaNameList(connection);
                if (!CollectionUtils.isEmpty(existedSchemaNames)) {
                    // 滤掉已经映射的schema
                    schemaNameList.removeAll(existedSchemaNames);
                }
            } catch (SQLException e) {
                throw new DataFlowException(e);
            }
        }

        return schemaNameList;
    }

    /**
     * 获取已经映射的schemaName
     * <p>
     * 映射原则是多个schemaName可以映射同一个DataSourceOutput，
     * 但是一个schemaName只能映射一个DataSourceOutput
     *
     * @param id
     * @return
     */
    private List<String> getDataOutputMappings(Long id) {
        DataOutputMapping condition = new DataOutputMapping();
        condition.setDataInstanceId(id);
        ServiceResult<List<DataOutputMapping>> serviceResult = dubboDataOutputMappingService.findByCondition(condition, new PageSet());
        if (!serviceResult.isSuccess()) {
            logger.error("{}调用{}时发生未知异常,error Message:{}", "com.github.dataflow.dashboard.service.DataInstanceService.getSchema",
                         "serviceResult", serviceResult.getErrorMessage());
            throw new DataFlowException();
        }
        List<DataOutputMapping> dataOutputMappingList = serviceResult.getResult();
        if (CollectionUtils.isEmpty(dataOutputMappingList)) {
            logger.warn("com.github.dataflow.dashboard.service.DataInstanceService.getSchema时未获取到结果");
            return new ArrayList<>();
        } else {
            List<String> schemaNameListToUse = new ArrayList<>();
            for (DataOutputMapping dataOutputMapping : dataOutputMappingList) {
                schemaNameListToUse.add(dataOutputMapping.getSchemaName());
            }

            return schemaNameListToUse;
        }
    }

    public List<String> getTable(Long id, String schema) {
        DataInstance dataInstance = getById(id);
        List<String> tableNameList = new ArrayList<>();
        if (dataInstance != null) {
            try {
                Connection connection = DataSourceUtil.getConnection(dataInstance);
                tableNameList = getTableNames(connection, schema);
            } catch (SQLException e) {
                throw new DataFlowException(e);
            }
        }

        return tableNameList;
    }

    private List<String> getTableNames(Connection connection, String schema) throws SQLException {
        ResultSet resultSet = null;
        List<String> tableNameList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getTables(schema, null, "%", new String[]{"TABLE"});
            while (resultSet.next()) {
                tableNameList.add(resultSet.getString("TABLE_NAME"));
            }
        } finally {
            Closer.closeQuietly(resultSet);
        }

        return tableNameList;
    }

    private Map<String, List<String>> getColumn(Long id, String schema, String tableName) {
        DataInstance dataInstance = getById(id);
        Map<String, List<String>> columnListMap = new HashMap<>();
        if (dataInstance != null) {
            try {
                Connection connection = DataSourceUtil.getConnection(dataInstance);
                columnListMap = getColumnList(connection, schema, tableName);
            } catch (SQLException e) {
                throw new DataFlowException(e);
            }
        }

        return columnListMap;
    }

    private Map<String, List<String>> getColumnList(Connection connection, String catalog, String tableName) throws SQLException {
        ResultSet resultSet = null;
        ResultSet primaryKeysSet = null;
        Map<String, List<String>> columnListMap = new HashMap<>();
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            resultSet = metaData.getColumns(catalog, null, tableName, null);
            List<String> columns = new ArrayList<>();
            while (resultSet.next()) {
                String columnName = resultSet.getString("COLUMN_NAME");
                columns.add(columnName);
            }
            Collections.sort(columns);
            columnListMap.put("columns", columns);

            primaryKeysSet = metaData.getPrimaryKeys(catalog, catalog, tableName);
            List<String> primaryKeys = new ArrayList<>();
            while (primaryKeysSet.next()) {
                String columnName = primaryKeysSet.getString("COLUMN_NAME");
                primaryKeys.add(columnName);
            }
            columnListMap.put("primaryKeys", primaryKeys);
        } finally {
            Closer.closeQuietly(resultSet);
            Closer.closeQuietly(primaryKeysSet);
        }

        return columnListMap;
    }

    private List<String> getSchemaNameList(Connection connection) throws SQLException {
        ResultSet resultSet = null;
        List<String> schemaList = new ArrayList<>();
        try {
            resultSet = connection.getMetaData().getCatalogs();
            while (resultSet.next()) {
                schemaList.add(resultSet.getString("TABLE_CAT"));
            }
        } finally {
            Closer.closeQuietly(resultSet);
        }
        return schemaList;
    }


    public Map getDataInstanceByNodePath(String nodePath) {
        DataInstance dataInstance = new DataInstance();
        dataInstance.setNodePath(nodePath);
        return findByCondition(dataInstance, 0, Integer.MAX_VALUE);
    }
}
