package cn.webank.blockchain.impl.payment;

/**
 * Created by suyuhui on 17/5/24.
 */
public interface AuthGenerateService {
    String generateCode(String userAccount);
}
