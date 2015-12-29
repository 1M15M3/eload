/**
 * 
 */
package com.axis.security;

/**
 * @author axis
 *
 */
public class TerminalInfo {

	public static final String TERMINAL_DATA = "01";
	public static final String TERMINAL_FIRMWARE_VER = "02";
	public static final String PUBLIC_KEY_VER = "03";
	public static final String ICC_NO = "04";
	public static final String REVERSAL_FLAG = "05";
	public static final String TERMINAL_TYPE = "06";
	public static final String TERMINAL_ISSUER = "07";
	public static final String PIN_NO = "08";
	public static final String CA_SERIES_NO = "09";
	public static final String PLATFORM = "0A";
	public static final String TERMINAL_CERT_VALID_DATE = "0B";
	public static final String AID = "84";

	public static final String IS_REVERSAL = "31";

	public static final String NO_REVERSAL = "30";

	protected String terminalData;

	protected String terminalFirmwareVer;

	protected String publicKeyVer;

	protected String iccNo;

	protected String reversal;

	protected String terminalType;

	protected String terminalIssuer;

	protected String pinNo;

	protected String CASeriesNo;

	protected String platform;

	protected String terminalCertsValidDate;
	protected String icaid;
}
