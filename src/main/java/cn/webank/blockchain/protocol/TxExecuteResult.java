package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.dto.IResult;
import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
public class TxExecuteResult implements IResult {

    /*
     * 写区块链是否成功
     */
    boolean isSucc = false;

    /*
     * 错误码
     * new added, you can ignore this.
     */
    Integer errorCode;

    /*
     * 写区块链请求返回的交易hash
     */
    String txHash;
}
