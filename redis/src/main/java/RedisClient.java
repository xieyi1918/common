import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisClient {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public String get(String key){
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key,String value){
        redisTemplate.opsForValue().set(key,value);
    }
}
