public class Probe {
    static { System.load(System.getProperty("sportsauce.probe.library")); }
    private static native String loadFramework(String path);
    public static void main(String[] args) {
        java.awt.Toolkit.getDefaultToolkit();
        System.out.println(loadFramework(args[0]));
        System.exit(0);
    }
}
