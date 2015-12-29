package com.axis.transaction;

import com.axis.common.chipher.DES;
import com.axis.common.chipher.MAC;
import com.axis.common.log.LogWrapper;
import com.axis.common.Log;
import com.axis.common.Util;

abstract class IssuerAuthenticate {
	private static final LogWrapper log = Log.get();

	public abstract String readCard(int code, String icdata);
	/**
	 * 校验发卡卡行自定义数据MAC
	 * 
	 * @param idd
	 * @return
	 */
	boolean verifyIDD(String idd) {
		log.debug("验证发卡行应用数据的自定义数据的MAC, 发卡行自定义数据 {}", idd);
		calKeySession();
		String sourceMac = idd.substring(14);
		_9f79 = idd.substring(4, 14);
		String source = _9f36 + _9f79 + "00";
		log.debug("MAC计算的输入数据 {}", source);
		String mac = null;
		if (algorithmFlag.equals(DES3))
			mac = MAC.macForDes_3Des(mac_key_session, "0000000000000000", source, MAC.IS_SHORT);
		log.debug("收到的MAC {},计算的MAC {}", sourceMac, mac);
		if (mac.equals(sourceMac)) {
			log.debug("发卡行验证MAC成功,收到的MAC {},计算的MAC {}", sourceMac, mac);
			return true;
		}
		return false;
	}
	
	/**
	 * 发卡行认证(验证卡片计算的应用密文)
	 * 
	 * @param 卡片计算的应用密文
	 * @return 应用密文
	 */
	boolean issuerAuthenticate(String cac) {
		log.debug("发卡行认证，密文版本 {}, 算法标识 {}, 卡片应用密文 {}", ENCRYPTION_VER, algorithmFlag,cac);
		StringBuilder source = new StringBuilder();
		source.append(_9f02).append(_9f03).append(_9f1a).append(_95).append(_5f2a).append(_9a).append(_9c).append(_9f37).append(_82)
				.append(_9f36).append(cvr);
		log.debug("AC 过程密钥 {}", ac_key_session);
		log.debug("发卡行计算应用密文的输入数据 {}", source);
		String ac = null;
		if (algorithmFlag.equals(DES3))
			ac = MAC.macForDes_3Des(ac_key_session, "0000000000000000", source.toString(), MAC.IS_LONG);
		log.debug("发卡行认证成功 收到的AC {} 计算的AC {}", cac, ac);
		if (ac.equals(cac)) {
			AC = ac;
			log.debug("发卡行认证成功 收到的AC {} 计算的AC {}", cac, ac);
			return true;
		}
		return false;
	}

	/**
	 * 生成发卡行脚本
	 * 
	 * @return
	 */
	abstract String generateScript();

	void calAPRC() {
		// AC & 3030 & 000000000000 XOR
		String xxx = ARCode.concat("000000000000");
		String yyy = Util.calXOR(AC, xxx, 8);

		ARPC = DES.enc3DES(ac_key_session, yyy, false);
	}
	
	String ac_key_session;
	String mac_key_session;
	void calKeySession(){
		String ATC = _9f36;
		log.debug("应用交易计数器 ATC {}", ATC);
		String NATC =  Util.calXOR(ATC,"FFFF", 2);
		String left = "000000000000".concat(ATC);
		String right = "000000000000".concat(NATC);
		ac_key_session = DES.enc3DES(MK_AC, left+right, false);
		mac_key_session = DES.enc3DES(MK_MAC, left+right, false);
		log.debug("AC 过程密钥 {}", ac_key_session);
		log.debug("MAC 过程密钥 {}", mac_key_session);
	}

	abstract String doFinal();
	
	final String DES3 = "01";
	final String ENCRYPTION_VER = "01";
	final String MK_AC = "01C270D329575D3E5737C7203EDA02B0";
	final String MK_MAC = "730ED3AD380792E043344CD9E09134DC";
	final String MK_DEK = "0768FD91FD0B2ACE92CE7ABF0D08856B";

	String encryptionVersion = null;
	String encryptionIndex = null;
	String algorithmFlag = null;
	String IDD = null;
	String ARPC = null;
	String ARCode = null;
	String AC = null;

	/**
	 * 应用密文
	 */
	public String _9f26;
	/**
	 * 电子现金余额
	 */
	public String _9f79;
	/**
	 * 授权金额
	 */
	public String _9f02;
	/**
	 * 其他金额
	 */
	public String _9f03;
	/**
	 * 终端国家代码
	 */
	public String _9f1a;
	/**
	 * 终端验证结果
	 */
	public String _95;
	/**
	 * 交易货币代码
	 */
	public String _5f2a;
	/**
	 * 交易日期
	 */
	public String _9a;
	/**
	 * 交易类型
	 */
	public String _9c;
	/**
	 * 不可预知数
	 */
	public String _9f37;
	/**
	 * 应用交互特征（AIP）
	 */
	public String _82;
	/**
	 * 应用交易计数器（ATC）
	 */
	public String _9f36;
	/**
	 * 卡片验证结果, 含在9F10
	 */
	public String cvr;
}
