import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisDataException;
import redis.clients.util.SafeEncoder;
import java.util.List;

public class JavaJedisPerformance {
    public static final int N = 1000000;

    public static void main(String[] args) {
        Jedis jedis = new Jedis("localhost");
        for (int j = 0; j < 10; j++) {
            long startTime = System.currentTimeMillis();
            Pipeline p = jedis.pipelined();
            for(int i= 0; i < N; i++) {
                p.set("foo", "bar");
            }
            List<Object> results = p.syncAndReturnAll();
            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime-startTime) + "ms");
        }
    }
}
