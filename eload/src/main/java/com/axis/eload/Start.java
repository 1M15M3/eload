/**
 * 
 */
package com.axis.eload;

import com.axis.eload.action.SchannelAction;
import com.axis.eload.session.Session;
import com.axis.axisnetty.NServer;
import com.axis.axisnetty.ServerConfig;

/**
 * @author axis
 *
 */
public class Start {

	public static void main(String[] args)
	{
		// 启动session
		Session.start();
		// 启动Session管理
		Session.getInstanse().sessionMenage();
		
		// 设置通信端口
		ServerConfig.setPort(8090);
		// 设置服务接口
		ServerConfig.setAction("/api/sc", SchannelAction.class);
		// 启动服务
		NServer.start();
	}

}
