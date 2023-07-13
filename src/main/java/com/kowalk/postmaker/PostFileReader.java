package com.kowalk.postmaker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PostFileReader {
    
    public static String read(String path) throws IOException {
        File f = new File(path);
        StringBuilder builder = new StringBuilder();
        char[] buffer = new char[128];
        int read = 0;

        try (FileReader reader = new FileReader(f)) {
            while((read = reader.read(buffer)) != -1) {
                builder.append(buffer, 0, read);
            }
            return builder.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new IOException("Could not open file");
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("Could not close file");
        }
    }

}
