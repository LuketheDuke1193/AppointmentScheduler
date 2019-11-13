package Utilities;

import java.io.*;
import java.time.ZonedDateTime;

    public class LoggerHandler
    {
        private static final String fileName = "log.txt";

        public LoggerHandler() {

        }

        public static void log(boolean successfulLogin, String user) throws IOException {
            FileWriter fileWriter = new FileWriter(fileName, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            PrintWriter outputStream = new PrintWriter(bufferedWriter);
            if (successfulLogin == true) {
                String success = "successfully";
                outputStream.println(ZonedDateTime.now() + " - " + user + " tried to log in " + success + ".");
                outputStream.close();
            } else {
                String success = "unsuccessfully";
                outputStream.println(ZonedDateTime.now() + " - " + user + " tried to log in " + success + ".");
                outputStream.close();
            }
        }
    }