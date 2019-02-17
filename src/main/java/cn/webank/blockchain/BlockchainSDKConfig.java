package cn.webank.blockchain;

import cn.webank.blockchain.spi.common.convert.RequestBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by junqizhang on 08/06/2017.
 */

@Configuration
@ComponentScan(basePackageClasses = {RequestBuilder.class})
public class BlockchainSDKConfig {

    @Bean
    public RequestBuilder requestBuilder() {
        return new RequestBuilder();
    }
}
