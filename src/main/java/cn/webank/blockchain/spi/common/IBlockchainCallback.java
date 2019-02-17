package cn.webank.blockchain.spi.common;

public interface IBlockchainCallback<T> {

    void onResult(T result);

    void onException(Exception e);
}
