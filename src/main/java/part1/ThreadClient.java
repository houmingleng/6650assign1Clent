package part1;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
        AtomicInteger finished = new AtomicInteger(0);
        //IPAddress = "34.215.195.155:8080/upiServlet_war/";
        IPAddress="http://localhost:8080/cs6650assign2RabiitMQ_war_exploded/skiers/";
        numThreads = 32;
        numLifts = 40;
        //numRuns = cmdParser.numRuns;
        numSkiers = 100000;

        long start = System.currentTimeMillis();
        int numReq1 =  1000,
                begin = 1,
                finial = 360,
                //endLatch = 1,
                totalReq = 200000;

        // phase1
        ThreadPoolExecutor es = new ThreadPoolExecutor(32,32,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        ThreadPoolExecutor es2 = new ThreadPoolExecutor(32,32,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        ThreadPoolExecutor es3 = new ThreadPoolExecutor(80,80,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        ThreadPoolExecutor es4 = new ThreadPoolExecutor(80,(totalReq-32000)/numReq1,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
//        MiddleWare middleWare = new MiddleWare(totalReq,IPAddress, resortID, dayID, seasonID,
//                numSkiers, begin, finial, numLifts, success, failure,es);
        CountDownLatch latch = new CountDownLatch(32);
        AtomicInteger successproduce = new AtomicInteger(0);

        int producernumber = 1;
        int comsumernumber = 32;
       ThreadPoll threadPoll = new ThreadPoll(32*1000,IPAddress,resortID,
                dayID,seasonID,numSkiers,begin, finial,numLifts, success,failure,es,es2,finished,latch,producernumber,comsumernumber,successproduce);
        threadPoll.beginPhase();
        //latch.await();
        while(success.get()+failure.get() < 32*1000);
        long end = System.currentTimeMillis();
        long currs = (end-start)/1000;
        System.out.println("************phase1 take : "+(end-start)/1000 +" S"+"**************************************");
        System.out.println("*********************************************************");
        latch = new CountDownLatch(totalReq/1000 - 32);
         producernumber = 42;
         comsumernumber = (totalReq-1000*32)/1000;
        ThreadPoll threadPoll2 = new ThreadPoll(totalReq ,IPAddress,resortID,
                dayID,seasonID,numSkiers,begin, finial,numLifts, success,failure,es3,es4,finished,latch,producernumber,comsumernumber,successproduce);
        threadPoll2.beginPhase();

        while(success.get()+failure.get()<totalReq);

       end = System.currentTimeMillis();
        long wallTime = end - start;
        System.out.println("************phase2 take : "+((end-start)/1000-currs) +" S"+"**************************************");
        System.out.println("*********************************************************");

        System.out.println("*********************************************************");
        System.out.println("End......");
        System.out.println("*********************************************************");
        System.out.println("*********************************************************");
        System.out.println("Number of successful requests :" + success.get());
        System.out.println("Number of failed requests :" + failure.get());
        System.out.println("Total wall time: " + wallTime);
        System.out.println( "Throughput: " + (int)((success.get() + failure.get()) /
                (double)(wallTime / 1000) )+ " POST requests/second");
    }
}