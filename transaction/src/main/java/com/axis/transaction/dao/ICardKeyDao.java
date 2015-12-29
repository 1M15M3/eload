package com.axis.transaction.dao;

import com.axis.transaction.bean.CardMasterKey;

public interface ICardKeyDao {

	/**
	 * 获取卡片主密钥
	 * @param cardNO 卡号
	 * @return 卡片主密钥 cardMasterKey 的对象
	 */
	public CardMasterKey getCardMK(String cardNO);
	
	/**
	 * 更新卡片主密钥
	 * @param cmk CardMasterKey对象
	 * @return true：更新成功，false：更新失败
	 */
	public boolean setCardMK(CardMasterKey cmk);
}
