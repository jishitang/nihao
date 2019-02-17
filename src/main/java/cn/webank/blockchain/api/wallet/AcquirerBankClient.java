package cn.webank.blockchain.api.wallet;

import cn.webank.blockchain.protocol.DoTransactionArgs;
import cn.webank.blockchain.protocol.DoTransactionResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;

/**
 * Created by junqizhang on 21/06/2017.
 */
public interface AcquirerBankClient {
    ResponseStruct<DoTransactionResult> doTransaction(String toOrgId, DoTransactionArgs transactionArgs) throws  Exception;
}
