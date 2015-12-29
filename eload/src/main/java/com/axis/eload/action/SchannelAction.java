/**
 * 
 */
package com.axis.eload.action;

import java.security.cert.X509Certificate;

import org.json.JSONObject;

import com.axis.axisnetty.action.Action;
import com.axis.axisnetty.handler.Request;
import com.axis.axisnetty.handler.Response;
import com.axis.common.Log;
import com.axis.common.ftBaseFunc;
import com.axis.common.log.LogWrapper;
import com.axis.common.pki.X509Helper;
import com.axis.eload.EloadCofing;
import com.axis.eload.message.MessageType;
import com.axis.security.Schannel;
import com.axis.transaction.Load;

/**
 * @author axis
 *
 */
public class SchannelAction implements Action{

	private static final LogWrapper log = Log.get();
	

	public void doAction(Request request, Response response) {
		String type = request.getParam(EloadCofing.MESSAGE_TYPE);
		String terminalStatusCode = request.getParam(EloadCofing.TERMINAL_STATUS_CODE);
		String body = request.getParam(EloadCofing.BODY);
		
		if(EloadCofing.SUCCESS_TERMINAL_STATUS.equals(terminalStatusCode))
		{
			int messageType = Integer.parseInt(type);
			String ip = request.getIp();
			Schannel handshake = com.axis.eload.session.Session.getInstanse().getSecurityChannel(ip);
			
			String data = null;
			
			log.debug("IP {} 发起 {} 请求 {} 链接", ip, messageType, request.getHeaders().get("Connection"));
			if( messageType  == MessageType.APPLICATION_DATA)
				log.debug("body {}" ,body);
			switch(messageType) 
			{
			case MessageType.CLIENT_HELLO:
				data = handshake.generateRandom(body);
				break;
			case MessageType.APPLICATION_DATA:
				JSONObject jo= new JSONObject(body);
				int code = jo.getInt("code");
				String tsc = jo.getString("tsc");
				String icdata = jo.getString("data");
				
				if(tsc.equals("9000")){
					if(!icdata.isEmpty()){
						icdata = handshake.DecryptApplicationData(icdata);
						int endIndex = (int) Long.parseLong(icdata.substring(2, 4),16) *2;
						icdata = icdata.substring(4, endIndex);
						log.debug("解密后的卡片数据 {}", icdata);
					}
					if(handshake.load == null)
						handshake.load = new Load();
					data = handshake.load.readCard(code, icdata);
					if(data != null)
						data = handshake.schannelCmd(handshake.EncryptApplicationData(data),false);
				}
				break;
			case MessageType.CLIENT_CERTIFICATE:
				X509Certificate certs;
				certs = new X509Helper(body, false).getX509Certificate();
				data = handshake.readTerminalMasterKey(certs);
				break;
			case MessageType.CLIENT_FINISHED:
				data = handshake.handShakeFinish(body);
				break;
			case MessageType.CLIENT_MASTER_KEY:
				byte[] eMasterkey = new ftBaseFunc().ftHexStringToBytes(body);
				data = handshake.getTerminalSignature(eMasterkey);
				break;
			case MessageType.CLIENT_RANDOM:
				data = handshake.sendServerCertificate(body);
				break;
			case MessageType.CLIENT_SIGNATURE:
				byte[] terminalSign = new ftBaseFunc().ftHexStringToBytes(body);
				data = handshake.sendServerFinishMessage(terminalSign);
				break;
			case MessageType.ERROR_MESSAGE:
				break;
			case MessageType.REQUEST_CLIENT_CERTIFICATE:
				data = handshake.readTerminalCertificate();
				break;
			case MessageType.REQUEST_SERVER_CERTIFICATE:
				data = handshake.verifyServerCertificate();
				break;
			case MessageType.REQUEST_SERVER_FINISHED:
				data = handshake.sendReadTerminalFinishMessage();
				break;
			default:
				break;
			}
			response.setStatus(EloadCofing.SERVER_SUCCESS_STATUS);
			if (data == null)
			{
				response.setJsonContent(responseData(EloadCofing.SERVER_FAIL_STATUS, messageType+"请求异常"));
				response.send();
			}
			else
			{
				response.setJsonContent(responseData(EloadCofing.SERVER_SUCCESS_STATUS, data));
				response.send();
			}
		}
		else{
			response.setStatus(EloadCofing.SERVER_RUFUSED_STATUS);
			response.send();
		}
	}
	
	public String responseData(int code, String body)
	{
		JSONObject json = new JSONObject();
		json.put(EloadCofing.STATUS_CODE, code);
		json.put(EloadCofing.BODY, body);
		return json.toString();
	}
}
