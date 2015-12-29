package com.axis.transaction;

import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

import com.axis.common.BerTLV;
import com.axis.common.Convert;
import com.axis.common.Dateutil;
import com.axis.common.Log;
import com.axis.common.chipher.MAC;
import com.axis.common.log.LogWrapper;

public class Load extends IssuerAuthenticate {
	private static final LogWrapper log = Log.get();

//	public final static byte[] TAG_0 = { (byte) 0x9F26, /* 应用密文AC */
//			(byte) 0x9F27, /* 密文信息数据 CID */
//			(byte) 0x9F10, /* 发卡行应用数据 */
//			(byte) 0x9F37, /* 不可预知数 */
//			(byte) 0x9F36, /* 应用交易计数器ATC */
//			(byte) 0x95, /* 终端验证结果 */
//			(byte) 0x9A, /* 交易日期 */
//			(byte) 0x9C, /* 交易类型 */
//			(byte) 0x9F02, /* 授权金额 */
//			(byte) 0x5F2A, /* 交易货币代码 */
//			(byte) 0x82, /* 应用交互特征AIP */
//			(byte) 0x9F1A, /* 终端国家代码 */
//			(byte) 0x9F03, /* 其他金额 */
//			(byte) 0x9F33, /* 终端性能 */
//			(byte) 0x9F34, /* 持卡人验证方法结果 */
//			(byte) 0x9F35, /* 终端类型 */
//			(byte) 0x9F1E, /* 接口设备序列号 */
//			(byte) 0x84, /* 专用文件名称 */
//			(byte) 0x9F09, /* 应用版本号 */
//			(byte) 0x9F41, /* 交易序列计数器 */
//			(byte) 0x9F63 /* 卡产品标示信息 */
//	};
//	public final static byte[] TAG_1 = { (byte) 0x5A/* PAN */, (byte) 0x5F34, /* PAN序列号 */
//			(byte) 0x57, /* 二磁道等效数据 */
//			(byte) 0x99, /* 联机PIN密文 */
//			(byte) 0xFF04 /* 服务点输入方式码 */
//	};
//	public final static byte[] TAG_END = { (byte) 0xDF31, /* 脚本执行结果 */
//			(byte) 0x95, /* 终端验证结果 */
//			(byte) 0x9F36, /* 应用交易序列号 */
//			(byte) 0x9F27, /* 密文信息类型 */
//			(byte) 0x9F26, /* 应用密文 */
//			(byte) 0x9F10 /* 发卡行应用数据 */
//	};
	  public final static String[] TAG_0 = { "9F26", /* 应用密文AC */
		      "9F27", /* 密文信息数据 CID */
		      "9F10", /* 发卡行应用数据 */
		      "9F37", /* 不可预知数 */
		      "9F36", /* 应用交易计数器ATC */
		      "95", /* 终端验证结果 */
		      "9A", /* 交易日期 */
		      "9C", /* 交易类型 */
		      "9F02", /* 授权金额 */
		      "5F2A", /* 交易货币代码 */
		      "82", /* 应用交互特征AIP */
		      "9F1A", /* 终端国家代码 */
		      "9F03", /* 其他金额 */
		      "9F33", /* 终端性能 */
		      "9F34", /* 持卡人验证方法结果 */
		      "9F35", /* 终端类型 */
		      "9F1E", /* 接口设备序列号 */
		      "84", /* 专用文件名称 */
		      "9F09", /* 应用版本号 */
		      "9F41", /* 交易序列计数器 */
		      "9F63" /* 卡产品标示信息 */
		  };
		  public final static String[] TAG_1 = { "5A/* PAN */", "5F34", /* PAN序列号 */
		      "57", /* 二磁道等效数据 */
		      "99", /* 联机PIN密文 */
		      "FF04" /* 服务点输入方式码 */
		  };
		  public final static String[] TAG_END = { "DF31", /* 脚本执行结果 */
		      "95", /* 终端验证结果 */
		      "9F36", /* 应用交易序列号 */
		      "9F27", /* 密文信息类型 */
		      "9F26", /* 应用密文 */
		      "9F10" /* 发卡行应用数据 */
		  };
	public static final String LOAD_CMD_0 = "7E400000";
	public static final String LOAD_CMD_1 = "7E40000100";
	public static final String LOAD_CMD_2 = "7E400100";

	public String UPDATE_EC_CMD = "04DA9F790A";
	public String SCRIPT_TEMPLATE = "72";
	public String _5a=null;
	public String _5f34=null;
	public String _57=null;
	public String _99 = null;
	public String _ff04 = null;
	public String _df31 = null;
	public String _9F36 = null;
	public String _9f27 = null;
	public String _9f26 = null;
	public String _9f10 = null;

	private boolean isOnline = false;
	private String TC=null;

	@Override
	public String readCard(int code, String icdata) {
		String data = null;
		String result = null;
		String cmd = null;
		int len = 0;
		byte[] blen = new byte[2];
		switch (code) {
		case 10:
			Date date = new Date();
			data = "000000000000"+ Dateutil.format(date, "yyMMddHHmmss") + "DD051122334455";
			if (data != null)
				cmd = LOAD_CMD_0 + String.format("%02x", data.length() / 2) + data;
			len = cmd.length() / 2;
			blen[0] = (byte) ((len & 0xFF00) >> 8);
			blen[1] = (byte) (len & 0x00FF);
			result = Convert.toString(blen) + cmd;
			break;
		case 11:
			// 分析 code10 的返回报文
			if(icdata == null)
				break;
			String money = icdata.substring(0, 12);
			String TS = icdata.substring(12,14);
			log.debug("充值金额 {}, 交易结果 {}", money, TS);
			analyzeBerTlv10(icdata.substring(14));
			if(!verifyIDD(IDD))
				break;
			// 发送 code11 指令
			cmd = LOAD_CMD_1;
			len = cmd.length() / 2;
			blen[0] = (byte) ((len & 0xFF00) >> 8);
			blen[1] = (byte) (len & 0x00FF);
			result = Convert.toString(blen) + cmd;
			isOnline = true;
			break;
		case 12:
			// 分析 code11 的返回报文
			if (icdata == null)
				break;
			analyzeBerTlv11(icdata);
			if(!issuerAuthenticate(_9f26))
				break;
			cmd = LOAD_CMD_2;
			if (isOnline){
				data = "00"+doFinal();
			}
			if (data != null)
				cmd = LOAD_CMD_2 + String.format("%02x", data.length() / 2) + data;
			len = cmd.length() / 2;
			blen[0] = (byte) ((len & 0xFF00) >> 8);
			blen[1] = (byte) (len & 0x00FF);
			result = Convert.toString(blen) + cmd;
			break;
		case 13:
			// 处理报文数据，并且关闭安全通道
			if(icdata == null)
				break;
			TC = icdata.substring(0,2);
			analyzeBerTlv12(icdata.substring(2));
			break;
		default:
			break;
		}
		return result;
	}

	private void analyzeBerTlv12(String tlv) {
		log.debug("BER-TLV {}", tlv);
		HashMap<String, String> map = BerTLV.split(tlv);
		for (Entry<String, String> entry : map.entrySet()) {
			String tag = entry.getKey();
			String value = entry.getValue();
			switch (tag) {
			case Tag._DF31:
				_df31 = value;
				break;
			case Tag._95:
				super._95 = value;
				break;
			case Tag._9F36:
				super._9f36 = value;
				break;
			case Tag._9F27:
				_9f27 = value;
				break;
			case Tag._9F26:
				super._9f26 = value;
				break;
			case Tag._9F10:
				_9f10 = value;
				break;
			default:
				break;
			}
		}
	}
	private void analyzeBerTlv11(String tlv) {
		log.debug("BER-TLV {}", tlv);
		HashMap<String, String> map = BerTLV.split(tlv);
		for (Entry<String, String> entry : map.entrySet()) {
			String tag = entry.getKey();
			String value = entry.getValue();
			switch (tag) {
			case Tag._5A:
				_5a = value;
				log.debug("PAN {}", _5a);
				break;
			case Tag._5F34:
				_5f34 = value;
				log.debug("PAN序列号 {}", _5f34);
				break;
			case Tag._57:
				_57 = value;
				log.debug("二磁道等效数据 {}", _57);
				break;
			case Tag._99:
				_99 = value;
				log.debug("联机PIN密文 {}", _99);
				break;
			case Tag._FF04:
				_ff04 = value;
				log.debug("服务点输入方式码 {}", _ff04);
				break;
			default:
				break;
			}
		}
	}

	private void analyzeBerTlv10(String tlv) {
		log.debug("BER-TLV {}", tlv);
		HashMap<String, String> map = BerTLV.split(tlv);
		for (Entry<String, String> entry : map.entrySet()) {
			String tag = entry.getKey();
			String value = entry.getValue();
			switch (tag) {
			case Tag._9F02:
				super._9f02 = value;
				break;
			case Tag._9F03:
				super._9f03 = value;
				break;
			case Tag._9F1A:
				super._9f1a = value;
				break;
			case Tag._95:
				super._95 = value;
				break;
			case Tag._5F2A:
				_5f2a = value;
				break;
			case Tag._9A:
				_9a = value;
				break;
			case Tag._9C:
				super._9c = value;
				break;
			case Tag._9F37:
				_9f37 = value;
				break;
			case Tag._82:
				_82 = value;
				break;
			case Tag._9F36:
				_9f36 = value;
				break;
			case Tag._9F26:
				_9f26 = value;
				break;
			case Tag._9F10:
				log.debug("9f10 {}", value);
				String lenbuf = value.substring(0, 2);
				log.debug("长度 {}", lenbuf);
				encryptionIndex = value.substring(2, 4);
				log.debug("分散密钥索引 {}", encryptionIndex);
				encryptionVersion = value.substring(4, 6);
				log.debug("密文版本号 {}", encryptionVersion);
				cvr = value.substring(6, 14);
				log.debug("卡片验证结果 {}", cvr);
				algorithmFlag = value.substring(14, 16);
				log.debug("算法标识 {}", algorithmFlag);
				IDD = value.substring(16);
				log.debug("idd {}", IDD);
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 生成发卡行脚本
	 * 
	 * @return
	 */
	String generateScript() {
		StringBuilder script = new StringBuilder("");

		// 金额计算
		String total = String.valueOf(Integer.parseInt(_9f79) + Integer.parseInt(_9f02));
		total = "000000000000".substring(total.length()) + total;

		// MAC计算
		String source = UPDATE_EC_CMD + _9f36 + AC + total;
		String mac = MAC.macForDes_3Des(mac_key_session, "0000000000000000", source, MAC.IS_SHORT);

		script.append("9F180400000001").append("860F").append(UPDATE_EC_CMD).append(total).append(mac);

		return script.toString();
	}

	String doFinal() {
		ARCode = "3030";
		calAPRC();
		// 发卡行脚本数据
		StringBuilder script = new StringBuilder();
		String temp = generateScript();
		script.append(SCRIPT_TEMPLATE).append(String.format("%02x", temp.length() / 2)).append(temp);

		return "910A" + ARPC + ARCode + "8A02"+ ARCode + script.toString();
	}
}
