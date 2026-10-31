package com.markettwits.selfupdater.thirdparty.api;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/** Test-only JNI harness with the same class and callback ABI as the Kotlin driver. */
public class SparkleJniDriver {
    private final BlockingQueue<Long> done = new ArrayBlockingQueue<>(4);
    private volatile int code = -1;
    private volatile String version;
    private volatile String build;
    private native void nativeBind();
    private native void nativeRequest(long id, int command);
    private void onNativeReply(long id, int code, String version, String build, String description) {
        this.code = code;
        this.version = version;
        this.build = build;
        done.offer(id);
    }
    private void requestAndWait(long id, int command) throws Exception {
        nativeRequest(id, command);
        Long result = done.poll(25, TimeUnit.SECONDS);
        if (result == null || result != id) throw new IllegalStateException("JNI callback timed out or had the wrong id");
    }
    public static void main(String[] args) throws Exception {
        System.load(System.getProperty("sportsauce.probe.library"));
        java.awt.Toolkit.getDefaultToolkit();
        SparkleJniDriver driver = new SparkleJniDriver();
        driver.nativeBind();
        driver.requestAndWait(42, 1);
        System.out.println("JNI result: " + driver.code + " version=" + driver.version + " build=" + driver.build);
        int expected = Integer.parseInt(args[0]);
        boolean valid = driver.code == expected && (expected != 1 ||
            ("99.0.0".equals(driver.version) && "999999".equals(driver.build)));
        if (valid && args.length > 1 && args[1].equals("interactive")) {
            driver.requestAndWait(43, 2);
            if (driver.code != 4) throw new IllegalStateException("Interactive update did not start");
            driver.requestAndWait(44, 3);
            if (driver.code != 4) throw new IllegalStateException("Interactive session was not active");
            System.out.println("JNI interactive session active");
        }
        System.exit(valid ? 0 : 1);
    }
}
