package com.think.core.net.codec;

import com.think.core.net.message.Message;

/**
 * 消息编解码接口类
 * 
 * @author veione
 *
 */
public interface Codec {
	/**
	 * 消息编码
	 * 
	 * @param msg
	 */
	void encode(Message msg);

	/**
	 * 消息解码
	 * 
	 * @param msg
	 */
	void decode(Message msg);
}
