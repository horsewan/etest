package com.eningqu.common.constant;

/**
 *
 * @desc TODO  查询搜索条件 常量
 * @author     Yanghuangping
 * @date       2018/4/17 16:51
 * @version    1.0
 *
 **/
public class SearchParam {
    /*** 等于*/
    public static final String SEARCH_EQ="search_eq_";
    public static final String SEARCH_ISNULL="search_isnull_";
    public static final String SEARCH_ISNOTNULL="search_isnotnull_";
    /*** 左模糊*/
    public static final String SEARCH_LLIKE="search_llike_";
    /*** 右模糊*/
    public static final String SEARCH_RLIKE="search_rlike_";
    /**** 全模糊*/
    public static final String SEARCH_LIKE="search_like_";
    /**** 范围 */
    public static final String SEARCH_BETWEEN="search_between_";
    public static final String BETWEEN_START_WITH="_start";
    public static final String BETWEEN_END_WITH="_end";
}
