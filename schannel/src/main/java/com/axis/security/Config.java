/**
 * 
 */
package com.axis.security;

/**
 * @author axis
 *
 */
public class Config {

	public static final String ENCODING = "UTF-8";
	public static final String CIPHER = "08";
	public static final String CA_CERT_PATH = "certs//ICCBD CA.cer";
	public static final String SERVER_CERT_PATH = "certs//eload.server.cer";
	public static final String KEY_STORE_PATH = "keystore//eload.server.p12";
	public static final String KEY_STORE_PWD = "eload.server";
	public static final String ALIAS = "192.168.36.28";
	// server certificate package number
	/**
	 * 分包规则</p>
	 * OU字段必须包含在一个包里，</br>
	 * OU包的索引 {@code OU_PKG_INDEX} </br>
	 * OU距离OU包的偏离距离{@code OU_OFFSET}个传输字节 HEX</br>
	 * 公钥字段必须拆分两个邻包，且第二个包必须全部是公钥字段。
	 * 第一个公钥包的索引{@code PUBLIC_KEY_PKG_INDEX}</br>
	 * 公钥距离公钥包的偏离距离{@code PUBLIC_KEY_OFFSET}个传输字节 HEX</br>
	 * 为了方便截取字符串包大小是按照字节数十进制计算，</br>
	 * 一个传输字节=两个字节。例子：指令 7E01000000 是10个字节5个传输字节。 
	 */
	public static final int SERVER_CERT_PKG_NUM = 4;
	public static final String NONE_OFFSET = "00";
	public static final int OU_PKG_INDEX = 1;
	public static final String OU_OFFSET = "00";
	public static final int PUBLIC_KEY_PKG_INDEX = 2;
	public static final String PUBLIC_KEY_OFFSET = "00";
	public static final int[] SERVER_CERT_PKG_SIZE = {0,0,426,932,1006};
	
	/**
	 * 服务器签名分包
	 */
	public static final int SERVER_SIGN_PKG_NUM = 2;
	public static final int[] SERVER_SIGN_PKG_SIZE = {0,256,522};
	
	public static final String START_PKG_FLAG = "80";
	public static final String END_PKG_FLAG = "40";
	public static final String PUBLIC_KEY_PKG_FLAG = "10";
	public static final String OU_PKG_FLAG = "20";
	public static final String COMMON_PKG_FLAG = "00";
	// CipherSuit
	public static final String RSA = "RSA";
	public static final String ECC ="ECC";
	public static final String DES3 = "3DES";
	public static final String SM4 ="SM4";
	public static final String CA_2048 = "CA(2048)";  
	public static final String CA_1024 = "CA(1024)";
	public static final String SM2 = "国密SM2";
	public static final String FOREIGN_RSA = "国际RSA";
}
