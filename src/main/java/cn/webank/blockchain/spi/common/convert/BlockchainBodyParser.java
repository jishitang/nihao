package cn.webank.blockchain.spi.common.convert;

import cn.webank.blockchain.spi.common.annoation.BlockChainDTO;
import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.exception.IndexFieldParsingFailedException;
import cn.webank.blockchain.spi.common.reflect.FileldDefinitionParser;
import cn.webank.blockchain.utils.JsonMapper;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

@Component
public class BlockchainBodyParser {

    public static final JsonMapper jsonMapper = new JsonMapper(Include.NON_DEFAULT);

    public <T> Object toMessageObject(Class<? extends T> domainClazz, T domainObj) {
        if (isBindByList(domainClazz)) {
            return convertToArrayList(domainClazz, domainObj);
        }
        return domainObj;
    }


    public <T> T toDomainObject(Class<T> domainClazz, String json) {

        try {
            if (!isBindByList(domainClazz)) {
                T domain = jsonMapper.getMapper().readValue(json, domainClazz);
                return domain;
            } else {
                JsonMapper jsonMapper = new JsonMapper(Include.NON_DEFAULT);
                ArrayList<String> objectAsList = Lists.newArrayList();
                JsonNode arrNode = jsonMapper.getMapper().readTree(json);
                if (arrNode.isArray()) {
                    for (JsonNode jsonNode : arrNode) {
                        objectAsList.add(jsonNode.toString());
                    }
                }
                return convertFromArrayList(domainClazz, objectAsList);
            }
        } catch (IOException e) {
            throw Throwables.propagate(e);
        }

    }


    private <T> T convertFromArrayList(Class<T> domainClazz, ArrayList<String> jsonElements) {

        if (jsonElements == null || ((List<?>) jsonElements).size() == 0) {
            return null;
        }

        List<String> msgJsonAsList = jsonElements;

        List<Pair<Field, IndexField>> csvFields;
        try {
            csvFields = FileldDefinitionParser.getInstance().parseIndexFileds(domainClazz);

            T result = domainClazz.newInstance();
            for (int i = 0; i < csvFields.size(); i++) {
                Pair<Field, IndexField> pair = csvFields.get(i);
                Field field = pair.getLeft();
                field.setAccessible(true);
                String json = msgJsonAsList.get(i);
                Object value = toDomainObject(field.getType(), json);
                //Object value = conversionService.convert(object, field.getType());
                field.set(result, value);
            }
            return result;

        } catch (IndexFieldParsingFailedException | InstantiationException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }


    private <T> ArrayList<?> convertToArrayList(Class<? extends T> clazz, T instance) {
        if (instance == null) {
            return new ArrayList<>(0);
        }
        // only list object can access this code
        try {
            List<Pair<Field, IndexField>> csvFields = FileldDefinitionParser.getInstance().parseIndexFileds(clazz);
            ArrayList<Object> result = new ArrayList<>();
            for (int i = 0; i < csvFields.size(); i++) {
                Pair<Field, IndexField> pair = csvFields.get(i);
                Field field = pair.getLeft();
                field.setAccessible(true);
                Object value = field.get(instance);
                if (value == null) {
                    result.add(i, null);
                } else {
                    Object object = toMessageObject(value.getClass(), value);
                    result.add(i, object);
                }

            }
            return result;
        } catch (IndexFieldParsingFailedException | IllegalArgumentException | IllegalAccessException e) {
            throw Throwables.propagate(e);
        }
    }


    private <T> boolean isBindByList(Class<T> clazz) {
        Annotation[] annotations = clazz.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof BlockChainDTO) {
                return BlockChainDTO.BindTypeEnum.List.equals(((BlockChainDTO) annotation).bindType());
            }
        }
        return false;

    }

}
