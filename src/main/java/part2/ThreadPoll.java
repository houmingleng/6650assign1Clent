package part2;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoll {
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
    AtomicInteger finished;
    ThreadPoolExecutor es;
    ThreadPoolExecutor es2;
    CountDownLatch latch;
       int producernumber;
       int comsumernumber;
    AtomicInteger successproduce;
    CopyOnWriteArrayList<List<Record>> recordList;
    private LinkedBlockingQueue queue;
    public ThreadPoll(Integer totalReq, String IPAddress, Integer resortID, String dayID, String seasonID,
                      Integer numSkier, Integer startTime, Integer endTime, Integer numLifts, AtomicInteger successCallCount,
                      AtomicInteger failCallCount, ThreadPoolExecutor es,ThreadPoolExecutor es2,AtomicInteger finished,CountDownLatch latch,int producernumber,int comsumernumber,AtomicInteger successproduce, CopyOnWriteArrayList<List<Record>> recordList) {
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
        this.es2 = es2;
        this.finished = finished;
        this.latch =latch;
        this.producernumber= producernumber;
        this.comsumernumber= comsumernumber;
        this.successproduce = successproduce;
        this.recordList= recordList;
    }

    public void initilPhase() throws InterruptedException{


    }
    public  void beginPhase() throws InterruptedException{

        this.queue = new LinkedBlockingQueue(2000);
        for (int i = 0; i < producernumber; i++) {
            Producer producer = new Producer(totalReq,IPAddress,resortID,dayID,seasonID,1,numSkier, startTime,endTime, numLifts, successCallCount,queue ,1000, successproduce );
            es2.execute(producer);
        }


        for(int i = 0; i < comsumernumber; i++) {

            es.execute(new Consumer(1000,IPAddress,queue,1000,successCallCount,failCallCount,finished,recordList));

        }
        es.shutdown();
        es2.shutdown();;


    }

}
