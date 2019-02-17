package cn.webank.blockchain.protocol;

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
public class CreateVirtualAccountArgsInnerObj1 implements IArgs {
    /**
     * first name
     */
    @IndexField(index = 0)
    private String firstName;

    /**
     * last_name
     */
    @IndexField(index = 1)
    private String lastName;

    /**
     * |证件类型 01:护照;02:驾照
     */
    @IndexField(index = 2)
    private String idType;

    /**
     * 证件号码
     */
    @IndexField(index = 3)
    private String userId;

    /**
     * 用户绑定的银行卡号
     */
    @IndexField(index = 4)
    private String userCard;
}
