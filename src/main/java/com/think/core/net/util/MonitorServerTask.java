package com.think.core.net.util;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.think.core.net.Server;
import com.think.core.net.server.ServerConfig;
import com.think.util.TimeUtil;

/**
 * 服务器消息监控任务类
 * 
 * @author veione
 *
 */
public class MonitorServerTask extends TimerTask {
	private Logger logger = LoggerFactory.getLogger(MonitorServerTask.class);
	private IOStatistics statistics;
	private ServerConfig serverConfig;
	static long lastInNum;
	static long lastOutNum;
	static long lastInBytes;
	static long lastOutBytes;

	public MonitorServerTask(Server server) {
		this.statistics = server.getIOStatistics();
		this.serverConfig = server.getServerConfig();
	}

	@Override
	public void run() {
		long currentInMsg = this.statistics.getInMessages();
		long currentOutMsg = this.statistics.getOutMessages();
		long newAddedIn = currentInMsg - lastInNum;
		long newAddedOut = currentOutMsg - lastOutNum;
		lastInNum = currentInMsg;
		lastOutNum = currentOutMsg;

		long currentInBytes = this.statistics.getInBytes();
		long currentOutBytes = this.statistics.getOutBytes();
		long newAddedBytesIn = currentInBytes - lastInBytes;
		long newAddedBytesOut = currentOutBytes - lastOutBytes;
		lastInBytes = currentInBytes;
		lastOutBytes = currentOutBytes;

		if (this.serverConfig.isEnableMonitor()) {
			logger.info("----------------------------------------------------------------|");
			logger.info(
					"|" + getCurrentTimeString() + "\tMessages(In/Out):\t" + newAddedIn + "/" + newAddedOut + "\t|");
			logger.info("|" + getCurrentTimeString() + "\tBytes(In/Out):   \t" + newAddedBytesIn + "/"
					+ newAddedBytesOut + "\t|");
			logger.info("----------------------------------------------------------------|");
		}
	}

	public static String getCurrentTimeString() {
		String dateTime = TimeUtil.getNowDateTime("yyyy-MM-dd HH:mm:ss.SSSS");
		return dateTime;
	}
}
