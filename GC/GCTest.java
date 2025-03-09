import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.List;

public class GCTest {
    public static void main(String[] args) {
        // Get GC MXBeans
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        long totalGcCountBefore = 0;
        long totalGcTimeBefore = 0;
        
        // Record GC statistics at the beginning
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            totalGcCountBefore += gcBean.getCollectionCount();
            totalGcTimeBefore += gcBean.getCollectionTime();
        }
        
        long start = System.currentTimeMillis();
        long objectsCreated = 0;
        long testDuration = 60_000; // One minute
        int progressBarWidth = 50;
        
        // Run for one minute, creating many short-lived objects
        while (System.currentTimeMillis() - start < testDuration) {
            // Calculate and display progress
            long elapsed = System.currentTimeMillis() - start;
            int progress = (int)((elapsed * progressBarWidth) / testDuration);
            int percent = (int)((elapsed * 100) / testDuration);
            
            // Create progress bar string
            StringBuilder progressBar = new StringBuilder("\r[");
            for (int i = 0; i < progressBarWidth; i++) {
                if (i < progress) {
                    progressBar.append("=");
                } else if (i == progress) {
                    progressBar.append(">");
                } else {
                    progressBar.append(" ");
                }
            }
            progressBar.append("] ").append(percent).append("% ");
            
            // Display progress
            System.out.print(progressBar.toString());
            
            // Create objects
            for (int i = 0; i < 100_000; i++) {
                Object[] objects = new Object[100]; // Create array objects
                for (int j = 0; j < 100; j++) {
                    objects[j] = new String("Object-" + i + "-" + j); // Create string objects
                }
                objectsCreated += 100;
            }
        }
        
        // Clear progress bar line and move to next line
        System.out.print("\r" + " ".repeat(progressBarWidth + 10) + "\r");
        
        // Record GC statistics at the end
        long totalGcCountAfter = 0;
        long totalGcTimeAfter = 0;
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            totalGcCountAfter += gcBean.getCollectionCount();
            totalGcTimeAfter += gcBean.getCollectionTime();
        }
        
        // Calculate GC count and time
        long gcCount = totalGcCountAfter - totalGcCountBefore;
        long gcTimeMs = totalGcTimeAfter - totalGcTimeBefore;
        
        // Output the results in a table format
        System.out.println("+-------------------------+------------------------+");
        System.out.println("| Metric                  | Value                  |");
        System.out.println("+-------------------------+------------------------+");
        System.out.printf("| Objects Created         | %,22d |\n", objectsCreated);
        System.out.printf("| GC Count (per minute)   | %,22d |\n", gcCount);
        System.out.printf("| GC Time (ms per minute) | %,22d |\n", gcTimeMs);
        System.out.println("+-------------------------+------------------------+");
    }
}