package pipline;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.*;

@Slf4j
public class InitRunner implements CommandLineRunner {
    private String host = "localhost";
    private Integer port = 6379;

    @Override
    public void run(String... args) throws ParseException {
    }

    public void piplineForWriteToRedis(Map<String, String> keyValueEntry, Map<String, Integer> keyExpireEntry) {
        Set<HostAndPort> nodes = new HashSet<>();
        nodes.add(new HostAndPort(host, port));
        JedisCluster jc = new JedisCluster(nodes);
        PiplineUtil util = PiplineUtil.pipelined(jc);
        util.refreshCluster();
        try {
            for (Map.Entry<String, String> map : keyValueEntry.entrySet()) {
                util.set(map.getKey(), map.getValue());
            }
            for (Map.Entry<String, Integer> map : keyExpireEntry.entrySet()) {
                util.expire(map.getKey(), map.getValue());
            }
        } finally {
            util.close();
            try {
                jc.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Map<String, String> getObjectFieldAndValue(Object object) {
        Class<?> clz = object.getClass();
        // 获取实体类的所有属性，返回Field数组
        Field[] fields = clz.getDeclaredFields();
        Map<String, String> map = new LinkedHashMap<>();
        for (Field field : fields) {
            Method m;
            try {
                m = (Method) object.getClass().getMethod("get" + getMethodName(field.getName()));
                // 如果type是包装类型，则前面包含"class "，后面跟类名，基本数据类型则类型为自身
                if (Objects.equals(field.getGenericType().toString(), "class java.lang.String")) {
                    String val = (String) m.invoke(object);
                    if (val == null) {
                        map.put(field.getName().toLowerCase(), "");
                    } else {
                        map.put(field.getName().toLowerCase(), val);
                    }
                }
                if (Objects.equals(field.getGenericType().toString(), "class java.math.BigDecimal")) {
                    BigDecimal val = (BigDecimal) m.invoke(object);
                    if (val == null) {
                        map.put(field.getName().toLowerCase(), "");
                    } else {
                        map.put(field.getName().toLowerCase(), val.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    private static String getMethodName(String fildeName) {
        byte[] items = fildeName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }

}
