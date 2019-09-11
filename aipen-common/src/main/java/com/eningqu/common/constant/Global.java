package com.eningqu.common.constant;

/**
 *
 * @desc TODO  全局常量
 * @author     Yanghuangping
 * @date       2018/4/17 16:51
 * @version    1.0
 *
 **/
public class Global {

    /** -------------------- 后台 ------------------------- */

    /** 管理员ID */
    public static final Long ADMIN_ID = 1L ;
    /** 顶层父级节点ID */
    public static final Long TOP_PID_ID = 0L ;
    /** 验证码 */
    public static final String VERIFY_CODE = "VERIFY_CODE";
    /** 权限分隔符 */
    public static final String PERM_DELIMITER = ";";
    /** 权限码分隔符 */
    public static final String REDIS_PERMS_PREFIX = "redis:perms:";
    /** 菜单分隔符 */
    public static final String REDIS_MENUS_PREFIX = "redis:menus:";
    /** redis默认过期时间 （秒） */
    public static final int REDIS_EXPIRE_TIME = 1800;
    /*** 初始密码 */
    public static final String DEFAULT_PWD = "123456789";
    /** cron 表达式*/
    public static final String CRON_PATTERN = "(((^([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\\\* ))((([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\\\* ))((([0-9]|[01][0-9]|2[0-3])(\\\\,|\\\\-|\\\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\\\* ))((([0-9]|[0-2][0-9]|3[01])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\\\? )|(\\\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\\\,|\\\\-|\\\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\\\* ))(([1-7](\\\\,|\\\\-|\\\\/){1}[1-7])|([1-7])|(\\\\?)|(\\\\*)|(([1-7]L)|([1-7]\\\\#[1-4]))))|(((^([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|^([0-9]|[0-5][0-9]) |^(\\\\* ))((([0-9]|[0-5][0-9])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-5][0-9]) )|([0-9]|[0-5][0-9]) |(\\\\* ))((([0-9]|[01][0-9]|2[0-3])(\\\\,|\\\\-|\\\\/){1}([0-9]|[01][0-9]|2[0-3]) )|([0-9]|[01][0-9]|2[0-3]) |(\\\\* ))((([0-9]|[0-2][0-9]|3[01])(\\\\,|\\\\-|\\\\/){1}([0-9]|[0-2][0-9]|3[01]) )|(([0-9]|[0-2][0-9]|3[01]) )|(\\\\? )|(\\\\* )|(([1-9]|[0-2][0-9]|3[01])L )|([1-7]W )|(LW )|([1-7]\\\\#[1-4] ))((([1-9]|0[1-9]|1[0-2])(\\\\,|\\\\-|\\\\/){1}([1-9]|0[1-9]|1[0-2]) )|([1-9]|0[1-9]|1[0-2]) |(\\\\* ))(([1-7](\\\\,|\\\\-|\\\\/){1}[1-7] )|([1-7] )|(\\\\? )|(\\\\* )|(([1-7]L )|([1-7]\\\\#[1-4]) ))((19[789][0-9]|20[0-9][0-9])\\\\-(19[789][0-9]|20[0-9][0-9])))";

    /** -------------------- API接口 ------------------------- */

    /** 授权TOKEN */
    public final static String AUTH_TOKEN = "auth_token";
    /** SESSION 过期时间 (秒）。1814400：21天 */
    public final static int SSESSION_EXPIRE_TIME = 1814400;
    /** TOKEN 前缀 */
    public final static String REDIS_TOKEN_PREFIX = "api:token:";
    /** 短信 前缀 */
    public final static String REDIS_SMS_PREFIX = "api:sms:";
    /** 短信验证码 过期时间 */
    public final static int SMS_EXPIRE_TIME = 300;
    /** 文件上传根路经 */
    public static String FILE_UPLOAD_ROOT;
    /** 文件后缀名 */
    public final static String FILE_SUFFIX = ".zip";
    /** 用户信息 key */
    public final static String API_LOGIN_INFO = "api:login:";
    /** 设备使用次数 默认-1 表示可以使用无限次 */
    public final static int DEVICE_USE_TIMES = -1;
}