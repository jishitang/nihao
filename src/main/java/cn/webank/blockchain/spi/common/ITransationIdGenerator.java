package cn.webank.blockchain.spi.common;

public interface ITransationIdGenerator {
    String generateUnquieId();

    long generateUniqueIdLong();
}
