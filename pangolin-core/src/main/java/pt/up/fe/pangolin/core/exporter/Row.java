package pt.up.fe.pangolin.core.exporter;

public class Row {
    private final String packageName;
    private final String className;
    private final String methodName;
    private final String line;
    private final String score;

    public Row(String packageName, String className, String methodName, String line, String score) {
        this.packageName = packageName;
        this.className = className;
        this.methodName = methodName;
        this.line = line;
        this.score = score;
    }

    @Override
    public String toString() {
        return    this.packageName + ","
                + this.className + ","
                + this.methodName + ","
                + this.line + ","
                + this.score + "\n";
    }
}
