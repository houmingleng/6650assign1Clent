package part1;

import com.google.gson.Gson;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
public class SingleThread implements Runnable{
    private static final Integer HTTP_OK = 200;
    private static final Integer HTTP_CREATED = 201;
    private static final Integer ALLOW_ATTEMPTS_NUM = 5;

    private Integer totalRideLiftCall;
    private String IPAddress;
    private Integer resortID;
    private String dayID;
    private String seasonID;
    private Integer startSkierID;
    private Integer endSkierID;
    private Integer startTime;
    private Integer endTime;
    private Integer numLifts;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;
    CountDownLatch nextLatch;
    CountDownLatch totalLatch;
    Gson gson = new Gson();
    public SingleThread(Integer totalRideLiftCall, String IPAddress, Integer resortID,
                        String dayID, String seasonID, Integer startSkierID, Integer endSkierID, Integer startTime,
                        Integer endTime, Integer numLifts,
                        AtomicInteger successCallCount, AtomicInteger failCallCount
                        ) {
        this.totalRideLiftCall = totalRideLiftCall;
        this.IPAddress = IPAddress;
        this.resortID = resortID;
        this.dayID = dayID;
        this.seasonID = seasonID;
        this.startSkierID = startSkierID;
        this.endSkierID = endSkierID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numLifts = numLifts;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;


    }
    @Override
    public void run() {
        String url = "35.90.188.162:8080/upiServlet_war/";
        SkiersApi api = new SkiersApi();
        api.getApiClient().setBasePath(url).setReadTimeout(100);
        int curSuccess = 0;
        int curFail = 0;
        int i = 0;
        while (i < totalRideLiftCall) {
            Integer curTime = ThreadLocalRandom.current().nextInt(startTime, endTime);
            Integer curSkierId = ThreadLocalRandom.current().nextInt(startSkierID, endSkierID);
            Integer currResortId = ThreadLocalRandom.current().nextInt(1,resortID+1);
            Integer currLiftId = ThreadLocalRandom.current().nextInt(1,numLifts+1);

            int retry = 0;

            while (retry < ALLOW_ATTEMPTS_NUM) {
                try {
                    long start = System.currentTimeMillis();
                    LiftRide curLiftRide = generateLiftRideCall(curTime, currLiftId, currResortId);
                    ApiResponse<Void> res = api.writeNewLiftRideWithHttpInfo(curLiftRide, resortID, seasonID,
                            dayID, curSkierId);
                    if (res.getStatusCode() == HTTP_OK || res.getStatusCode() == HTTP_CREATED) {
                        curSuccess++;
                        long end = System.currentTimeMillis();
                       // System.out.println(end - start);
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
    }

    private LiftRide generateLiftRideCall(Integer time, Integer liftId, Integer waitTime) {
        LiftRide liftRide = new LiftRide();
        liftRide.setLiftID(liftId);
        liftRide.setTime(time);
        return liftRide;
    }
}
