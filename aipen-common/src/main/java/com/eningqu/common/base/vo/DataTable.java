package com.eningqu.common.base.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @desc TODO  列表数据返回模型
 * @author     Yanghuangping
 * @date       2018/4/18 10:03
 * @version    1.0
 *
 **/
public class DataTable<T> implements Serializable{
    /** 成功 状态码 */
    private int code = 1;
    /** 消息提示 */
    private String msg = "数据请求成功";
    /** 页码数 */
    private int page;
    /** 页数 */
    private int limit;
    /** 总记录数 */
    private int count;
    /** 数据 */
    private List<T> data;
    /** 查询参数 */
    private Map<String, Object> searchParams;
    /** 查询字段 */
    private String[] fields;
    /** 排序 **/
    private Map<String, Boolean> sort = new HashMap<>();

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public Map<String, Object> getSearchParams() {
        return searchParams;
    }

    public void setSearchParams(Map<String, Object> searchParams) {
        this.searchParams = searchParams;
    }

    public String[] getFields() {
        return fields;
    }

    public void setFields(String[] fields) {
        this.fields = fields;
    }

    public Map<String, Boolean> getSort() {
        return sort;
    }

    public void sortAsc(String field) {
        this.sort.put(field, Boolean.TRUE);
    }

    public void sortDesc(String field) {
        this.sort.put(field, Boolean.FALSE);
    }

}
