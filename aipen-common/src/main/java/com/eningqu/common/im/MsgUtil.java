package com.eningqu.common.im;

import com.dy.javaclient.core.LocalUDPDataSender;

import java.util.Observable;
import java.util.Observer;

public  class MsgUtil {

    IMClientManager imClientManager = IMClientManager.getInstance();
    public static String  userid="-1";
    public static String user_pwd="11";
    public static boolean isLogin=false;

    public static void sendMsg(String to_user_id,String msg){
        if(!isLogin) doLoginIM(userid,user_pwd);
        new LocalUDPDataSender.SendCommonDataAsync(msg,to_user_id){
            @Override
            protected void onPostExecute(Integer code)
            {
                if(code == 0)
                    System.out.println("2数据已成功发出！from ");
                else
                    System.out.println("数据发送失败。错误码是："+code+"！");
            }
        }.execute();
    }

    public static void sendMsg(String to_user_id,String msg,int typeu){
        if(!isLogin) doLoginIM(userid,user_pwd);
        new LocalUDPDataSender.SendCommonDataAsync(msg,to_user_id,typeu){
            @Override
            protected void onPostExecute(Integer code)
            {
                if(code == 0)
                    System.out.println("2数据已成功发出！from ");
                else
                    System.out.println("数据发送失败。错误码是："+code+"！");
            }
        }.execute();
    }

    private static void doLoginIM(String uid, String password) {
        IMClientManager imClientManager = IMClientManager.getInstance();
        imClientManager.getBaseEventListener().setLoginOkForLaunchObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {

            }
        });
        new LocalUDPDataSender.SendLoginDataAsync( uid, password) {
            @Override
            protected void fireAfterSendLogin(int code) {
                System.out.println(code);
                if (code == 0) {
                    System.out.println( "数据发送成功！");
                } else {
                    System.out.println( "数据发送失败。错误码是：+ code ="+code);

                }
            }
        }.execute();

        isLogin=true;
    }


}
