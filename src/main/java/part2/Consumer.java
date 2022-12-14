package part2;

import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import module.ReqObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private static final Integer HTTP_OK = 200;
    private static final Integer HTTP_CREATED = 201;
    private static final Integer ALLOW_ATTEMPTS_NUM = 5;
    private Integer totalRideLiftCall;
    private String IPAddress;

    private LinkedBlockingQueue<ReqObject> blockingQueue;
    private int maxCapacity;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;
    AtomicInteger finished;

    CopyOnWriteArrayList<List<Record>> recordList;
    public Consumer(Integer totalRideLiftCall, String IPAddress, LinkedBlockingQueue<ReqObject> blockingQueue,
                    int maxCapacity, AtomicInteger successCallCount, AtomicInteger failCallCount, AtomicInteger finished, CopyOnWriteArrayList<List<Record>> recordList) {
        this.totalRideLiftCall = totalRideLiftCall;
        this.IPAddress = IPAddress;
        this.blockingQueue = blockingQueue;
        this.maxCapacity = maxCapacity;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;
        this.finished = finished;
        this.recordList= recordList;
    }

    @Override
    public void run() {

      String url = "http://34.219.153.177:8080/cs6650assign2RabiitMQ_war/skiers/";
      //  String url = "http://localhost:8080/s6650assign2RabiitMQ_war_exploded/skiers/";
       // String url = "http://localhost:8080/upiServlet_war_exploded/";
        //http://localhost:8080/upiServlet_war_exploded/
        SkiersApi api = new SkiersApi();
        api.getApiClient().setBasePath(url).setReadTimeout(100);
        List<Record> record = new ArrayList<>();
        int curSuccess = 0;
        int curFail = 0;
        int i = 0;
        while (i < totalRideLiftCall) {

                int retry = 0;
            ReqObject product = null;
            try {
                product = blockingQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            while (retry < ALLOW_ATTEMPTS_NUM ) {
                    try {
                        long start = System.currentTimeMillis();
                        ApiResponse<Void> res = api.writeNewLiftRideWithHttpInfo(product.getCurLiftRide(), product.getResortID(), product.getSeasonID(),
                                product.getDayID(), product.getSkierID());
                        if (res.getStatusCode() == HTTP_OK || res.getStatusCode() == HTTP_CREATED) {
                            curSuccess++;
                            long end = System.currentTimeMillis();
                            // System.out.println("                 "+successCallCount.get()+failCallCount.get());
                            record.add(new Record(start, end, end - start, "POST", res.getStatusCode()));
                            break;
                        }
                    } catch (ApiException e) {
                        retry++;
                        e.printStackTrace();
                    }
                }
                if (retry == ALLOW_ATTEMPTS_NUM) {
                    curFail++;
                }
                i++;

        }
            successCallCount.getAndAdd(curSuccess);
            failCallCount.getAndAdd(curFail);
            finished.getAndAdd(1);
        recordList.add(new ArrayList<>(record));
          //  latch.countDown();
        }
    }

