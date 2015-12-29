/**
 * 
 */
package com.axis.security.implement;

import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Date;

import com.axis.common.ftBaseFunc;
import com.axis.common.pki.CertificateHelper;
import com.axis.common.pki.X509Helper;
import com.axis.security.Config;
import com.axis.security.interfaces.InterfaceHandshake;

/**
 * @author axis
 *
 */
public class Handshake implements InterfaceHandshake{

	ftBaseFunc ftcommon = new ftBaseFunc();
	private String[] terminalCiphersuites;
	private String[] serverCipherSuite;

	/**
	 * 返回读取终端序列号指令
	 */
	public String readTerminalNo() {
		return "7E100001";
	}

	/**
	 * 返回读取终端随机数指令
	 * 
	 * @return 读取终端随机数指令
	 */
	public String readTerminalRandom() {
		return "7E25000021";
	}

	/**
	 * 返回指定length字节长度随机数
	 * 
	 * @return length
	 */
	public String generateRandom(int length) {
		String ramdom = ftcommon.generateRandomNumeric(length);
		return ramdom;
	}

	/**
	 * 发送服务器证书指令
	 * @param data 服务器证书数据十六进制字符串类型
	 * @return 服务器证书指令
	 */
	public String sendServerCertificate(String data) {
		return "7E260000" + String.format("%02x", data.length() / 2) + data;
	}

	/**
	 * 获得制定服务器证书的主体
	 * 
	 * @param server
	 * @return 服务器证书的主体
	 */
	public String getServerCertificateMainBody(int server) {
		try {
			byte[] szB = new X509Helper(Config.SERVER_CERT_PATH).getX509Certificate().getTBSCertificate();
			return ftcommon.ftBytesToHexString(szB);
		} catch (CertificateEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取指定服务器证书公钥
	 * 
	 * @param server 服务器标识
	 * @return 服务器证书十六进制字符串表示
	 */
	public String getServerCertificatePublicKey(int server) {
		byte[] szB = new X509Helper(Config.SERVER_CERT_PATH).getX509Certificate().getPublicKey().getEncoded();
		return ftcommon.ftBytesToHexString(szB);
	}

	/**
	 * 验证服务器证书指令
	 * @param data 证书的签名值
	 * @return 证书签名指令
	 */
	public String verifyServerCertificate(String data) {
		return "7E270000" + String.format("%02x", data.length() / 2) + data;
	}
	/**
	 * 获取服务器证书的主体部分用于签名的输入数据
	 * @param server 服务器标识
	 * @return 返回指定证书的主体
	 */
	public String getServerCertificateSignature(int server) {
		return new X509Helper(Config.SERVER_CERT_PATH).getSignature();
	}
	/**
	 * 返回读取终端证书指令
	 */
	public String readTerminalCertificate() {
		return "7E230000";
	}
	/**
	 * 返回读取终端证书指令
	 * @param SW2 后续读取数据的长度
	 */
	public String readTerminalCertificate(String SW2) {
		return "7E240000".concat(SW2);
	}

	/**
	 * 返回读取终端签名指令
	 * @param data 服务器发给终端的签名值输入数据
	 */
	public String readTerminalSignature(String data) {
		return "7E280000" + String.format("%02x", data.length() / 2) + data;
	}

	/**
	 * 返回读取终端主密钥指令
	 */
	public String readTerminalMasterKey() {
		return "7E29000000";
	}

	/**
	 * 返回服务器完成消息指令
	 * @param hmac 服务器握手完成消息
	 */
	public String sendServerFinishMessage(String hmac) {
		return "7E2A000114".concat(hmac);
	}

	/**
	 * 返回读取终端完成消息指令
	 */
	public String readTerminalFinishMessage() {
		return "7E2A000014";
	}

	/**
	 * 返回生成会话密钥的指令
	 */
	public String generateSessionKey() {
		return "7E2A000200";
	}

	/**
	 * 验证证书合法
	 * @param certs 需要验证的证书
	 * @return true 合法， false 不合法
	 */
	public boolean verifyCertificateLegal(X509Certificate certs) {
		try {
			certs.checkValidity(new Date());
			byte data[] = certs.getTBSCertificate();
			byte sign[] = certs.getSignature();
			if (CertificateHelper.verify(data, sign, Config.CA_CERT_PATH))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * 验证终端签名
	 * @param data 签名的输入数据
	 * @param sign 签名值
	 * @param cert 终端证书
	 * @return true 验证通过，false 不通过
	 */
	public boolean verifyTerminalSignature(byte[] data, byte[] sign, X509Certificate cert) {
		
		try {
			return CertificateHelper.verify(data, sign, cert);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 验证算法标识，服务器是否支持该算法
	 * @param tcipher 终端算法标识
	 */
	public boolean verifyChipher(String tcipher) {
		terminalCiphersuites = new String[8];
		StringBuilder dest = new StringBuilder();
		int ret = new ftBaseFunc().ftDataToBitStringNoASCII(tcipher,dest);
		if (ret != 0)
			return false;
		String cipher = dest.reverse().toString();
		// 验证终端加密算法
		int k = 0;
		for (int i = 0; i<8; i++) {
			char ch = cipher.charAt(i);
			if (ch == '1') {
				switch (i) {
				case 7:
					terminalCiphersuites[k++] = Config.SM2;
					return true;
				case 6:
					terminalCiphersuites[k++] = Config.CA_2048;
					break;
				case 5:
					terminalCiphersuites[k++] = Config.SM4;
					break;
				case 4:
					terminalCiphersuites[k++] = Config.DES3;
					break;
				case 3:
					return false;
				case 2:
					return false;
				case 1:
					terminalCiphersuites[k++] = Config.ECC;
					break;
				case 0:
					terminalCiphersuites[k++] = Config.RSA;
					break;
				default:
					return false;
				}
			} else {
				if (i == 6)
					terminalCiphersuites[k++] = Config.CA_1024;
				else if (i == 7) {
					terminalCiphersuites[k++] = Config.FOREIGN_RSA;
				}
			}
			if(i == 7)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @return 服务器加密算法标识
	 */
	public String getServerCipher() {
		return Config.CIPHER;
	}

	/**
	 * 返回一个加密算法标识枚举数组
	 */
	public String[] getTerminalCiperSuite() {
		return this.terminalCiphersuites;
	}

	/**
	 * 返回一个加密算法标识枚举数组
	 */
	public String[] getServerCipherSuite() {
		serverCipherSuite = new String[8];
		BigInteger bignum = new BigInteger(getServerCipher());
		int bytes[] = { 0, 1, 2, 3, 4, 5, 6, 7 };
		int k = 0;
		for (int i : bytes) {
			if (bignum.testBit(i)) {
				switch (i) {
				case 0:
					serverCipherSuite[k++] = Config.RSA;
					break;
				case 1:
					serverCipherSuite[k++] = Config.ECC;
					break;
				case 2:
					break;
				case 3:
					break;
				case 4:
					serverCipherSuite[k++] = Config.DES3;
					break;
				case 5:
					serverCipherSuite[k++] = Config.SM4;
					break;
				case 6:
					serverCipherSuite[k++] = Config.CA_2048;
					break;
				case 7:
					serverCipherSuite[k++] = Config.SM2;
					break;
				default:
					break;
				}
			} else {
				if (i == 6)
					serverCipherSuite[k++] = Config.CA_1024;
				else if (i == 7) {
					serverCipherSuite[k++] = Config.FOREIGN_RSA;
				}
			}
		}
		return serverCipherSuite;
	}

	enum mCipherSuite {
		RSA, ECC, DES3, SM4, CA2048, CA1024, SM2, Foreign
	}

	public void setServerCipherSuite(String cipher) {
		// TODO Auto-generated method stub
		
	}

	public String EncrptData() {
		// TODO Auto-generated method stub
		return "7F2B0000";
	}

	public String seriesEncryptData() {
		// TODO Auto-generated method stub
		return "7F2B0001";
	}

	public void closeSecurityChannel() {
		// TODO Auto-generated method stub
		
	}
}
