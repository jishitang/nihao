package cn.webank.blockchain.spi.common.vo;

import com.google.common.base.Optional;
import lombok.Data;

import java.io.Serializable;


@Data
public class BlockChainResponseVO<T> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 返回模型
     */
    private Optional<T> domain;

    /**
     * 返回码
     */
    private Integer returnCode;
    /**
     * 返回信息
     */
    private String returnMsg;

    private BlockChainResponseVO() {
    }

    public static <T> BlockChainResponseVO<T> of(Integer returnCode, String message, T object) {
        BlockChainResponseVO<T> vo = new BlockChainResponseVO<>();
        vo.setReturnCode(returnCode);
        vo.setReturnMsg(message);
        vo.setDomain(Optional.fromNullable(object));
        return vo;
    }

    public boolean isSuccess() {
        return Integer.valueOf(0).equals(returnCode);
    }

}
