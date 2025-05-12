package by.it.group310951.suponenko.lesson15;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class SourceScannerC {

    public static void main(String[] args) {
        String src = System.getProperty("user.dir") + File.separator + "src" + File.separator;
        processDirectory(src);
    }

    private static void processDirectory(String src) {
        Map<String, List<Path>> contentMap = new HashMap<>();
        try {
            Files.walkFileTree(Paths.get(src), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    String path = file.toString();
                    if (path.endsWith(".java")) {
                        try {
                            String text = new String(Files.readAllBytes(file), "UTF-8");
                            if (text.contains("@Test") || text.contains("org.junit.Test"))
                                return FileVisitResult.CONTINUE;
                            text = removePackageAndImports(text);
                            text = removeComments(text);
                            text = replaceLowCharsWithSpace(text).trim();
                            if (!text.isEmpty()) {
                                contentMap.computeIfAbsent(text, k -> new ArrayList<>()).add(file);
                            }
                        } catch (MalformedInputException mie) {
                            System.err.println("Невозможно прочитать файл (неверная кодировка): " + path);
                        } catch (IOException ioe) {
                            System.err.println("Ошибка при чтении файла: " + path + ": " + ioe.getMessage());
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            System.err.println("Ошибка обхода каталога: " + src + ", " + e.getMessage());
            return;
        }
        List<String> keys = new ArrayList<>(contentMap.keySet());
        Collections.sort(keys);
        for (String key : keys) {
            List<Path> files = contentMap.get(key);
            if (files.size() > 1) {
                files.sort(Comparator.comparing(Path::toString));
                for (int i = 0; i < files.size(); i++) {
                    Path original = files.get(i);
                    List<Path> copies = new ArrayList<>();
                    for (int j = i + 1; j < files.size(); j++) {
                        Path other = files.get(j);
                        int dist = levenshteinDistance(key, key);
                        if (dist < 10) {
                            copies.add(other);
                        }
                    }
                    if (!copies.isEmpty()) {
                        System.out.println(original.toString());
                        for (Path p : copies) {
                            System.out.println("  " + p.toString());
                        }
                    }
                }
            }
        }
    }

    private static String removePackageAndImports(String text) {
        return text.replaceAll("(?m)^package\\s+[^;]+;", "")
                .replaceAll("(?m)^import\\s+[^;]+;", "");
    }

    private static String removeComments(String text) {
        StringBuilder sb = new StringBuilder();
        int len = text.length();
        for (int i = 0; i < len; ) {
            if (i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '/') {
                i += 2;
                while (i < len && text.charAt(i) != '\n') i++;
            } else if (i + 1 < len && text.charAt(i) == '/' && text.charAt(i + 1) == '*') {
                i += 2;
                while (i + 1 < len && !(text.charAt(i) == '*' && text.charAt(i + 1) == '/')) i++;
                i += 2;
            } else {
                sb.append(text.charAt(i));
                i++;
            }
        }
        return sb.toString();
    }

    private static String replaceLowCharsWithSpace(String text) {
        char[] arr = text.toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] < 33) arr[i] = ' ';
        }
        return new String(arr);
    }

    private static int levenshteinDistance(String a, String b) {
        int n = a.length(), m = b.length();
        if (n == 0) return m;
        if (m == 0) return n;
        int[] prev = new int[m + 1];
        int[] curr = new int[m + 1];
        for (int j = 0; j <= m; j++) prev[j] = j;
        for (int i = 1; i <= n; i++) {
            curr[0] = i;
            for (int j = 1; j <= m; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                curr[j] = Math.min(Math.min(curr[j - 1] + 1, prev[j] + 1), prev[j - 1] + cost);
            }
            int[] tmp = prev; prev = curr; curr = tmp;
        }
        return prev[m];
    }
}
