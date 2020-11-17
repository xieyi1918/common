import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;


public class JsonUtil {
    private JsonUtil(){

    }

    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        try {
            str2Map("{\"key\":\"value\"}");
        } catch (IOException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
    }

    public static Map<String,Object> str2Map(String str) throws IOException {
        return mapper.readValue(str, LinkedHashMap.class);
    }

    public static <T> Object str2Object(String str, TypeReference<T> reference){
        try {
            return mapper.readValue(str, reference);
        } catch (IOException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
        return null;
    }

    public static String obj2Str(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
//            log.error(ExceptionUtil.getExceptionStack(e));
        }
        return null;
    }

}
