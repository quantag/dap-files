package com.quantag.DAP;


import java.io.File;

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

    public static void createFolderIfNotExist(String path) {
        File file = new File(path);
        File parent = file.getParentFile();

        if (parent != null && !parent.exists()) {
            parent.mkdirs(); // This will create all missing parent directories
        }
    }
}
