package cn.webank.blockchain.protocol;

import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
///@Data
public class DoTransactionResult extends TxExecuteResult {

    /*
     * result 为0表明记账成功
     */
    public static final Integer STATUS_DO_TRANSACTION_SUCC = 0;

    /*
     * 这一笔交易在链上被打上的场切码
     */
    Integer checkCode = 0; 
}
