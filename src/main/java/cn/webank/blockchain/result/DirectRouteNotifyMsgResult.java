package cn.webank.blockchain.result;

import cn.webank.blockchain.spi.common.dto.IResult;
import lombok.Data;

/**
 * Created by junqizhang on 15/08/2017.
 */
@Data
public class DirectRouteNotifyMsgResult implements IResult {

    /*
     * 错误信息
     */
    protected String message;
    /*
     * 错误码：返回0表明成功收到通知；其他表明异常情况.
     */
    private Integer errorCode;

    @Override
    public Integer getErrorCode() {
        return null;
    }

    @Override
    public void setErrorCode(Integer _errorCode) {
        errorCode = _errorCode;
    }
}
