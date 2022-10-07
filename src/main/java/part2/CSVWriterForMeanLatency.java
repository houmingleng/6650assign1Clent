package part2;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
public class CSVWriterForMeanLatency {
    public static void write(List<int[]> list, String numThreads) {
        File csvFile = new File("recordsMeanLat" + "_" + numThreads + ".csv");
        if(csvFile.exists()) csvFile.delete();
        try{
            csvFile.createNewFile();
            FileWriter writer = new FileWriter(csvFile);
            writer.append("Second, MeanLatency\n");
            writer.flush();
            for(int[] arr : list) {
                writer.append(String.valueOf(arr[0]));
                writer.append(",");
                writer.append(String.valueOf(arr[1]));
                writer.append("\n");
                writer.flush();
            }
            writer.close();
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}
