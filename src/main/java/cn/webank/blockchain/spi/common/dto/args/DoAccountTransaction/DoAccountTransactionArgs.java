package cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 *
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class DoAccountTransactionArgs implements IArgs {

    //充值
    public static final Integer TX_TYPE_TOP_UP = 21;
    //提现
    public static final Integer TX_TYPE_WITHDRAW = 22;

    /**
     * 交易发生时间YYYYMMDD
     */
    @IndexField(index = 0)
    private String txDate;

    @IndexField(index = 1)
    private DoAccountTransactionArgsInnerObj1 innerObj1;

    @IndexField(index = 2)
    private DoAccountTransactionArgsInnerObj2 innerObj2;

    @IndexField(index = 3)
    private DoAccountTransactionArgsInnerObj3 innerObj3;

    /**
     * 时间戳，精确到毫秒
     */
    @IndexField(index = 4)
    private Long txTime;

    public DoAccountTransactionArgs() {
        innerObj1 = new DoAccountTransactionArgsInnerObj1();
        innerObj2 = new DoAccountTransactionArgsInnerObj2();
        innerObj3 = new DoAccountTransactionArgsInnerObj3();
        this.setAccPwd("");
    }

    public void setOrgId(String orgId) {
        innerObj1.setOrgId(orgId);
    }

    public void setAccount(String userAccount) {
        innerObj1.setUserAccount(userAccount);
    }

    /**
     * 业务流水号，由发卡行生成的一个全局唯一的业务流水号。
     * 不能重复，重复的值会返回失败，错误码2014。
     */
    public void setBizNo(String bizNo) {
        innerObj2.setBizNo(bizNo);
    }

    public void setOrgBizNo(String orgBizNo) {
        innerObj2.setOrgBizNo(orgBizNo);
    }

    public void setTxType(Integer txType) {
        innerObj3.setTxType(txType);
    }

    public void setIsStrike(Integer isStrike) {
        innerObj3.setIsStrike(isStrike);
    }

    public void setCurrency(CurrencyType currencyType) {
        innerObj3.setCurrency(currencyType.getValue());
    }

    public void setAmount(Integer amount) {
        innerObj3.setAmount(amount);
    }

    /*
     * 合约里面不再使用
     */
    private void setAccPwd(String accPwd) {
        innerObj1.setAccPwd(accPwd);
    }
}
