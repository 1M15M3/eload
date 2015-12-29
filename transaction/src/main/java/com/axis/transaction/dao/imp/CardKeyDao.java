package com.axis.transaction.dao.imp;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;

import com.axis.common.Log;
import com.axis.common.log.LogWrapper;
import com.axis.transaction.bean.CardMasterKey;
import com.axis.transaction.connection.C3P0ConnectionManager;
import com.axis.transaction.constant.DBTable;
import com.axis.transaction.dao.ICardKeyDao;

public class CardKeyDao implements ICardKeyDao{
	private static final LogWrapper log = Log.get();
	
	public CardMasterKey getCardMK(String cardNO) {
		CardMasterKey cmk = null;
		if(cardNO != null){
			
			Connection conn = C3P0ConnectionManager.getInstance().getConnection();
            QueryRunner runner = new QueryRunner();
            ResultSetHandler<CardMasterKey> resultSetHandler = new BeanHandler<CardMasterKey>(CardMasterKey.class);  
            String sql = "select * from "+DBTable.TBL_CARD_MASTER_KEY+" ca where ca.rid=? and ca.certPubkeyIndex";
            Object[] params={cardNO};
            try {
				cmk = (CardMasterKey) runner.query(conn, sql, resultSetHandler,params);
			} catch (SQLException e) {
				com.axis.common.Log.error(e);
			} finally{
				
				DbUtils.closeQuietly(conn);
			}
			
		}
		return cmk;
	}

	public boolean setCardMK(CardMasterKey cmk) {
		// TODO Auto-generated method stub
		return false;
	}

}
