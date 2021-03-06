import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;


public class ExceptionUtil {
    public ExceptionUtil() {
    }

    public static String getExceptionStack(Exception e) {
        String exceptionString = "";
        StringWriter sw = null;
        PrintWriter pw = null;

        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            pw.flush();
            sw.flush();
        } finally {
            if (sw != null) {
                try {
                    exceptionString = sw.toString();
                    sw.close();
                } catch (IOException var10) {
                    var10.printStackTrace();
                }
            }

            if (pw != null) {
                pw.close();
            }

        }

        return exceptionString;
    }
}

