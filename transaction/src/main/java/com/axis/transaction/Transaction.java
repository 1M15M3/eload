package com.axis.transaction;

public class Transaction {

	public String load(int code ,String stc, String data){
		String result = null;
		if(stc == "9000"){
			Load l = new Load();
			result = l.readCard(code,data);
		}
		return result;
	}
}
