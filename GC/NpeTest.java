public class NpeTest {
    public static String getString() {
        return Math.random() > 0.5 ? "value" : null;
    }

    public static void main(String[] args) {        
        long startTime = System.currentTimeMillis();
        long duration = 60_000; // One minute
        long npeCount = 0;
        long totalCalls = 0;
        int progressBarWidth = 50;

        while (System.currentTimeMillis() - startTime < duration) {
            // Calculate and display progress
            long elapsed = System.currentTimeMillis() - startTime;
            int progress = (int)((elapsed * progressBarWidth) / duration);
            int percent = (int)((elapsed * 100) / duration);
            
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
            
            // Run NPE test
            for (int i = 0; i < 10000; i++) {
                try {
                    String s = getString();
                    int length = s.length(); // May trigger NPE
                    totalCalls++;
                } catch (NullPointerException e) {
                    npeCount++;
                    totalCalls++;
                }
            }
        }
        
        // Clear progress bar line and move to next line
        System.out.print("\r" + " ".repeat(progressBarWidth + 10) + "\r");
        
        double npePerMinute = npeCount;
        double npePercentage = (double) npeCount / totalCalls * 100;
        
        // Output the results in a table format
        System.out.println("+-------------------------+------------------------+");
        System.out.println("| Metric                  | Value                  |");
        System.out.println("+-------------------------+------------------------+");
        System.out.printf("| Total Calls             | %,22d |\n", totalCalls);
        System.out.printf("| NPE Count               | %,22d |\n", npeCount);
        System.out.printf("| NPE per Minute          | %,22.0f |\n", npePerMinute);
        System.out.printf("| NPE Percentage          | %,21.3f%% |\n", npePercentage);
        System.out.println("+-------------------------+------------------------+");
    }
}