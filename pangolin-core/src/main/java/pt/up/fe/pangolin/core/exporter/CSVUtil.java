package pt.up.fe.pangolin.core.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVUtil {
    public static void initializeCSVDirectory(String dirPath) {
        File dir = new File(dirPath);
        createDirIfNotExists(dir);
        cleanup(dir);
        initializeNeededFiles(dir);
    }

    private static void createDirIfNotExists(File dir) {
        if (!dir.exists()) {
            try {
                if(!dir.mkdirs())
                    System.out.println("oops, couldn't create the directory " + dir);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void cleanup(File dir) {
        if (dir.listFiles() != null) {
            for (File file : dir.listFiles()) {
                file.delete();
            }
        }
    }

    private static void initializeNeededFiles(File dir) {
        createCSVFile(dir.getPath() + File.separator + Constants.ANALYSIS_PATH_SUFFIX, Constants.ANALYSIS_HEADER);
    }

    private static void createCSVFile(String path, String header) {
        try {
            File file = new File(path);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.append(header);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addAllToCSVFile(String path, List collection) {
       try {
          File file = new File(path);
          FileWriter fileWriter = new FileWriter(file, true);
          BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
          for(Object obj: collection) {
              String row = (obj instanceof String) ? (String) obj : obj.toString();
              bufferedWriter.append(row);
          }
          bufferedWriter.close();
       } catch(IOException e) {
           e.printStackTrace();
       }
    }
}
