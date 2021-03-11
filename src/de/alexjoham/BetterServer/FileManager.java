package de.alexjoham.BetterServer;

import org.bukkit.craftbukkit.libs.org.apache.commons.io.output.FileWriterWithEncoding;

import java.io.*;
import java.util.logging.Level;
import java.util.stream.Stream;

public class FileManager {

    public static boolean createFile(String path) {
        File file = new File(path);
        try {
            if(file.createNewFile()) {
                return true;
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean writeFile(String path, String key, String value) throws IOException {
        File file = new File(path);
        if(file.exists()) {
            BufferedWriter writer = new BufferedWriter(new FileWriter(path));
            BufferedReader reader = new BufferedReader(new FileReader(path));
            if(reader.lines().toArray().length == 0) {
                writer.append(key + ": " + value);
                writer.close();
                return true;
            }
            if(reader.lines().filter(entry -> entry.startsWith(key)).count() == 0) {
                writer.append(key + ": " + value);
            } else {
                Object[] lines = reader.lines().toArray();
                for(int i = 0; i < lines.length; i++) {
                    if(lines[i].toString().startsWith(key)) {
                        lines[i] = key + ": "+value;
                        break;
                    }
                }
                writer.write(lines[0].toString());
                for(int i = 1; i < lines.length; i++) {
                    writer.append(lines[i].toString());
                }
            }
            writer.close();
            return true;
        }
        return false;
    }

    public static Stream<String> readFile(String path) throws FileNotFoundException {
        BufferedReader reader = new BufferedReader(new FileReader(path));
        return reader.lines();
    }
}
