package com.eningqu.common.kit;

import com.eningqu.common.constant.Setting;

import java.io.*;
import java.security.SecureRandom;
import java.util.*;

/**
 *
 * @desc TODO  密码加密
 * @author     Yanghuangping
 * @date       2018/4/25 17:54
 * @version    1.0
 *
 **/
public class PasswordKit {

    private final static SecureRandom random = new SecureRandom();

    public static byte[] createRandomSalt(int numBytes){
        byte[] bytes = new byte[numBytes];
        random.nextBytes(bytes);
        return bytes;
    }

    public static String entrypt(String password){
        byte[] salt = createRandomSalt(Setting.SALT_SIZE);
        byte[] hashPassword = DigestsKit.sha1(password.getBytes(), salt, Setting.HASH_INTERATIONS);
        StringBuilder sb = new StringBuilder();
        sb.append(HexKit.encodeHexStr(salt)).append(HexKit.encodeHexStr(hashPassword));
        return sb.toString();
    }

    public static String entrypt(String password, byte[] salt){
        byte[] hashPassword = DigestsKit.sha1(password.getBytes(), salt, Setting.HASH_INTERATIONS);
        StringBuilder sb = new StringBuilder();
        sb.append(HexKit.encodeHexStr(salt)).append(HexKit.encodeHexStr(hashPassword));
        return sb.toString();
    }

    public static void append(File aFile, String content) {
        try {
            PrintStream p = new PrintStream(new BufferedOutputStream(new FileOutputStream(aFile, true)));
            p.println(content);
            p.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(aFile);
        }
    }

    public static String getRandomJianHan(int len) {
        String ret = "";
        for (int i = 0; i < len; i++) {
            String str = null;
            int hightPos, lowPos; // 定义高低位
            Random random = new Random();
            hightPos = (176 + Math.abs(random.nextInt(39))); // 获取高位值
            lowPos = (161 + Math.abs(random.nextInt(93))); // 获取低位值
            byte[] b = new byte[2];
            b[0] = (new Integer(hightPos).byteValue());
            b[1] = (new Integer(lowPos).byteValue());
            try {
                str = new String(b, "GBK"); // 转成中文
            } catch (UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
            ret += str;
        }
        return ret;
    }

    public static void main(String[] args) {

        PasswordKit passwordKit = new PasswordKit();
        String resString = "<xml><bank_type><![CDATA[DEBIT_CARD]]></bank_type>\n" +
                "<buyer_logon_id><![CDATA[mil***@yahoo.com]]></buyer_logon_id>\n" +
                "<buyer_user_id><![CDATA[2088702639063572]]></buyer_user_id>\n" +
                "<charset><![CDATA[UTF-8]]></charset>\n" +
                "<coupon_fee><![CDATA[0]]></coupon_fee>\n" +
                "<device_info><![CDATA[QR_ORDER]]></device_info>\n" +
                "<fee_type><![CDATA[CNY]]></fee_type>\n" +
                "<fund_bill_list><![CDATA[[{\"amount\":\"1.00\",\"fundChannel\":\"BANKCARD\",\"fundType\":\"DEBIT_CARD\"}]]]></fund_bill_list>\n" +
                "<gmt_create><![CDATA[20190829202641]]></gmt_create>\n" +
                "<invoice_amount><![CDATA[100]]></invoice_amount>\n" +
                "<mch_id><![CDATA[QRA584273722KLD]]></mch_id>\n" +
                "<mdiscount><![CDATA[0]]></mdiscount>\n" +
                "<nonce_str><![CDATA[1567081609623]]></nonce_str>\n" +
                "<openid><![CDATA[2088702639063572]]></openid>\n" +
                "<out_trade_no><![CDATA[67050895879471106]]></out_trade_no>\n" +
                "<out_transaction_id><![CDATA[2019082922001463570597784006]]></out_transaction_id>\n" +
                "<pay_result><![CDATA[0]]></pay_result>\n" +
                "<result_code><![CDATA[0]]></result_code>\n" +
                "<sign><![CDATA[23DAFA856A390395B1D5A33B9042C8A3]]></sign>\n" +
                "<sign_type><![CDATA[MD5]]></sign_type>\n" +
                "<status><![CDATA[0]]></status>\n" +
                "<time_end><![CDATA[20190829202649]]></time_end>\n" +
                "<total_fee><![CDATA[100]]></total_fee>\n" +
                "<trade_type><![CDATA[pay.alipay.jspay]]></trade_type>\n" +
                "<transaction_id><![CDATA[9551600000201908294138792545]]></transaction_id>\n" +
                "<version><![CDATA[2.0]]></version>\n" +
                "</xml>";
        Map<String,String> map = null;
        try {
            map = XmlUtils.toMap(resString.getBytes(), "utf-8");
            System.out.println("++++++通知内容：" + XmlUtils.toXml(map));
            if(map.containsKey("sign")){
                String status = map.get("status");
                if(status != null && "0".equals(status)){
                    String result_code = map.get("result_code");
                    if(result_code != null && "0".equals(result_code)){
                        String out_trade_no = map.get("out_trade_no");
                        System.out.println(out_trade_no);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Long l1 = 123123L;
//        Long l2 = 123123L;
//        System.out.println(l1.equals(l2));
//        System.out.println(passwordKit.entrypt("futruedao"));//默认密码

//        Date date = DateTimeKit.stringToDate("20190819164304");

//        System.out.println(DateUtil.date(DateUtil.current(false)));


        /*boolean bool = StrUtil.equalsIgnoreCase(OrderStatusEnum.COMPLETED.getValue(),"5");
        System.out.println(bool);

        RedisKit redisKit = new RedisKit();
        Set<String> strSet = (Set<String>) redisKit.get(Global.REDIS_TOKEN_PREFIX+"*");

        for (String str:strSet) {
            System.out.println(str);
        }
        System.out.println(strSet.size());*/

        //解析密文 3>ticket>1>bId>2<<singleNo
       /* String qrval = "MA1X2J9H88>2aa9ee4b66be3f34149a223a07e12407181f6a8ce670acb7bd2e3d3e>9132>a380de7a1a3eb830a2bc3dd11cd5128c1c8dfbb7278b3b989a3f81a3>0100<<A10101";
        String[] oneArr = qrval.split("<<");
        if(oneArr==null||oneArr.length==0){
            System.out.println("|");
        }
//            return RJson.error("商家二维码数据错误");

        String[] twoArr = oneArr[0].split(">");
        if(twoArr==null||twoArr.length==0){
            System.out.println("|");
        }
//            return RJson.error("商家二维码数据错误");

        StringBuffer sbBusiness = new StringBuffer();
        sbBusiness.append(twoArr[2]);
        sbBusiness.append(twoArr[4]);
        sbBusiness.append(twoArr[0]);
        System.out.println(sbBusiness.toString());


        BigDecimal creditVal = new BigDecimal(120);
        System.out.println(creditVal.divide(new BigDecimal(100)));*/

//        HashSet<String> set = new HashSet<String>();
//        for (int i = 0; i < 100; i++) {
//            String chineseName = getRandomJianHan(5);
//            if (!set.contains(chineseName)) {
//                set.add(chineseName);
//            }
//        }
//        Iterator<String> iterator = set.iterator();
//        while (iterator.hasNext()) {
//            System.err.print(iterator.next() + "\n");
//        }

//        String str = HttpUtil.get("http://restapi.amap.com/v3/geocode/regeo?key=1a32c0167e8db25ecacb5bec6ccbef74&location=113.9523399405771,22.54038690331183");
//        String city = null;
//        JSONObject jsonobject = (JSONObject) JSONObject.parse(str);
//        String info = jsonobject.getString("info");
//        if("OK".equals(info)){
//            JSONObject jsonArray = jsonobject.getJSONObject("regeocode");
//            if(jsonArray!=null){
//                JSONObject jsonObject2 = jsonArray.getJSONObject("addressComponent");
//                if(jsonObject2!=null){
//                    city = jsonObject2.getString("city");
//                    System.out.println(city);
//                }
//            }
//        }

        /*TreeMap<Integer,String> treeMap = new TreeMap<Integer, String>(new Comparator<Integer>(){
            public int compare(Integer a,Integer b){
                return b-a;
            }
        });
        treeMap.put(234,"A2");
        treeMap.put(24,"B2");
        treeMap.put(34,"C2");
        System.out.println("Map2="+treeMap);
        for (String str : treeMap.values()) {
            System.out.println(str);
        }*/

        //TODO 生成user_role关系
        /*for (int i=47;i<=280;i++){
            StringBuffer sb = new StringBuffer();
            sb.append("INSERT INTO ");
            sb.append("`sys_user_role`(`user_id`,`role_id`)VALUES(");
            sb.append(i+"");
            sb.append(",2);");
            System.out.println(sb.toString());
        }*/
        //TODO 市级代理账号生成
//        String[] strAtoZ = new String[]{"M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};//

        //TODO市级代理账号生成
        /*for (int i=1;i<10;i++){
            for (int j=0;j<strAtoZ.length;j++){
//                System.out.println(strAtoZ[j]+i+"0000");
                StringBuffer sb = new StringBuffer();
                sb.append("INSERT INTO ");
                sb.append("`sys_user`(`login_name`,`login_pwd`,`emp_id`,`name`,`sex`,`email`,`phone`,`user_type`,`create_id`,`create_time`,`update_id`,`update_time`,`is_disable`,`del_status`,`remarks`) VALUES ");
                sb.append("('");
                sb.append(strAtoZ[j]+i+"0000");
                sb.append("','4684314ccdd4c16542a2f3d14470532b2d157341c624416f6470f89a','','");
                sb.append(strAtoZ[j]+i+"0000");
                sb.append("',NULL,'','','O',1,'2019-07-24 10:08:21',1,'2019-07-24 10:08:21','Y','Y',NULL);");

                System.out.println(sb.toString());
            }
        }*/
        //TODO 团代账号生成
        /*for (int i=1;i<10;i++){
            for (int j=0;j<strAtoZ.length;j++){
                //中间两位01-99
                for (int ii=1;ii<100;ii++){
                    StringBuffer sbd = new StringBuffer();
                    sbd.append(strAtoZ[j]);
                    sbd.append(i+"");//A1 - Z1不变
                    if(ii<10){
                        sbd.append("0"+ii);
                    }else{
                        sbd.append(ii+"");
                    }
                    sbd.append("00");
                    StringBuffer sbsql = new StringBuffer();
                    sbsql.append("INSERT INTO `nq_app_info`(`app_name`,`app_pkg`,`app_type`,`signature`,`default_use_count`,`create_id`,`create_time`,`update_id`,");
                    sbsql.append("`update_time`,`valid`,`voice_engine_switch`,`ocr_use_count`,`enable_status`)VALUES('");
                    sbsql.append(sbd.toString());
                    sbsql.append("', '");
                    sbsql.append(sbd.toString());
                    sbsql.append("', '2', '', '-1', '1', '2019-07-23 21:13:23', '1', '2019-07-23 21:13:23', 'N', '1', '50', 'Y');");

                    System.out.println(sbsql.toString());
                }
            }
        }*/
       //TODO 个代账号生成
        /*for (int i=1;i<10;i++){
            String filePath = "E:\\A-Z1(01-99)-A-Z9(01-99)_"+i+".sql";
            File fos = new File(filePath);
            for (int j=0;j<strAtoZ.length;j++){
                //中间两位01-99
                for (int ii=1;ii<100;ii++){
                    StringBuffer sbdPath = new StringBuffer();
                    sbdPath.append(strAtoZ[j]);
                    sbdPath.append(i+"");//A1 - Z1不变
                    if(ii<10){
                        sbdPath.append("0"+ii);
                    }else{
                        sbdPath.append(ii+"");
                    }
                    //个代末尾两位01-99
                    for (int jj=1;jj<100;jj++){
                        StringBuffer sbd = new StringBuffer();
                        sbd.append(strAtoZ[j]);
                        sbd.append(i+"");//A1 - Z1不变
                        if(ii<10){
                            sbd.append("0"+ii);
                        }else{
                            sbd.append(ii+"");
                        }
                        //TODO 写入文件保存当前个代数据脚本
                        //根据4位独立生成一个数据文件，比如：A10100(01-99)个代数据、A10200(01-99)个代数据。。。。
                        //创建文件，命名方式为团代编号（A10100)即可
                        //生成动态数据
                        if(jj<10){
                            sbd.append("0"+jj);
                        }else{
                            sbd.append(jj+"");
                        }
                        //数据结构体
                        StringBuffer sbSingle = new StringBuffer();
                        sbSingle.append("INSERT INTO `nq_ble_device`(`mac`,`ble_sn`,`ausn`,`pkg_id`,`enable_status`,`create_time`,`update_time`,`remarks`)VALUES('', NULL, '");
                        sbSingle.append(sbd.toString());
                        sbSingle.append("', '2', 'Y', '2019-07-24 12:48:16', '2019-07-24 13:54:20', NULL);");
//                        System.out.println(sbSingle.toString());
                        passwordKit.append(fos,sbSingle.toString());
                    }

                }
            }
        }*/

            //切成A-Z数据块
            /*for (int j=0;j<strAtoZ.length;j++){
                String filePath = "E:\\A-Z1(01-99)-A-Z9(01-99)_"+strAtoZ[j]+".sql";
                File fos = new File(filePath);
                for (int i=1;i<10;i++){
                //中间两位01-99
                for (int ii=1;ii<100;ii++){
                    StringBuffer sbdPath = new StringBuffer();
                    sbdPath.append(strAtoZ[j]);
                    sbdPath.append(i+"");//A1 - Z1不变
                    if(ii<10){
                        sbdPath.append("0"+ii);
                    }else{
                        sbdPath.append(ii+"");
                    }
                    //个代末尾两位01-99
                    for (int jj=1;jj<100;jj++){
                        StringBuffer sbd = new StringBuffer();
                        sbd.append(strAtoZ[j]);
                        sbd.append(i+"");//A1 - Z1不变
                        if(ii<10){
                            sbd.append("0"+ii);
                        }else{
                            sbd.append(ii+"");
                        }
                        //TODO 写入文件保存当前个代数据脚本
                        //根据4位独立生成一个数据文件，比如：A10100(01-99)个代数据、A10200(01-99)个代数据。。。。
                        //创建文件，命名方式为团代编号（A10100)即可
                        //生成动态数据
                        if(jj<10){
                            sbd.append("0"+jj);
                        }else{
                            sbd.append(jj+"");
                        }
                        //数据结构体
                        StringBuffer sbSingle = new StringBuffer();
                        sbSingle.append("INSERT INTO `nq_ble_device`(`mac`,`ble_sn`,`ausn`,`pkg_id`,`enable_status`,`create_time`,`update_time`,`remarks`)VALUES('', NULL, '");
                        sbSingle.append(sbd.toString());
                        sbSingle.append("', '2', 'Y', '2019-07-24 12:48:16', '2019-07-24 13:54:20', NULL);");
//                        System.out.println(sbSingle.toString());
                        passwordKit.append(fos,sbSingle.toString());
                    }

                }
            }*/
//        }



    }
}
