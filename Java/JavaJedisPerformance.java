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

    private static void notPipelined() {
        Jedis jedis = new Jedis("localhost");
        for (int j = 0; j < 5; j++) {
            long startTime = System.currentTimeMillis();
            for(int i= 0; i < N; i++) {
                jedis.set("foo", "bar");
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime-startTime) + "ms");
        }
    }

    private static void allPipelined() {
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

    private static void partPipelined() {
        Jedis jedis = new Jedis("localhost");
        for (int j = 0; j < 10; j++) {
            long startTime = System.currentTimeMillis();
            Pipeline p = jedis.pipelined();
            for(int i= 0; i < N; i += 1000) {
                for (int x = 0; x < 1000; x++) {
                    p.set("foo", "bar");
                }
                p.syncAndReturnAll();
            }
            long endTime = System.currentTimeMillis();
            System.out.println("Total execution time: " + (endTime-startTime) + "ms");
        }
    }

    public static void main(String[] args) {
        System.out.println("Not pipelined:");
        notPipelined();
        System.out.println("All pipelined:");
        allPipelined();
        System.out.println("Part pipelined:");
        partPipelined();
    }
}
