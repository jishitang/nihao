package cn.webank.common.conf;

/**
 * Created by junqizhang on 08/06/2017.
 */

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

public class ConfigProvider extends PropertyPlaceholderConfigurer {

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
        // 加载 <context:property-placeholder> 配置。【注意】配置可以加载多个文件，生成一个 Properties 对象！！！
        super.processProperties(beanFactoryToProcess, props);

        // 把占位符配置缓存起来，需要缓存的其他配置文件用<util:properties />方式加载，会缓存为 bean 实例。
        ConfigUtil.setAppProperties(props);
    }
}
