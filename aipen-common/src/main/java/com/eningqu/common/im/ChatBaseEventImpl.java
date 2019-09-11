
package com.eningqu.common.im;

import com.dy.javaclient.event.ChatBaseEvent;
import com.dy.javaclient.utils.Log;

import java.util.Observer;


public class ChatBaseEventImpl implements ChatBaseEvent
{
	private final static String TAG = ChatBaseEventImpl.class.getSimpleName();

	
	// 本Observer目前仅用于登陆时（因为登陆与收到服务端的登陆验证结果
	// 是异步的，所以有此观察者来完成收到验证后的处理）
	private Observer loginOkForLaunchObserver = null;
	
	//@Override
	public void onLoginMessage(int dwErrorCode)
	{
		if (dwErrorCode == 0) 
		{
			Log.p(TAG, "【DEBUG_UI】IM服务器登录/连接成功！");

		}
		else 
		{
			Log.e(TAG, "【DEBUG_UI】IM服务器登录/连接失败，错误代码：" + dwErrorCode);
		}
		
		// 此观察者只有开启程序首次使用登陆界面时有用
		if(loginOkForLaunchObserver != null)
		{
			loginOkForLaunchObserver.update(null, dwErrorCode);
			loginOkForLaunchObserver = null;
		}
	}

	//@Override
	public void onLinkCloseMessage(int dwErrorCode)
	{
		Log.e(TAG, "【DEBUG_UI】与IM服务器的网络连接出错关闭了，error：" + dwErrorCode);
		

	}
	
	public void setLoginOkForLaunchObserver(Observer loginOkForLaunchObserver)
	{
		this.loginOkForLaunchObserver = loginOkForLaunchObserver;
	}


}
