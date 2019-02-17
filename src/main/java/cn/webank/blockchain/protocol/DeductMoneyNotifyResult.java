package cn.webank.blockchain.protocol;

import cn.webank.blockchain.spi.common.dto.IResult;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by junqizhang on 08/07/2017.
 */
public class DeductMoneyNotifyResult implements IResult {

    /*
     * 错误信息
     */
    @Getter
    @Setter
    protected String message;
    /*
     * 返回0表明成功收到通知；其他表明异常情况.
     */
    @Getter
    @Setter
    private Integer errorCode;
}
