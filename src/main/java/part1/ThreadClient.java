package part1;


import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadClient {
    private static String IPAddress;
    private static final Integer resortID = 1;
    private static final String dayID = "abc";
    private static final String seasonID = "summer";
    private static Integer numSkiers;
    private static Integer numThreads;
    private static Integer numLifts;
    private static Integer numRuns;
    public static void main(String[] args) throws InterruptedException {

        System.out.println("*********************************************************");
        System.out.println("Client starts...");
        System.out.println("*********************************************************");
        AtomicInteger success = new AtomicInteger(0);
        AtomicInteger failure = new AtomicInteger(0);
        IPAddress = "35.90.188.162:8080/upiServlet_war/";
        numThreads = 32;
        numLifts = 40;
        //numRuns = cmdParser.numRuns;
        numSkiers = 100000;

        long start = System.currentTimeMillis();
        int numReq1 =  1000,
                begin = 1,
                finial = 360,
                endLatch = 1,
                totalReq = 200000;
        CountDownLatch latch = new CountDownLatch(endLatch);
        CountDownLatch totalLatch = new CountDownLatch(endLatch);
        ThreadPoolExecutor es = new ThreadPoolExecutor(numThreads,2*numThreads,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        MiddleWare middleWare = new MiddleWare(totalReq,IPAddress, resortID, dayID, seasonID,
                numSkiers, begin, finial, numLifts, success, failure,es);
        middleWare.beginPhase();
        while(es.getPoolSize()!=0);

        long end = System.currentTimeMillis();
        long wallTime = end - start;
        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("*********************************************************");
        System.out.println("Target server IP is: " + IPAddress);
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests :" + success.get());
        System.out.println("Number of failed requests :" + failure.get());
        System.out.println("Total wall time: " + wallTime);
        System.out.println( "Throughput: " + (int)((success.get() + failure.get()) /
                (double)(wallTime / 1000) )+ " POST requests/second");
    }
}