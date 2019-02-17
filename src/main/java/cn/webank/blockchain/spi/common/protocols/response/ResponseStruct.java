package cn.webank.blockchain.spi.common.protocols.response;

import lombok.Data;

/**
 * Created by junqizhang on 2017/5/22.
 */
@Data
public class ResponseStruct<T> {

    protected Integer errorCode;

    protected T result;

    protected String message;

    protected String rawResponse;

    public <F> ResponseStruct(ResponseStruct<F> responseStruct) {
        this.errorCode = responseStruct.getErrorCode();
        this.message = responseStruct.getMessage();
        this.rawResponse = responseStruct.getRawResponse();
    }

    public ResponseStruct(Integer errorCode, T result, String message, String rawResponse) {
        this.errorCode = errorCode;
        this.result = result;
        this.message = message;
        this.rawResponse = rawResponse;
    }

    public ResponseStruct() {
    }

//    void setErrorCode(Integer errorCode) {
//        this.errorCode = Integer.parseInt()
//    }
}
