package cn.webank.blockchain.spi.common.annoation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Retention(RUNTIME)
public @interface BlockChainDTO {

    /**
     * object or list
     *
     * @return
     */
    public BindTypeEnum bindType();

    enum BindTypeEnum {
        List, Object;
    }
}
