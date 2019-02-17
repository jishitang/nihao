package cn.webank.blockchain.spi.common.dto.args;

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
public class DateArgsObj implements IArgs {
//	/**
//	 * 清算结果状态，1：完成
//	 */
//	public static final Integer STATUS_CLEARING_COMPLETE = Integer.valueOf(1);
//
    /**
     * 交易日期, YYYYMMDD
     */
    @IndexField(index = 0)
    private String date;

}
