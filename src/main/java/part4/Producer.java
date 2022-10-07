package part4;

import com.google.gson.Gson;
import module.ReqObject;

import java.util.Queue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable{
    private Queue<ReqObject> queue;
    private int maxCapacity;
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
    int maxNumber ;
    Gson gson = new Gson();

    public Producer(Queue queue, int maxCapacity, int totalCalls, int skierID, int liftID, int time,
                    AtomicInteger successCallCount, AtomicInteger failCallCount, String IPAddress
            , String dayID, String seasonID, int resortID) {
        this.queue = queue;
        this.maxCapacity = maxCapacity;
    }

    public Producer(Queue<ReqObject> queue, int maxCapacity, int skierID, int resortID, int liftID, String dayID,
                    String seasonID, int time, String IPAddress, AtomicInteger successCallCount, AtomicInteger failCallCount, int totalCalls) {
        this.queue = queue;
        this.maxCapacity = maxCapacity;
        this.skierID = skierID;
        this.resortID = resortID;
        this.liftID = liftID;
        this.dayID = dayID;
        this.seasonID = seasonID;
        this.time = time;
        this.IPAddress = IPAddress;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;
        this.maxNumber = totalCalls;
    }

    @Override
    public void run() {


            synchronized (queue) {
                while (queue.size() == maxCapacity ) {
                    try {
                        wait();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        ;
                    }
                    if (queue.size() == 0) {
                        queue.notifyAll();
                    }
                    Integer currSkierId = ThreadLocalRandom.current().nextInt(1, skierID + 1);
                    Integer currResortId = ThreadLocalRandom.current().nextInt(1, resortID + 1);
                    Integer currLiftId = ThreadLocalRandom.current().nextInt(1, liftID + 1);
                    Integer currTime = ThreadLocalRandom.current().nextInt(1, time + 1);

                    queue.offer(new ReqObject(currSkierId, currResortId, currLiftId, dayID, seasonID, currTime));
                }

            }

        }

}
