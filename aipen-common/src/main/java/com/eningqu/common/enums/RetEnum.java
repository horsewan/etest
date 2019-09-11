package com.eningqu.common.enums;
/**
 *
 * @desc TODO  
 * @author     Yanghuangping
 * @since      2018/5/26 15:21
 * @version    1.0
 *
 **/

public enum  RetEnum {
    /** 成功 */
    SUCCESS(200),
    /** 失败 */
    FAIL(400),
    /** 未认证（签名错误）*/
    UNAUTHORIZED(401),
    /** 未登录 */
    UN_LOGIN(4401),
    /** 未授权，拒绝访问 */
    UN_AUTH(4403),
    /** 服务器内部错误 */
    SERVER_ERROR(500);

    public int code;

    RetEnum(int code) {
        this.code = code;
    }
}
