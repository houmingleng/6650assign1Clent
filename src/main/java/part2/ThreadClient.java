package part2;


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
       //IPAddress = "http://54.212.214.84:8080/upiServlet_war/";
        //35.90.188.162
        IPAddress = "http://35.90.188.162:8080/upiServlet_war/";
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
        CopyOnWriteArrayList<List<Record>> recordList = new CopyOnWriteArrayList<>();
        ThreadPoolExecutor es = new ThreadPoolExecutor(numThreads,2*numThreads,1, TimeUnit.SECONDS,new LinkedBlockingDeque<Runnable>());
        MiddleWare middleWare = new MiddleWare(totalReq,IPAddress, resortID, dayID, seasonID,
                numSkiers, begin, finial, numLifts, success, failure,es,recordList);
        middleWare.beginPhase();

        while(es.getPoolSize()!=0);

        long end = System.currentTimeMillis();
        long wallTime = end - start;
        List<Record> outputList = new ArrayList<>();
        for(List<Record> list : recordList) {
            outputList.addAll(list);
        }
        CSVWriter.write(outputList, String.valueOf(numThreads));
        long totalTime = 0;
        outputList.sort(new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Long.compare(o1.getLatency(), o2.getLatency());
            }
        });

        long p99Time = outputList.get((int)(outputList.size() * 0.99)).getLatency();

        for(int i = 0; i <outputList.size(); i++) {
            totalTime += outputList.get(i).getLatency();
        }

        Collections.sort(outputList, new Comparator<Record>() {
            @Override
            public int compare(Record o1, Record o2) {
                return Long.compare(o1.getEndTime(), o2.getEndTime());
            }
        });
        List<int[]> meanLatList = new ArrayList<>();
        int n = outputList.size();
        long s = outputList.get(0).getEndTime(), cur = 0;
        int count = 1, i = 0, sec = 0;
        while(i < n) {
            if(outputList.get(i).getEndTime() - s <= 1000) {
                cur += outputList.get(i).getLatency();
                count++;
            } else {
                sec++;
                meanLatList.add(new int[]{sec, (int) (cur / count)});
                count = 1;
                s = outputList.get(i).getEndTime();
                cur = outputList.get(i).getLatency();
            }
            i++;
        }

        CSVWriterForMeanLatency.write(meanLatList, String.valueOf(numThreads));
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