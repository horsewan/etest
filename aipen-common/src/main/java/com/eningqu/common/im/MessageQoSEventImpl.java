
package com.eningqu.common.im;

import com.dy.javaclient.event.MessageQoSEvent;
import com.dy.javaclient.utils.Log;
import com.dy.zserver.protocal.Protocal;

import java.util.ArrayList;

public class MessageQoSEventImpl implements MessageQoSEvent
{
	private final static String TAG = MessageQoSEventImpl.class.getSimpleName();

	public void messagesLost(ArrayList<Protocal> lostMessages)
	{
		Log.d(TAG
				, "【DEBUG_UI】收到系统的未实时送达事件通知，当前共有"
						+lostMessages.size()+"个包QoS保证机制结束，判定为【无法实时送达】！");
	}

	//@Override
	public void messagesBeReceived(String theFingerPrint)
	{
		if(theFingerPrint != null)
		{
			Log.d(TAG, "【DEBUG_UI】收到对方已收到消息事件的通知，fp="+theFingerPrint);

		}
	}

}
