
package com.eningqu.common.im;

import com.dy.javaclient.event.ChatTransDataEvent;
import com.dy.javaclient.utils.Log;

public class ChatTransDataEventImpl implements ChatTransDataEvent
{
	private final static String TAG = ChatTransDataEventImpl.class.getSimpleName();


	public void onTransBuffer(String fingerPrintOfProtocal, String userid, String dataContent, int typeu)
	{
		Log.d(TAG, "【DEBUG_UI】[typeu="+typeu+"]收到来自用户"+userid+"的消息:"+dataContent);

	}

	public void onErrorResponse(int errorCode, String errorMsg)
	{
		Log.d(TAG, "【DEBUG_UI】收到服务端错误消息，errorCode="+errorCode+", errorMsg="+errorMsg);

	}
}
