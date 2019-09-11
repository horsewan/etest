package com.eningqu.common.base.vo;


import java.io.Serializable;
import java.util.List;

/**
 *
 * @desc TODO  用户信息实体类
 * @author     Yanghuangping
 * @date       2018/4/18 19:36
 * @version    1.0
 *
 **/
public class InfoResultTable<T> implements Serializable {

    /** 页码数 */
    private int page;
    /** 页数 */
    private int limit;
    /** 总记录数 */
    private int count;
    /** 数据 */
    private List<T> data;

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
}
