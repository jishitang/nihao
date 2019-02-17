package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.BlockChainDTO.BindTypeEnum;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;
import lombok.EqualsAndHashCode;

//import cn.webank.blockchain.spi.common.protocols.response.GetTransList;

/**
 * Created by junqizhang on 01/06/2017.
 */

@Data
@BlockChainDTO(bindType = BindTypeEnum.Object)
@EqualsAndHashCode(callSuper = false)
public class GetMerchantInfoArgs extends DirectRouteBaseMsgArgs implements IArgs {

    /*
     * 主扫的时候扫码获取到的二维码的字符串
     */
    protected String merchantQrCode;
}
