package cn.webank.blockchain.result;

import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
public class SetExchangeRateResult {

    /*
     * result 为0表明成功
     */
    public static final Integer STATUS_SUCC = 0;

    /*
     * 是否成功
     */
    boolean isSucc = false;
}
