import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;
public class WrldMap {
    private  Map<String, Map<String, String>> world;

    public WrldMap(File loc) {
        try {
            FileReader file = new FileReader(loc);
            world = new HashMap<>();
            CSVReader csv = new CSVReader(file);
            List<String[]> allData = csv.readAll();
            allData.remove(0);
            for (String[] ln : allData) {
                addyFiller(ln[4].toLowerCase(), ln[1].toLowerCase(), ln[5].toLowerCase());
            }
            allData.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void addyFiller(String zip, String addy, String num) {
        world.putIfAbsent(zip, new HashMap<>());
        world.get(zip).putIfAbsent(addy, num);
    }
    public String storeNumPuller(List<String> addy) {
        Map<String, String> options = world.get(addy.get(0));
        double max = 0.0;
        String correct = "";
        for (String street : options.keySet()) {
            if (similarity(addy.get(1), street) > max) {
                max = similarity(addy.get(1), street);
                correct = street;
            }
        }
        String num = options.get(correct);
        options.remove(correct);
        return num;
    }
    public List<String> fileParser(File report) {
        String name = report.getName();
        String[] splitName = name.split("- ");
        switch (splitName.length) {
            case 4 -> {
                String[] stateZip = splitName[2].split(" ");
                if (stateZip.length != 2) {
                    return new ArrayList<>();
                }
                String[] addy = {stateZip[1].toLowerCase(), splitName[0].toLowerCase()};
                return Arrays.asList(addy);
            }
            case 5 -> {
                String[] stateZip1 = splitName[3].split(" ");
                if (stateZip1.length != 2) {
                    return new ArrayList<>();
                }
                String[] addy1 = {stateZip1[1].toLowerCase(), splitName[1].toLowerCase()};
                return Arrays.asList(addy1);
            }
            default -> {
                return new ArrayList<>();
            }
        }
    }
    public void renamer(File dir) {
        File[] reports = dir.listFiles();
        for (File report : reports) {
            List<String> addy = fileParser(report);
            String newFileName;
            if (addy.size() == 2) {
                newFileName = storeNumPuller(addy) + ".pdf";
            } else {
                newFileName = report.getName() + ".pdf";
            }
            String sourceFilePath = "C:/Users/nathan.canera/Downloads/Crime Report Process/Reports"
                    + "/" + dir.getName() + "/" + report.getName();
            String destinationDirectory = "C:/Users/nathan.canera/Downloads/Crime Report Process/ReportsOutput"
                    + "/" + dir.getName();
            try {
                Path sourcePath = Path.of(sourceFilePath);
                Path destinationPath = Path.of(destinationDirectory);
                Path newFilePath = destinationPath.resolve(newFileName);
                Files.copy(sourcePath, newFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public double similarity(String s1, String s2) {
        String longer = s1, shorter = s2;
        if (s1.length() < s2.length()) { // longer should always have greater length
            longer = s2; shorter = s1;
        }
        int longerLength = longer.length();
        if (longerLength == 0) { return 1.0; /* both strings are zero length */ }
    /* // If you have Apache Commons Text, you can use it to calculate the edit distance:
    LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
    return (longerLength - levenshteinDistance.apply(longer, shorter)) / (double) longerLength; */
        return (longerLength - editDistance(longer, shorter)) / (double) longerLength;

    }

    // Example implementation of the Levenshtein Edit Distance
    // See http://rosettacode.org/wiki/Levenshtein_distance#Java
    public int editDistance(String s1, String s2) {
        s1 = s1.toLowerCase();
        s2 = s2.toLowerCase();

        int[] costs = new int[s2.length() + 1];
        for (int i = 0; i <= s1.length(); i++) {
            int lastValue = i;
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    costs[j] = j;
                else {
                    if (j > 0) {
                        int newValue = costs[j - 1];
                        if (s1.charAt(i - 1) != s2.charAt(j - 1))
                            newValue = Math.min(Math.min(newValue, lastValue),
                                    costs[j]) + 1;
                        costs[j - 1] = lastValue;
                        lastValue = newValue;
                    }
                }
            }
            if (i > 0)
                costs[s2.length()] = lastValue;
        }
        return costs[s2.length()];
    }

}
