package cn.webank.blockchain.spi.common.dto.args.DoAccountTransaction;

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
public class DoAccountTransactionArgsInnerObj1 implements IArgs {
    /**
     * 交易流水号
     */
    @IndexField(index = 0)
    private String orgId;

    /**
     * 发卡行用户在链上唯一标示，要求在发卡行内唯一（可用卡号的md5值），在链下可以与用户的卡号进行映射即可
     */
    @IndexField(index = 1)
    private String userAccount;

    /**
     * 发卡行的用户设置的密码
     */
    @IndexField(index = 2)
    private String accPwd;
}
