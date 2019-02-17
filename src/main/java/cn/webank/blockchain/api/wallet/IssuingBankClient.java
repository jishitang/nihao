package cn.webank.blockchain.api.wallet;

import cn.webank.blockchain.protocol.CreateVirtualAccountArgs;
import cn.webank.blockchain.protocol.CreateVirtualAccountStatus;
import cn.webank.blockchain.protocol.GetAccountBalanceResult;
import cn.webank.blockchain.protocol.GetClearingStatusResult;
import cn.webank.blockchain.protocol.GetClearingTransArgs;
import cn.webank.blockchain.protocol.GetClearingTransResult;
import cn.webank.blockchain.protocol.GetTransListArgs;
import cn.webank.blockchain.protocol.GetTransListResult;
import cn.webank.blockchain.protocol.IsAccountExistResult;
import cn.webank.blockchain.spi.common.dto.args.IssuingBankUserAccountArgs;
import cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction.DoAccountTransactionArgs;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;
import cn.webank.blockchain.spi.common.protocols.response.TransactionReceiptResult;

/**
 * Created by junqizhang on 21/06/2017.
 */
public interface IssuingBankClient {

    ResponseStruct<CreateVirtualAccountStatus> createVirtualAccount(CreateVirtualAccountArgs transactionArgs);

    ResponseStruct<IsAccountExistResult> isAccountExists(IssuingBankUserAccountArgs transactionArgs);

    /*
     * 用户充值提现请求上链
     */
    ResponseStruct<TransactionReceiptResult> doAccountTransaction(DoAccountTransactionArgs transactionArgs);

    ResponseStruct<GetAccountBalanceResult> getAccountBalance(IssuingBankUserAccountArgs transactionArgs);

    ResponseStruct<GetClearingStatusResult> getClearingStatus(String orgId, Integer checkCode);

    ResponseStruct<GetClearingTransResult> getClearingTrans(GetClearingTransArgs transactionArgs);

    ResponseStruct<GetTransListResult> getTransList(GetTransListArgs transactionArgs);
}
