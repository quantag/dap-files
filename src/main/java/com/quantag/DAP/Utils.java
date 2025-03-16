package com.quantag.DAP;


import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;

@Slf4j
public class Utils {
    public static String replace(String str, char a, char b) {
        // Check if the string is null or empty
        if (str == null || str.isEmpty()) {
            return str;
        }

        // Convert the characters to strings for easy replacement
        String charA = Character.toString(a);
        String charB = Character.toString(b);

        // Use the replace method to replace all occurrences of charA with charB
        String replacedString = str.replace(charA, charB);

        return replacedString;
    }

    public static boolean createFolderIfNotExist(String path) {
        log.info("[{}]", path);
        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            return parent.mkdirs(); // This will create all missing parent directories
        }
        return false;
    }

    public static void clearFolder(String path) {
        File folder = new File(path);

        if (folder.exists()) {
            if (folder.isDirectory()) {
                File[] files = folder.listFiles();
                if (files != null) {
                    for (File file : files) {
                        if (file.isDirectory()) {
                            clearFolder(file.getAbsolutePath()); // Recursively clear subdirectories
                        } else {
                            file.delete(); // Delete file
                        }
                    }
                }
            }
        } else {
            folder.mkdirs(); // Create folder if it doesn't exist
        }
    }

    public static void saveFile(String path, byte[] data) {
        if(path==null || data==null) {
            log.error("null data in saveFile");
            return;
        }
        boolean result = Utils.createFolderIfNotExist(path);
        if(result)
            log.info("created folder: {}", path);

        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path))) {
            bos.write(data);
            log.info("Written "+data.length+" bytes to " + path);
        } catch (java.io.IOException e) {
            log.error("Can not store data to file "+path);
        }
    }

    public static byte[] loadFile(String path) throws java.io.IOException {
        Path filePath = Paths.get(path);
        return Files.readAllBytes(filePath);
    }
}
