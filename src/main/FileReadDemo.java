package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static utils.Utils.*;

public class FileReadDemo {
    public static void main(String[] args) {
        try {
            File file = new File(SHORT_WORDS_FILE);
            Scanner scanner = new Scanner(file);
            int i = 0;

            while (scanner.hasNextLine()) {
                i += 1;
                String nextLine = scanner.nextLine();
                System.out.print("Line " + i + " is: ");
                System.out.println(nextLine);

                System.out.print("After splitting on tab characters, the first word is: ");
                String[] splitLine = nextLine.split("\t");
                System.out.println(splitLine[0]);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Could not find the file: " + SHORT_WORDS_FILE);
            e.printStackTrace();
        }
    }
}