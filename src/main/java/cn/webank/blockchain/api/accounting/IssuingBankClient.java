package cn.webank.blockchain.api.accounting;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.protocol.*;
import cn.webank.blockchain.result.DirectRouteNotifyMsgResult;
import cn.webank.blockchain.result.RecordAccountsPayableResult;
import cn.webank.blockchain.spi.common.protocols.response.ResponseStruct;

/**
 * Created by junqizhang on 21/06/2017.
 * 发卡行使用的client
 */
public interface IssuingBankClient extends BaseClient {

    /**
     * 主扫模式1使用，获取商户信息
     *
     * @param qrCodeString 扫描商户的二维码获取到的字符串
     * @return 商户的信息
     */
    ResponseStruct<GetMerchantInfoResult> getMerchantInfoByQrCodeString(String qrCodeString);

    /**
     * 主扫模式1使用，下单接口
     * 用户输入完金额后，点击确认按钮时调用
     *
     * @param placeOrderArgs 订单信息
     * @return 下单结果
     */
    ResponseStruct<PlaceOrderResult> placeOrder(PlaceOrderArgs placeOrderArgs);

    /**
     * 发卡行从链上获取汇率
     *
     * @param currencyType 币种
     * @return 汇率结构体
     */
    ResponseStruct<ExchangeRate> getExchangeRate(CurrencyType currencyType);

    /**
     * 发卡行向收单行发起记账，这笔订单会上链
     * 同步调用，会阻塞，等待区块链出块成功或者超时为止
     * 返回的状态可以分为3类：成功，失败，未知
     * <p>
     * 成功的情况：
     * 0 == responseStruct.getErrorCode() && 0 == responseStruct.getResult().getErrorCode()
     * <p>
     * 未知的情况：
     * <p>
     * 失败的情况：
     * 其他
     */
    ResponseStruct<RecordAccountsPayableResult> recordAccountsPayable(RecordAccountsPayableArgs arg);

    /**
     * 境外合作行扣款过程的发生了任何错误，都通过这个接口通知到WeBank
     *
     * @param arg 境外合作行扣款过程的发生的错误
     * @return 通知的返回: 0 == DirectRouteNotifyMsgResult.errorCode表明成功接收通知
     */
    ResponseStruct<DirectRouteNotifyMsgResult> reportFailedStatus(ReportFailedStatusArgs arg);

    /**
     * 生成支付授权码
     * 使用linux /dev/urandom，根据一定的规则生成支付授权码
     *
     * @param userAccount 用户在链上的账号
     * @return 展现到用户app的支付授权码，需要客服端转换为二维码和条形码的样式
     */
    String generatePaymentAuthCode(String userAccount);

//    ResponseStruct<TransactionQueryResult> getTransList(GetTransListArgs transactionArgs);
}
