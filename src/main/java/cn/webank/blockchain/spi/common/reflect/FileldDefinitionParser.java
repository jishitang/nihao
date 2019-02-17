package cn.webank.blockchain.spi.common.reflect;

import cn.webank.blockchain.spi.common.annoation.IndexField;
import cn.webank.blockchain.spi.common.exception.IndexFieldParsingFailedException;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;


public class FileldDefinitionParser {

    private static final Map<Class<?>, List<Pair<Field, IndexField>>> clazz2CSVFiles =
            Maps.newConcurrentMap();
    private static final FileldDefinitionParser instance = new FileldDefinitionParser();

    private FileldDefinitionParser() {
    }

    public static final FileldDefinitionParser getInstance() {
        return instance;
    }

    public List<Pair<Field, IndexField>> parseIndexFileds(Class<?> clazz) throws IndexFieldParsingFailedException {
        if (clazz2CSVFiles.containsKey(clazz)) {
            return clazz2CSVFiles.get(clazz);
        } else {
            List<Pair<Field, IndexField>> parsedResult = parseIndexFiledsDirect(clazz);
            clazz2CSVFiles.put(clazz, parsedResult);
            return parsedResult;
        }

    }

    private List<Pair<Field, IndexField>> parseIndexFiledsDirect(Class<?> clazz) throws IndexFieldParsingFailedException {
        int countOfFields = 0;
        Map<Integer, Pair<Field, IndexField>> csvFileds = Maps.newHashMap();
        Field[] filed = clazz.getDeclaredFields();
        for (Field field : filed) {
            Optional<IndexField> csvFieldOptional = findCSVFieldAnnotation(field);
            if (csvFieldOptional.isPresent()) {
                countOfFields++;
                IndexField csvField = csvFieldOptional.get();
                csvFileds.put(csvField.index(), ImmutablePair.of(field, csvField));
            }
        }
        if (csvFileds.size() != countOfFields) {
            throw new IndexFieldParsingFailedException("ERROR parse the CSV define for class " + clazz);
        }

        List<Pair<Field, IndexField>> list = Lists.newArrayList();
        for (int i = 0; i < countOfFields; i++) {
            Pair<Field, IndexField> element = csvFileds.get(i);
            if (element == null) {
                throw new IndexFieldParsingFailedException("ERROR parse the CSV define for class " + clazz);
            }
            list.add(i, element);
        }

        return list;
    }

    private Optional<IndexField> findCSVFieldAnnotation(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation instanceof IndexField) {
                return Optional.of((IndexField) annotation);
            }
        }
        return Optional.absent();
    }

}
