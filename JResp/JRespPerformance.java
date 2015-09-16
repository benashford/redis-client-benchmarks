import jresp.Client;
import jresp.ConnectionException;

import jresp.pool.Pool;
import jresp.pool.SingleCommandConnection;

import jresp.protocol.Ary;
import jresp.protocol.BulkStr;
import jresp.protocol.RespType;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.CountDownLatch;

public class JRespPerformance {
    public static final int N = 1000000;

    public static void main(String[] args) {
        try {
            Client client = new Client("localhost", 6379);
            Pool pool = new Pool(client);
            SingleCommandConnection con = pool.getShared();

            for (int j = 0; j < 10; j++) {
                long startTime = System.currentTimeMillis();
                CountDownLatch latch = new CountDownLatch(N);
                for (int i = 0; i < N; i++) {
                    con.write(new Ary(BulkStr.get("SET"),
                                      new BulkStr("foo"),
                                      new BulkStr("bar")),
                              resp -> latch.countDown());
                }
                latch.await();
                long endTime = System.currentTimeMillis();
                System.out.println("Total execution time: " + (endTime - startTime) + "ms");
            }
        } catch (IOException|ConnectionException|InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
