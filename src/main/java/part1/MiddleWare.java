package part1;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

public class MiddleWare {
    private Integer totalReq;

    private String IPAddress;
    private Integer resortID;
    private String dayID;
    private String seasonID;
    private Integer numSkier;
    private Integer startTime;
    private Integer endTime;
    private Integer numLifts;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;
    ExecutorService es;
    public MiddleWare(Integer totalReq, String IPAddress, Integer resortID, String dayID,
                      String seasonID, Integer numSkier, Integer startTime, Integer endTime, Integer numLifts,
                      AtomicInteger successCallCount, AtomicInteger failCallCount, ExecutorService es) {
        this.totalReq = totalReq;

        this.IPAddress = IPAddress;
        this.resortID = resortID;
        this.dayID = dayID;
        this.seasonID = seasonID;
        this.numSkier = numSkier;
        this.startTime = startTime;
        this.endTime = endTime;
        this.numLifts = numLifts;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;

        this.es = es;
    }
    public void beginPhase() throws InterruptedException{
        for(int i = 0; i < totalReq/1000; i++) {
            int startSkier = 1;
            int endSkier = numSkier;
            SingleThread t = new SingleThread(1000, IPAddress, resortID, dayID, seasonID, startSkier, endSkier,
                    startTime, endTime, numLifts, successCallCount, failCallCount
                    );
        es.submit(t);
        }
        es.shutdown();


    }
}
