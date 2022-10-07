package part4;

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
    private int totalCalls;
    private int skierID;
    private int resortID;
    private int liftID;
    private String dayID;
    private String seasonID;
    private int time;
    private String IPAddress;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;
    CountDownLatch nextLatch;
    CountDownLatch totalLatch;
    Gson gson = new Gson();
   // CountDownLatch totalLatch = new CountDownLatch(1000);
    public SingleThread(int totalCalls,int skierID, int liftID, int time,
                        AtomicInteger successCallCount, AtomicInteger failCallCount, String IPAddress
            , String dayID,String seasonID, int resortID, CountDownLatch nextLatch,
    CountDownLatch totalLatch) {
        this.totalCalls = totalCalls;
        this.skierID = skierID;
        this.liftID = liftID;
        this.time = time;
        this.IPAddress = IPAddress;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;
        this.dayID = dayID;
        this.seasonID = seasonID;
        this.resortID = resortID;

    }

    @Override
    public void run() {
        String url = "http://localhost:8080/untitled_war_exploded/";
        SkiersApi api = new SkiersApi();
        api.getApiClient().setBasePath(url).setReadTimeout(100);
        int curSuccess = 0;
        int curFail = 0;
        int i=0;
        long start=0,end=0;
        while(i< totalCalls){
            Integer currSkierId = ThreadLocalRandom.current().nextInt(1,skierID+1);
            Integer currResortId = ThreadLocalRandom.current().nextInt(1,resortID+1);
            Integer currLiftId = ThreadLocalRandom.current().nextInt(1,liftID+1);
            Integer currTime = ThreadLocalRandom.current().nextInt(1,time+1);
            int retry = 0;

            while(retry < ALLOW_ATTEMPTS_NUM ){
                try {
                     start = System.currentTimeMillis();
                LiftRide currLiftRide = new LiftRide();
                currLiftRide.setTime(currTime);
                currLiftRide.setLiftID(currLiftId);
                ApiResponse<Void> response = api.writeNewLiftRideWithHttpInfo(currLiftRide,currResortId, seasonID,dayID , currSkierId);
                if(response.getStatusCode() == HTTP_OK || response.getStatusCode() == HTTP_CREATED){

                    curSuccess++;
                    System.out.println("ok");
                    end = System.currentTimeMillis();

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
        nextLatch.countDown();
        totalLatch.countDown();
        successCallCount.getAndAdd(curSuccess);
        failCallCount.getAndAdd(curFail);
        System.out.println(" end: "+ (end - start));
    }
}
