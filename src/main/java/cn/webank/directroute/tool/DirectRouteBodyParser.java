package cn.webank.directroute.tool;

import cn.webank.blockchain.utils.JsonMapper;
import cn.webank.directroute.protocol.WePopDirectPathRequestBody;

/**
 * Created by junqizhang on 13/07/2017.
 */
public class DirectRouteBodyParser {

    static protected JsonMapper jsonMapper = JsonMapper.nonEmptyMapper();

    static public <T> String serialize(T object) {
        return jsonMapper.toJson(object);
    }

    static public <T> T deserializeG(String requestBody, Class<T> resultClazz) {
        return jsonMapper.fromJson(requestBody, resultClazz);
    }

    static public <T> T deserialize(String requestBody, Class<T> resultClazz) {

        WePopDirectPathRequestBody wePopDirectPathRequestBody = jsonMapper.fromJson(requestBody, WePopDirectPathRequestBody.class);
        return jsonMapper.fromJson(wePopDirectPathRequestBody.getMsgBody(), resultClazz);
    }
}
