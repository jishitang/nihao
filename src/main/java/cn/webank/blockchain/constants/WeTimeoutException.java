package cn.webank.blockchain.constants;

import lombok.Getter;
import org.springframework.web.client.ResourceAccessException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * Created by junqizhang on 20/06/2017.
 */
public class WeTimeoutException extends Exception {

    @Getter
    private ResourceAccessException baseResourceAccessEx;

    @Getter
    private Integer errorCode;

    public WeTimeoutException(ResourceAccessException baseResourceAccessEx) {
        this.baseResourceAccessEx = baseResourceAccessEx;
        Throwable baseEx = baseResourceAccessEx.getCause();

        if (baseEx.getClass() == ConnectException.class) {
            /*
             * 连接失败
             */
            this.errorCode = ErrorCode.UNI_FRONT_SERVER_NOT_FOUND;
        } else if (baseEx.getClass() == SocketTimeoutException.class) {
            /*
             * 能请求通前置，但是前置没有返回,会抛出SocketTimeoutException异常
             * 如何模拟：在前置回包处设断点或者在前置svr的机器上配置iptables使其丢弃回包。
             */
            this.errorCode = ErrorCode.UNI_FRONT_SERVER_RESPONSE_TIMEOUT;
        } else {
            /*
             * 其他未知异常
             */
            this.errorCode = ErrorCode.RESOURCE_ACCESS_EXCEPTION_UNKNOWN;
        }
    }

}
