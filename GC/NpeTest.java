public class NpeTest {
    public static String getString() {
        return Math.random() > 0.5 ? "value" : null;
    }

    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        long duration = 60_000; // one minute
        int npeCount = 0;

        while (System.currentTimeMillis() - startTime < duration) {
            try {
                String s = getString();
                int length = s.length(); // probably trigger NPE
            } catch (NullPointerException e) {
                npeCount++;
            }
        }
        System.out.println("Java NPE次数: " + npeCount);
    }
}