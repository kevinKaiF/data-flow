package com.github.dataflow.dubbo.model;

import java.io.Serializable;

/**
 * @author : kevin
 * @version : Ver 1.0
 * @description :
 * @date : 2017/6/2
 */
public class PageSet implements Serializable {
    private static final long serialVersionUID = -5629070201293836543L;
    /**
     * 页数
     */
    private Integer pageNumber;

    /**
     * 分页条数
     */
    private Integer pageSize;

    /**
     * 排序字段
     */
    private String sortColumns;

    public PageSet() {
        this.pageNumber = 0;
        this.pageSize = 20;
    }

    public PageSet(Integer pageNumber, Integer pageSize) {
        if (pageNumber == null || pageNumber < 0) {
            this.pageNumber = 0;
        } else {
            this.pageNumber = pageNumber;
        }

        if (pageSize == null || pageSize < 0) {
            this.pageSize = 20;
        } else {
            this.pageSize = pageSize;
        }
    }

    public Integer getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(Integer pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortColumns() {
        return sortColumns;
    }

    public void setSortColumns(String sortColumns) {
        this.sortColumns = sortColumns;
    }

    @Override
    public String toString() {
        return "PageSet{" +
               "pageNumber=" + pageNumber +
               ", pageSize=" + pageSize +
               ", sortColumns='" + sortColumns + '\'' +
               '}';
    }
}
