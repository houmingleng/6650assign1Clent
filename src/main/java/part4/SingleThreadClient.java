package part4;

import io.swagger.client.ApiClient;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class SingleThreadClient {

    private static String IPAddress;
    private static final String dayID = "1";
    private static final String seasonID = "2022";
    private static Integer numSkiers;
    private static Integer numThreads;
    private static Integer numLifts;
    private static Integer time;
    private static Integer resortID;
    public static void main(String[] args) throws InterruptedException {
        System.out.println("*********************************************************");
        System.out.println("Client starts...");
        System.out.println("*********************************************************");
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);
        IPAddress = "http://localhost:8080/upiServer_war_exploded/";
        numThreads = 32;
        numSkiers = 100000;
        numLifts = 40;
        time = 360;
        resortID = 10;
        int singleThreadCalls = 1000;
        int totalCalls = 2000000;
        int endLatch = 1;


        long start = System.currentTimeMillis();
        int currentCalls = 0;
        ApiClient apiClient = new ApiClient();
        CountDownLatch latch = new CountDownLatch(endLatch);
        CountDownLatch totalLatch = new CountDownLatch(endLatch);
        new Thread(new SingleThread(singleThreadCalls,numSkiers,numLifts,time,success,failure,IPAddress,dayID,seasonID,resortID,latch, totalLatch)).start();
        latch.await();
        long end = System.currentTimeMillis();
        long wallTime = end - start;
        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("Data for SingleThread");
        System.out.println("*********************************************************");
        System.out.println("Target server IP is: " + IPAddress);
        System.out.println("Number of created threads: 1");
        System.out.println("Number of requests: 10000");
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests :" + success.get());
        System.out.println("Number of failed requests :" + failure.get());
        System.out.println("Total wall time: " + wallTime);
        System.out.println( "Throughput: " + (int)((success.get() + failure.get()) /
                (double)(wallTime / 1000) )+ " POST requests/second");
    }
}