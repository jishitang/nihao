package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

/**
 * 清算结果通知
 */
@Data
@BlockChainDTO(bindType = BindTypeEnum.List)
public class CreateVirtualAccountArgs implements IArgs {

    /**
     * 发卡行在链上的唯一标示，由区块链运营方提前分配好。
     */
    @IndexField(index = 0)
    private String orgId;

    /**
     * 发卡行用户在链上的唯一标示，要求在发卡行内唯一，在链下可以与用户的卡号进行映射即可
     * 不能重复，如果传入重复的值，则会返回失败
     * 可以用Wallet id 或者 卡号
     */
    @IndexField(index = 1)
    private String userAccount;

    /**
     * 发卡行用户的钱包交易密码，发卡行传入，可取用户输入的交易密码的md5值
     */
    @IndexField(index = 2)
    private String accPwd;

    /**
     * 用户账户币种
     */
    @IndexField(index = 3)
    private Integer currency;

    /**
     * 创建跨境支付账户的日期,YYYYMMDD
     */
    @IndexField(index = 4)
    private String currDate;

    @IndexField(index = 5)
    private CreateVirtualAccountArgsInnerObj1 innerObj1;

    public CreateVirtualAccountArgs() {
        innerObj1 = new CreateVirtualAccountArgsInnerObj1();
        this.setAccPwd("");
    }

    public void setFirstName(String firstName) {
        this.innerObj1.setFirstName(firstName);
    }

    public void setLastName(String lastName) {
        this.innerObj1.setLastName(lastName);
    }

    public void setIdType(String idType) {
        this.innerObj1.setIdType(idType);
    }

    public void setUserId(String userId) {
        this.innerObj1.setUserId(userId);
    }

    public void setUserCard(String userCard) {
        this.innerObj1.setUserCard(userCard);
    }

    private void setAccPwd(String accPwd) {
        this.accPwd = accPwd;
    }

    public void setCurrency(CurrencyType currencyType) {
        this.currency = currencyType.getValue();
    }
}
