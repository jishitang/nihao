package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IResult;
import lombok.Data;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
public class GetMerchantInfoResult implements IResult {

    /*
     * 是否合法二维码
     * 如果为非法二维码，message会提示非法。
     * 0表示合法； 非0表示非法
     */
    protected Integer errorCode;

    /*
     * 聚合支付服务商id
     */
    protected String merchantSvcId;

    /*
     * 聚合支付服务商name
     */
    protected String merchantSvcName;

    /*
     * 商户id
     */
    protected String merchantId;

    /*
     * 商户name
     */
    protected String merchantName;

    /*
     * 额外信息
     */
    protected String extraData;

    /*
     * 错误信息
     */
    protected String message;
}
