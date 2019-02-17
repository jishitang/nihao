package cn.webank.blockchain.impl.wallet;

import cn.webank.blockchain.api.wallet.IssuingBankClient;
import cn.webank.blockchain.impl.base.BaseIssuingBankClientImpl;
import cn.webank.blockchain.protocol.GetAccountBalanceResult;
import cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction.DoAccountTransactionArgs;
import cn.webank.blockchain.spi.common.dto.args.IssuingBankUserAccountArgs;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionReceiptResult;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * Created by junqizhang on 2017/5/19.
 */
@Component("wallet.IssuingBankClientImpl")
public class IssuingBankClientImpl extends BaseIssuingBankClientImpl implements IssuingBankClient {

    /*
     * 用户充值提现请求上链
     */
    public ResponseStruct<TransactionReceiptResult> doAccountTransaction(DoAccountTransactionArgs transactionArgs) {

        return this.writeBlockChainAndGetResult(
                DoAccountTransactionArgsAddress,
                DoAccountTransactionArgs.class,
                transactionArgs
        );
    }

    public ResponseStruct<GetAccountBalanceResult> getAccountBalance(IssuingBankUserAccountArgs transactionArgs) {

        ResponseStruct<ArrayList> responseStruct = this.query(
                GetAccountBalanceArgsAddress,
                IssuingBankUserAccountArgs.class,
                transactionArgs,
                ArrayList.class
        );
        logger.info("response ：" + responseStruct);

        GetAccountBalanceResult result = null;
        if (0 == responseStruct.getErrorCode()
                && 2 == responseStruct.getResult().size()) {

            result = new GetAccountBalanceResult();
            result.setCurrency(Integer.parseInt((String) responseStruct.getResult().get(0)));
            result.setBalance(Integer.parseInt((String) responseStruct.getResult().get(1)));
        }

        ResponseStruct<GetAccountBalanceResult> specificResponseStruct = new ResponseStruct<>(responseStruct);
        specificResponseStruct.setResult(result);
        return specificResponseStruct;
    }

    //    public String getTransList(CreateVirtualAccountArgs transactionArgs) {
//
//        PostData bcMessage = ethbuilder.buildRequest(
//                CreateVirtualAccountsAddress,
//                CreateVirtualAccountArgs.class,
//                transactionArgs
//        );
//        logger.info("protocol : {}", bcMessage);
//
//        //convert java object to JSON format
//        String requestJson = gson.toJson(bcMessage);
//        RestClient httpClient = new RestClient(uniFrontSvrUrl);
//        String respJson = httpClient.post("", requestJson);
//        logger.info("response ：" + respJson);
//        BCResponseMessage<Integer> respMsg = gson.fromJson(respJson, BCResponseMessage.class);
//        HashData<Integer> respData = respMsg.getResp_data().getData();
//
//        return respJson;
////        return new ClearingStatusResponse(respData, respJson);
//    }
}
