package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;
import lombok.ToString;

/**
 * 记账 : 消费交易上链
 */
@ToString
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class DoTransactionArgs implements IArgs {
    /**
     * 交易日期, YYYYMMDD
     */
    @IndexField(index = 0)
    private String date;

    @IndexField(index = 1)
    private DoTransactionStringArgsInner stringArgsInner;

    @IndexField(index = 2)
    private DoTransactionIntArgsInner intArgsInner;

    public DoTransactionArgs() {
        this.stringArgsInner = new DoTransactionStringArgsInner();
        this.intArgsInner = new DoTransactionIntArgsInner();
    }

    public void setIbBizNo(String ibBizNo) {
        stringArgsInner.setIbBizNo(ibBizNo);
    }

    public void setAbBizNo(String abBizNo) {
        stringArgsInner.setAbBizNo(abBizNo);
    }

    public void setMsBizNo(String msBizNo) {
        stringArgsInner.setMsBizNo(msBizNo);
    }

    public void setOrgBizNo(String orgBizNo) {
        stringArgsInner.setOrgBizNo(orgBizNo);
    }

    public void setUserAccount(String userAccount) {
        stringArgsInner.setUserAccount(userAccount);
    }

    public void setUserName(String userName) {
        stringArgsInner.setUserName(userName);
    }

    public void setFromOrgId(String fromOrgId) {
        stringArgsInner.setFromOrgId(fromOrgId);
    }

    public void setToOrgId(String toOrgId) {
        stringArgsInner.setToOrgId(toOrgId);
    }

    public void setUserAppIp(String userAppIp) {
        stringArgsInner.setUserAppIp(userAppIp);
    }

    public void setReportCity(String reportCity) {
        stringArgsInner.setReportCity(reportCity);
    }

    public void setMerchantSvcId(String merchantSvcId) {
        stringArgsInner.setMerchantSvcId(merchantSvcId);
    }

    public void setMerchantSvcName(String merchantSvcName) {
        stringArgsInner.setMerchantSvcName(merchantSvcName);
    }

    public void setMerchantId(String merchantId) {
        stringArgsInner.setMerchantId(merchantId);
    }

    public void setMerchantName(String merchantName) {
        stringArgsInner.setMerchantName(merchantName);
    }

    public void setMccCode(String mccCode) {
        stringArgsInner.setMccCode(mccCode);
    }

    public void setTxTime(String txTime) {
        stringArgsInner.setTxTime(txTime);
    }

    public void setMerchantOrgCode(String merchantOrgCode) {
        stringArgsInner.setMerchantOrgCode(merchantOrgCode);
    }
    
    public void setStoreId(String storeId)
    {
    	stringArgsInner.setStoreId(storeId);
    }

    public void setIsRefunded(Integer isRefunded) {
        intArgsInner.setIsRefunded(isRefunded);
    }

    public void setRmbAmt(Integer rmbAmt) {
        intArgsInner.setRmbAmt(rmbAmt);
    }

    public void setTxAmt(Integer txAmt) {
        intArgsInner.setTxAmt(txAmt);
    }

    public void setCurrency(Integer currency) {
        intArgsInner.setCurrency(currency);
    }

    public void setFxRate(Integer fxRate) {
        intArgsInner.setFxRate(fxRate);
    }

    public void setExTimestamp(Long exTimestamp) {
        intArgsInner.setExTimestamp(exTimestamp);
    }

    public void setTxType(Integer txType) {
        intArgsInner.setTxType(txType);
    }

    public void setTradeType(Integer tradeType) {
        intArgsInner.setTradeType(tradeType);
    }

    public void setCheckCode(Integer checkCode) {
        intArgsInner.setCheckCode(checkCode);
    }

    public void setTxStatus(Integer txStatus) {
    	intArgsInner.setTxStatus(txStatus);
    }
    public void setRemedyMarkWithCheckCode(Integer checkCode) {
        intArgsInner.setExtInt0(1);
        intArgsInner.setCheckCode(checkCode);
    }
}
