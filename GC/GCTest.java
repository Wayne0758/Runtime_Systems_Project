public class GCTest {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start < 60_000) {
            for (int i = 0; i < 10_000; i++) {
                String s = new String("Object-" + i); // build short-lived object
            }
        }
    }
}