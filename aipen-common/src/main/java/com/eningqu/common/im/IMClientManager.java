
package com.eningqu.common.im;

import com.dy.javaclient.ClientCoreSDK;
import com.dy.javaclient.conf.ConfigEntity;

public class IMClientManager
{
	private static String TAG = IMClientManager.class.getSimpleName();
	
	private static IMClientManager instance = null;
	
	/** MobileIMSDK是否已被初始化. true表示已初化完成，否则未初始化. */
	private boolean init = false;
	
	// 
	private ChatBaseEventImpl baseEventListener = null;
	//
	private ChatTransDataEventImpl transDataListener = null;
	//
	private MessageQoSEventImpl messageQoSListener = null;

	public static IMClientManager getInstance()
	{
		if(instance == null)
			instance = new IMClientManager();
		return instance;
	}
	
	private IMClientManager()
	{
		initIMSDK();
	}

	public void initIMSDK()
	{
		if(!init)
		{
			// 设置AppKey
			ConfigEntity.appKey = "9418023cfd98c579b6001761";
		
			// 设置服务器ip和服务器端口
			ConfigEntity.serverIP = "futruedao.com";
			ConfigEntity.serverUDPPort = 10901;
	    
			// MobileIMSDK核心IM框架的敏感度模式设置
			ConfigEntity.setSenseMode(ConfigEntity.SenseMode.MODE_120S);
	    
			// 开启/关闭DEBUG信息输出
	    	ClientCoreSDK.DEBUG = false;
	    
			// 设置事件回调
			baseEventListener = new ChatBaseEventImpl();
			transDataListener = new ChatTransDataEventImpl();
			messageQoSListener = new MessageQoSEventImpl();
			ClientCoreSDK.getInstance().setChatBaseEvent(baseEventListener);
			ClientCoreSDK.getInstance().setChatTransDataEvent(transDataListener);
			ClientCoreSDK.getInstance().setMessageQoSEvent(messageQoSListener);
			
			init = true;
		}
	}

	public void release()
	{
		ClientCoreSDK.getInstance().release();
		resetInitFlag();
	}

	/**
	 * 重置init标识。
	 * <p>
	 * <b>重要说明：</b>不退出APP的情况下，重新登陆时记得调用一下本方法，不然再
	 * 次调用 {@link #initIMSDK()} ()} 时也不会重新初始化MobileIMSDK（
	 * 详见 {@link #initIMSDK()}代码）而报 code=203错误！
	 * 
	 */
	public void resetInitFlag()
	{
		init = false;
	}

	public ChatTransDataEventImpl getTransDataListener()
	{
		return transDataListener;
	}
	public ChatBaseEventImpl getBaseEventListener()
	{
		return baseEventListener;
	}
	public MessageQoSEventImpl getMessageQoSListener()
	{
		return messageQoSListener;
	}
}

