import com.opencsv.CSVReader;

import java.io.EOFException;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    //doing all 23 sets of 100
    public static void main(String[] args) {
        File storeDir = new File("C:/Users/nathan.canera/Downloads/Crime Report Process/100 stores");
        File reportDir = new File("C:/Users/nathan.canera/Downloads/Crime Report Process/Reports");
        File wilRep = new File("C:/Users/nathan.canera/Downloads/Crime Report Process/Wilson's Reports");
        File[] stores = storeDir.listFiles();
        File[] reports = reportDir.listFiles();
        for (int i = 0; i < 24; i++) {
            WorldMap wrld = new WorldMap(stores[i]);
            new File("C:/Users/nathan.canera/Downloads/Crime Report Process/ReportsOutput/"
                    + reports[i].getName()).mkdirs();
            wrld.renamer(reports[i]);
        }
    }
}
