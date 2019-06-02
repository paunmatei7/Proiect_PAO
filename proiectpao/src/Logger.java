import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Logger {
    private Logger() {
    }

    public static void log(String action){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        try(FileWriter out = new FileWriter("logging.csv", true)){
            out.append(action+","+timestamp+","+Thread.currentThread().toString()+"\n");

        } catch (IOException e) {
            System.out.println("There was a problem writing to the log file");

        }
    }
}
