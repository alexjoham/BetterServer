package de.alexjoham.BetterServer;


import java.io.*;
import java.util.stream.Stream;

public class FileManager {

    public static boolean createFile(String path) {
        File file = new File(path);
        try {
            return file.createNewFile();
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
                writer.append(key).append(": ").append(value);
                writer.close();
                return true;
            }
            if(reader.lines().noneMatch(entry -> entry.startsWith(key))) {
                writer.append(key).append(": ").append(value);
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
