package part4;

import io.swagger.client.ApiClient;
import module.ReqObject;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class Client {
    private static String IPAddress;
    private static final String dayID = "1";
    private static final String seasonID = "2022";
    private static Integer numSkiers;
    private static Integer numThreads;
    private static Integer numLifts;
    private static Integer time;
    private static Integer resortID;
    public static void main(String[] args) throws InterruptedException{
        System.out.println("*********************************************************");
        System.out.println("Client starts...");
        System.out.println("*********************************************************");
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);
        IPAddress = "http://localhost:8080/upiServer_war_exploded/";
        numThreads = 32;
        numSkiers = 100000;
        numLifts= 40;
        time = 360;
        resortID = 10;
        int singleThreadCalls = 1000;
        int totalCalls = 2000000;
        long start = System.currentTimeMillis();
        int currentCalls = 0;
        ApiClient apiClient = new ApiClient();
        int threadNeed = (int)Math.ceil(totalCalls/(double)singleThreadCalls) ;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads+1);
        Queue<ReqObject> q = new ArrayDeque<>();
       // Producer producer = new Producer(q,singleThreadCalls,numSkiers, resortID,numLifts,dayID,seasonID,time,IPAddress,success,failure,totalCalls);
        for (int i = 0; i < threadNeed; i++) {
           // executorService.submit(new Thread(producer));
//            executorService.submit(new Thread(new Consumer(numSkiers,singleThreadCalls,success,failure)));
           // executorService.submit(new Thread(new SingleThread(singleThreadCalls,numSkiers,numLifts,time,success,failure,IPAddress,dayID,seasonID,resortID)));
        }
        //new Thread(producer).start();
       // new Thread(new Consumer(q,numSkiers,singleThreadCalls,success,failure)).start();
       // new Thread(new SingleThread(singleThreadCalls,numSkiers,numLifts,time,success,failure,IPAddress,dayID,seasonID,resortID)).start();
        executorService.shutdown();
        long end = System.currentTimeMillis();
        long wallTime = end - start;
        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("Data for Client1");
        System.out.println("*********************************************************");
        System.out.println("Target server IP is: " + IPAddress);
        System.out.println("Number of created threads: " + numThreads);
        System.out.println("Number of skiers: " + numSkiers);
        System.out.println("Number of lifts: " + numLifts);
        System.out.println("Number of resort: " + resortID);
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests :" + success.get());
        System.out.println("Number of failed requests :" + failure.get());
        System.out.println("Total wall time: " + wallTime);
        System.out.println( "Throughput: " + (int)((success.get() + failure.get()) /
                (double)(wallTime / 1000) )+ " POST requests/second");
//        while (currentCalls< totalCalls  ){
//
//            SingleThread t = new SingleThread(singleThreadCalls,numSkiers,numLifts,time,success, failure,IPAddress,dayID,seasonID);
//            Thread thread = new Thread(t);
//            thread.start();
//        }
    }

}
