package cn.webank.blockchain.protocol;

import cn.webank.blockchain.constants.CurrencyType;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import lombok.Data;

/**
 * Created by junqizhang on 13/06/2017.
 */
@Data
@BlockChainDTO(bindType = BlockChainDTO.BindTypeEnum.List)
public class GetClearingStatusResult {

    /*
     * 是否清算完成
     */
//    boolean isFinish = false;

    /*
     * 清算结果状态, 1：清算完成
     */
    @IndexField(index = 0)
    private Integer status;

    /*
     * 境外合作行的机构id，用于区分不同境外合作行的清算
     * 例如为港中银的钱包清算，为BOCHK
     */
    @IndexField(index = 1)
    private String walletOwnerOrg;

    /*
     * lastCheckCode对应的场次所有订单累加的人民币金额，精度到分
     * 为场次中所有订单的rmbAmt的和
     */
    @IndexField(index = 2)
    private Integer totalRmbAmt;

    /*
     * lastCheckCode对应的场次所有订单累加的外币金额，精度到分
     * 为场次中所有订单的totalTxAmt的和
     */
    @IndexField(index = 3)
    private Integer totalTxAmt;

    /*
     * 币种
     */
    @IndexField(index = 4)
    private Integer currency;

    /*
     * 清算完成后，WeBank备付金余额(单位，RMB)
     */
    @IndexField(index = 5)
    private Integer wbBalance;

    /*
     * 清算行成功清算的时间戳
     */
    @IndexField(index = 6)
    private Long timestamp;

    /*
     ******************************************************************
     * 下面的内容接口使用者无需关注
     ******************************************************************
     */

    public CurrencyType getCurrency() {
        return CurrencyType.getType(this.currency);
    }

    /*
     * 方便函数
     */
    public boolean isFinish() {
        if (ClearingStatusType.CHECK_CODE_CLEARING_COMPLETE.equals(this.getStatus())) {
            return true;
        } else {
            return false;
        }
    }

    public enum ClearingStatusType {

        /* result 为0表明清算未完成 */
        CHECK_CODE_CLEARING_NOT_COMPLETE(0),

        /* result 为1表明清算完成 */
        CHECK_CODE_CLEARING_COMPLETE(1);

        private Integer value;

        ClearingStatusType(Integer index) {
            this.value = index;
        }

        public int getValue()
        {
        	return value;
        }
        public boolean equals(final Integer targetvalue) {
            if (targetvalue == value) {
                return true;
            } else {
                return false;
            }
        }
    }
}
