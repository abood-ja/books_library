package aboodZaidLibrary;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.regex.Pattern;

public class clsAddNewCDScreen extends clsScreen {

    private static final String SEP = "#//#";
    private static final String CDS_FILE = "CDs.txt";

    private static void __ensureCDsFile() {
        try {
            Path p = Paths.get(CDS_FILE);
            if (Files.notExists(p))
                Files.createFile(p);
        } catch (IOException e) {
            System.out.println("⚠️ Cannot create CDs file: " + e.getMessage());
        }
    }

    private static boolean __cdIDExists(String id) {
        Path p = Paths.get(CDS_FILE);
        if (!Files.exists(p)) return false;

        try {
            for (String line : Files.readAllLines(p)) {
                if (line == null || line.trim().isEmpty()) continue;

                String[] cols = line.split(Pattern.quote(SEP));

                for (String cell : cols) {
                    if (cell.trim().equalsIgnoreCase(id))
                        return true;
                }
            }
        } catch (IOException e) {
            return false;
        }
        return false;
    }

    private static boolean __appendCD(String title, String artist, String id) {
        String line = String.join(SEP, title, artist, id);

        try (BufferedWriter w = Files.newBufferedWriter(Paths.get(CDS_FILE), StandardOpenOption.APPEND)) {
            w.write(line);
            w.newLine();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void showAddNewCDScreen() {

        if (!clsUserSession.currentUser.checkAccessPermission(clsUser.enPermissions.pAddNewBook)) {
            System.out.println("❌ You do not have permission to add CDs.");
            return;
        }

        System.out.print("\033[H\033[2J");
        System.out.flush();
        _DrawScreenHeader("\t\tAdd New CD");

        __ensureCDsFile();

        System.out.print("Enter Title: ");
        String title = clsInputValidate.readString().trim();

        System.out.print("Enter Artist: ");
        String artist = clsInputValidate.readString().trim();

        System.out.print("Enter CD ID: ");
        String id = clsInputValidate.readString().trim();

        if (__cdIDExists(id)) {
            System.out.println("⚠️ A CD with this ID already exists!");
            return;
        }

        if (__appendCD(title, artist, id)) {
            System.out.println("✅ CD added successfully.");
        } else {
            System.out.println("❌ Failed to add CD.");
        }
    }
}
