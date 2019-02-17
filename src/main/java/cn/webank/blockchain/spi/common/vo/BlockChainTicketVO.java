package cn.webank.blockchain.spi.common.vo;

import lombok.Getter;

@Getter
public class BlockChainTicketVO {

    private int sleepTime;
    private String hash;

    private BlockChainTicketVO() {
    }

    public static final BlockChainTicketVO of(String hash) {
        return of(hash, 3000);
    }

    public static final BlockChainTicketVO of(String hash, int sleepTime) {
        BlockChainTicketVO blockChainTicketVO = new BlockChainTicketVO();
        blockChainTicketVO.sleepTime = sleepTime;
        blockChainTicketVO.hash = hash;
        return blockChainTicketVO;
    }

}
