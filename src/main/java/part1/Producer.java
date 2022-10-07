package part1;

import io.swagger.client.model.LiftRide;
import module.ReqObject;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {

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
    private LinkedBlockingQueue<ReqObject> blockingQueue;
    private int maxCapacity;
    AtomicInteger successproduce;

    public Producer(Integer totalRideLiftCall, String IPAddress, Integer resortID, String dayID, String seasonID, Integer startSkierID,
                    Integer endSkierID, Integer startTime, Integer endTime, Integer numLifts, AtomicInteger successCallCount,
                    LinkedBlockingQueue<ReqObject> blockingQueue, int maxCapacity, AtomicInteger successproduce) {
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
        this.blockingQueue = blockingQueue;
        this.maxCapacity = maxCapacity;
        this.successproduce= successproduce;
    }

    @Override
    public void run() {
        int i=0;

        while(successproduce.get()< totalRideLiftCall) {


            Integer curTime = ThreadLocalRandom.current().nextInt(startTime, endTime);
            Integer curSkierId = ThreadLocalRandom.current().nextInt(startSkierID, endSkierID);
            Integer currResortId = ThreadLocalRandom.current().nextInt(1, resortID + 1);
            Integer currLiftId = ThreadLocalRandom.current().nextInt(1, numLifts + 1);

            LiftRide curLiftRide = generateLiftRideCall(curTime, currLiftId, currResortId);

            ReqObject reqObject = new ReqObject(curSkierId, currResortId, currLiftId, dayID, seasonID, curTime, curLiftRide);
            if (this.blockingQueue != null && reqObject != null) {
                try {
                    blockingQueue.put(reqObject);
                    successproduce.getAndAdd(1);
                  //  System.out.println(successproduce.get());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }

        }

    }
    private LiftRide generateLiftRideCall(Integer time, Integer liftId, Integer waitTime) {
        LiftRide liftRide = new LiftRide();
        liftRide.setLiftID(liftId);
        liftRide.setTime(time);
        return liftRide;
    }
}
