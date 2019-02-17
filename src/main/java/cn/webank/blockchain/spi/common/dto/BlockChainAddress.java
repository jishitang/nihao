package cn.webank.blockchain.spi.common.dto;

import lombok.Getter;

@Getter
public class BlockChainAddress {

    private String contractAddress;
    private String method;
    private String function;
//	private BlockChainAddress(){}

    private BlockChainAddress(String contractAddress, String method, String function) {
        this.contractAddress = contractAddress;
        this.method = method;
        this.function = function;
    }

    public static BlockChainAddress of(String contractAddress, String method, String function) {
        return new BlockChainAddress(contractAddress, method, function);
    }


}
