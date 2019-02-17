package cn.webank.blockchain.impl.wallet;

import cn.webank.blockchain.api.wallet.AcquirerBankClient;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.protocol.DoTransactionArgs;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionReceiptResult;
import org.springframework.stereotype.Component;

//import org.json.JSONObject;


/**
 * Created by junqizhang on 2017/5/19.
 */
@Component("wallet.AcquirerBankClientImpl")
public class AcquirerBankClientImpl extends BaseIssuingBankClientImpl implements AcquirerBankClient {

    public ResponseStruct<TransactionReceiptResult> doTransaction(DoTransactionArgs transactionArgs) throws Exception{

        return this.writeBlockChainAndGetResult(
                this.DoTransactionArgsAddress,
                DoTransactionArgs.class,
                transactionArgs
        );
    }
}
