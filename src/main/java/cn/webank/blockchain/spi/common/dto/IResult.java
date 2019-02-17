package cn.webank.blockchain.spi.common.dto;

///import lombok.Data;

/**
 * 结果
 */
public interface IResult {
    Integer getErrorCode();
    void setErrorCode(Integer errorCode);
}
