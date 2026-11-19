import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class PackagedProbe {
    static { System.load(System.getProperty("sportsauce.probe.library")); }
    private static final CountDownLatch resultReady = new CountDownLatch(1);
    private static volatile String result;
    private static native String start();
    public static void onResult(String value) { result = value; resultReady.countDown(); }
    public static void main(String[] args) throws Exception {
        java.awt.Toolkit.getDefaultToolkit();
        String status = start();
        System.out.println(status);
        if (!status.equals("Sparkle updater started in packaged JVM") || !resultReady.await(25, TimeUnit.SECONDS)) System.exit(1);
        System.out.println(result);
        System.exit(args.length == 1 && args[0].equals(result) ? 0 : 1);
    }
}
