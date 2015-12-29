package com.axis.eload.session;
import java.util.HashMap;

import com.axis.common.Log;
import com.axis.common.log.LogWrapper;
import com.axis.security.Schannel;


public class Session {

	private static final LogWrapper log = Log.get();
	
	private HashMap<String, Schannel> hashMap = new HashMap<String, Schannel>();
	private HashMap<Long, String> timeMap = new HashMap<Long, String>();
	
	private static Session mapping;
	private static long timeLayer;
	private long validTime = 300000;
	
	private Session()
	{
	}
	/**
	 * 启动Session
	 */
	public static void start()
	{
		mapping = new Session();
		timeLayer = System.currentTimeMillis();
		log.debug("Session 启动");
	}
	/**
	 * 
	 * @return 一个Session实例
	 */
	public static Session getInstanse()
	{
		return mapping;
	}
	/**
	 * 获得一个指定ip对应的安全通道
	 * @param ip 客户端ip
	 * @return
	 */
	public Schannel getSecurityChannel(String ip)
	{
		Schannel sc = hashMap.get(ip);
		if(sc == null){
			sc = new Schannel();
			hashMap.put(ip, sc);
			timeMap.put(System.currentTimeMillis(), ip);
		}
		return sc;
	}
	/**
	 * 管理Session会清空失效时间的Session。</br>
	 * 失效时间默认5分钟（300000毫秒）
	 */
	private void sessionValidCheck()
	{
		long now = System.currentTimeMillis();
		long timeUP = now-validTime;
		long timeDown = timeLayer;
		log.debug("Session管理开始 开始检查时间:{}, 有效时间:{}, 检查时间上界:{}，检查时间下界:{}", now, validTime, timeUP, timeDown);
		
		do{
			String ip = timeMap.get(Long.valueOf(timeDown));
			if (ip != null)
				hashMap.remove(ip);
			timeDown++;
		}while(timeUP > timeDown);
		// 重新计时
		timeLayer = timeUP;
		
	}
	/**
	 * 设置Session失效时间
	 * @param time
	 */
	public void setSessionValidTime(long time)
	{
		this.validTime = time;
	}
	
	public void sessionMenage()
	{
		Runnable sm = new Runnable() {
			
			public void run() {
				while(true){
					try {
						Thread.sleep(validTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					sessionValidCheck();
				}
			}
		};
		
		Thread sessionMenage = new Thread(sm);
		sessionMenage.setName("sessionMenage");
		sessionMenage.start();
	}
}
