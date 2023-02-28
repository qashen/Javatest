package siebel.integration.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PricingUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PricingUtil.class);

    private static final ObjectMapper mapper = new ObjectMapper()
            .setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"))
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    public static String convertObjectToJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting object to json {}", jsonError.getMessage());
            throw new RuntimeException("Obj_JSON_Err");
        }
    }

    public static <T> T convertJsonToObject(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting json to object {}", jsonError.getMessage());
            throw new RuntimeException("JSON_Obj_Err");
        }
    }

    public static <T> T convertJsonMapToObject(Map<String,Object> jsonMap, Class<T> clazz) {
        try {
            String json = convertObjectToJson(jsonMap);
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException jsonError) {
            LOGGER.error("Error converting json to object {}", jsonError.getMessage());
            throw new RuntimeException("JSONMap_Obj_Err");
        }
    }

    public static void setFields(Object from, Object to) {
        Field[] fields = from.getClass().getDeclaredFields();
        for (Field field : fields) {
            try {
                Field fieldFrom = from.getClass().getDeclaredField(field.getName());
                Object value = fieldFrom.get(from);
                to.getClass().getDeclaredField(field.getName()).set(to, value);

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                //e.printStackTrace();
            }
        }
    }

    public static void setFields(LinkedHashMap from, LinkedHashMap to) {
        from.forEach((key, value) -> {
            {
                to.put (key,value);
            }
        });
    }
}