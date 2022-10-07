package part4;

import module.ReqObject;

import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable{
    private Queue<ReqObject> queue;
    private int maxCapacity;
    private static final Integer HTTP_OK = 200;
    private static final Integer HTTP_CREATED = 201;
    private static final Integer ALLOW_ATTEMPTS_NUM = 5;
    private int totalCalls;
    private String url;
    AtomicInteger successCallCount;
    AtomicInteger failCallCount;

    public Consumer(Queue<ReqObject> queue,int maxCapacity, int totalCalls, AtomicInteger successCallCount, AtomicInteger failCallCount) {
        this.queue = queue;
        this.maxCapacity = maxCapacity;
        this.totalCalls = totalCalls;
        this.successCallCount = successCallCount;
        this.failCallCount = failCallCount;
    }

    @Override
    public void run() {
        int i = 0;
        int curSuccess = 0;
        int curFail = 0;

        while (queue.isEmpty() ) {
            try {

                wait();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (queue.size() == maxCapacity) {
            queue.notifyAll();
        }

        ReqObject product = queue.poll();
//        String url = "http://localhost:8080/untitled_war_exploded/";


//        SkiersApi api = new SkiersApi();
//        api.getApiClient().setBasePath(url).setReadTimeout(100);
//        int retry = 0;
//         curSuccess = 0;
//         curFail = 0;
//        while(retry < ALLOW_ATTEMPTS_NUM){
//            try {
//                LiftRide currLiftRide = new LiftRide();
//                currLiftRide.setTime(product.getTime());
//                currLiftRide.setLiftID(product.getLiftID());
//                ApiResponse<Void> response = api.writeNewLiftRideWithHttpInfo(currLiftRide, product.getResortID(),
//                        product.getSeasonID(),product.getDayID() ,product.getSkierID());
//                if(response.getStatusCode() == HTTP_OK || response.getStatusCode() == HTTP_CREATED){
//                    curSuccess++;
//                }
//            } catch (ApiException e) {
//                retry++;
//                e.printStackTrace();
//            }
//        }
//        if (retry == ALLOW_ATTEMPTS_NUM) {
//            curFail++;
//        }
//        i++;

        curSuccess++;
        System.out.println(product);
        successCallCount.getAndAdd(curSuccess);
        failCallCount.getAndAdd(curFail);

    }
}
