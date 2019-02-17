package cn.webank.blockchain.spi.common;

import org.apache.logging.log4j.core.util.UuidUtil;
import org.springframework.stereotype.Service;

@Service
public class StubTransationIdGenerator implements ITransationIdGenerator {
    @Override
    public String generateUnquieId() {
        return UuidUtil.getTimeBasedUuid().toString().replaceAll("-", "");
    }

    @Override
    public long generateUniqueIdLong() {
        return System.currentTimeMillis();
    }
}
