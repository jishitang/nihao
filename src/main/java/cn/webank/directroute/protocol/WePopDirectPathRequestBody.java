package cn.webank.directroute.protocol;

import cn.webank.blockchain.constants.DirectRouteMsgType;
import cn.webank.blockchain.spi.common.dto.IArgs;
import lombok.Data;

@Data
public class WePopDirectPathRequestBody implements IArgs {

    protected DirectRouteMsgType msgType;

    protected String msgBody;

    protected String message;
}