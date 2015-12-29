package com.axis.security.interfaces;

public interface InterfaceHandshake {

	/**
	 * 读终端序列号
	 */
	public String readTerminalNo();
	/**
	 * 设置服务器加密算法标识
	 * @param cipher
	 */
	public void setServerCipherSuite(String cipher);
	
	/**
	 * 获取服务器加密算法标识
	 */
	public String[] getServerCipherSuite();
	
	/**
	 * 验证加密算法标识
	 * @param cipher 终端支持的算法标识别
	 * @return 服务器支持的算法标识
	 */
	public boolean verifyChipher(String cipher);
	/**
	 * 生成终端随机数
	 * @param length 指定生成的随机数长度（十进制）
	 * @return 生成指定长度的随机数
	 */
	public String generateRandom(int length);
	/**
	 * 
	 * @param data 证书数据十六进制
	 * @return 服务器证书
	 */
	public String sendServerCertificate(String data);
	/**
	 * 验证服务器证书
	 */
	public String verifyServerCertificate(String data);
	/**
	 * 读取终端证书
	 */
	public String readTerminalCertificate();
	/**
	 * 读取终端签名
	 */
	public String readTerminalSignature(String signature);
	/**
	 * 读取终端主密钥
	 */
	public String readTerminalMasterKey();
	/**
	 * 发送服务器完成消息
	 */
	public String sendServerFinishMessage(String hmac);
	/**
	 * 读取终端完成消息
	 */
	public String readTerminalFinishMessage();
	/**
	 * 生成会话密钥
	 */
	public String generateSessionKey();
	/**
	 * 无级联方式发送密文指令
	 */
	public String EncrptData();
	/**
	 * 级联方式发送密文指令
	 */
	public String seriesEncryptData();
	/**
	 * 关闭安全通道指令
	 */
	public void closeSecurityChannel();
	/**
	 * 支持的加密算法
	 * @author axis
	 *
	 */
	public enum CipherSuite
	{
		RSA,ECC,DES3,SM4,CA2048,CA1024,SM2,Foreign
	}
}
