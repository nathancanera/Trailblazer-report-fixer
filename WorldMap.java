import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

public class WorldMap {
    private Map<String, Map<String, Map<String, Map<String, String>>>> world;
    public WorldMap(File loc) {
        try {
            FileReader file = new FileReader(loc);
            world = new HashMap<>();
            CSVReader csv = new CSVReader(file);
            List<String[]> allData = csv.readAll();
            allData.remove(0);
            for (String[] ln : allData) {
                addyFiller(ln[3].toLowerCase(), ln[2].toLowerCase(), ln[4].toLowerCase(),
                        ln[1].toLowerCase(), ln[5].toLowerCase());
            }
            allData.size();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addyFiller(String state, String city, String zip, String addy, String num) {
        world.putIfAbsent(state, new HashMap<>());
        world.get(state).putIfAbsent(city, new HashMap<>());
        world.get(state).get(city).putIfAbsent(zip, new HashMap<>());
        world.get(state).get(city).get(zip).putIfAbsent(addy, num);
    }

    public String storeNumPuller(List<String> addy) {
        try {
            Map<String, String> options = world.get(addy.get(0)).get(addy.get(1)).get(addy.get(2));
            double max = 0.0;
            String correct = "";
            for (String street : options.keySet()) {
                if (similarity(addy.get(3), street) > max) {
                    max = similarity(addy.get(3), street);
                    correct = street;
                }
            }
            String num = options.get(correct);
            options.remove(correct);
            return num;
        }
        catch (NullPointerException e) {
            return addy.get(3);
        }

    }

    public void renamer(File dir) {
        File[] reports = dir.listFiles();
        for (File report : reports) {
            List<String> addy = fileParser(report);
            String newFileName;
            if (addy.size() == 4) {
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

    public List<String> fileParser(File report) {
        String name = report.getName();
        String[] splitName1 = name.split("- ");
        List<String> splitName;
        if (splitName1.length < 3) {
            return Arrays.asList(splitName1);
        }
        if (splitName1[2].length() > 8 && splitName1.length < 5) {
            splitName1[2] = splitName1[2].substring(0, 8);
            List<String> splitName2 = Arrays.asList(splitName1);
            splitName = new ArrayList<>(splitName2);
            splitName.add("kjlfdak;d");
        } else {
            splitName = Arrays.asList(splitName1);
        }
        switch (splitName.size()) {
            case 4 -> {
                String[] stateZip = splitName.get(2).split(" ");
                if (stateZip.length != 2) {
                    return new ArrayList<>();
                }
                if (stateZip[1].charAt(0) == '0') {
                    stateZip[1] = stateZip[1].substring(1);
                }
                String[] addy = {stateZip[0].toLowerCase(), splitName.get(1).toLowerCase(),
                        stateZip[1].toLowerCase(), splitName.get(0).toLowerCase()};
                return Arrays.asList(addy);
            }
            case 5 -> {
                String[] stateZip1 = splitName.get(3).split(" ");
                if (stateZip1.length != 2) {
                    return new ArrayList<>();
                }
                if (stateZip1[1].charAt(0) == '0') {
                    stateZip1[1] = stateZip1[1].substring(1);
                }
                String[] addy1 = {stateZip1[0].toLowerCase(), splitName.get(2).toLowerCase(),
                        stateZip1[1].toLowerCase(), splitName.get(1).toLowerCase()};
                return Arrays.asList(addy1);
            }
            case 3 -> {
                String[] stateZip2 = splitName.get(1).split(" ");
                String[] addy2 = {stateZip2[0].toLowerCase(), splitName.get(0).toLowerCase(),
                        stateZip2[1].toLowerCase()};
                return Arrays.asList(addy2);
            }
            default -> {
                List<String> addy3 = new ArrayList<>();
                return addy3;
            }
        }
    }

    public static void main(String[] args) {
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
