import java.util.*;
import java.io.*;


public class DictionaryManagement {

    public Dictionary dictionary = null;
    final String sourceFile = "dictionaries.txt";

    public DictionaryManagement (Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public void insertFromCommandline() {
        System.out.print("Input the amount of word: ");
        int wordAmount = ConsoleC.scan.nextInt();
        System.out.println();
        ConsoleC.scan.nextLine();

        for (int i = 1; i <= wordAmount; i++) {
            System.out.print("Input " + i + " target : ");
            String target = ConsoleC.scan.nextLine();
            System.out.print("Input " + i + " explain: ");
            String explain = ConsoleC.scan.nextLine();
            System.out.print("Input " + i + " synonym: ");
            String synonym = ConsoleC.scan.nextLine();
            System.out.print("Input " + i + " antonym: ");
            String antonym = ConsoleC.scan.nextLine();
            dictionary.addWord(target, explain, synonym, antonym);
        }
        System.out.println();
    }

    public void insertFromFile() {
        try {
            System.out.println("    -- Input from file --");
            File file = new File(sourceFile);
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String[] data = scan.nextLine().split("\\|");
                dictionary.addWord(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim());
            }
            scan.close();
            System.out.println("    -- Input done --");
        } catch (FileNotFoundException e) {
            System.out.println("Can't read file: " + sourceFile);
        }
        System.out.println();
    }

    public void showAllWords() {
        int noSpace = 4;
        int targetSpace = 25;
        int explainSpace = 20;
        int synonymSpace = 20;
        System.out.println("    -- Show all words: --");
        // Print header
        System.out.print("No");
        for (int i = 1; i <= noSpace - 2; i++) {
            System.out.print(" ");
        }
        System.out.print("|  English");
        for (int i = 1; i <= targetSpace - 9; i++) {
            System.out.print(" ");
        }
        System.out.print("|  Vietnamese");
        for (int i = 1; i <= 10; i++) {
            System.out.print(" ");
        }
        System.out.print("|  Synonyms");
        for (int i = 1; i <= 15; i++) {
            System.out.print(" ");
        }
        System.out.println("|  Antonyms");
        // Print content
        int count = 1;
        for (String target : dictionary.word.keySet()) {
            String no = count++ + "";
            String explain = dictionary.word.get(target).word_explain;
            String synonyms = dictionary.word.get(target).word_synonyms;
            String antonyms = dictionary.word.get(target).word_antonyms;

            System.out.print(no);
            for (int j = 1; j <= noSpace - no.length(); j++) {
                System.out.print(" ");
            }
            System.out.print("|" + target);
            for (int j = 1; j <= targetSpace - target.length(); j++) {
                System.out.print(" ");
            }
            System.out.print("|" + explain);
            for (int j = 1; j <= explainSpace - explain.length(); j++) { //getSpace - explain.length()
                System.out.print(" ");
            }
            System.out.print("|" + synonyms);
            for (int j = 1; j <= synonymSpace - synonyms.length(); j++) { //getSpace - explain.length()
                System.out.print(" ");
            }
            System.out.println("|" + antonyms);
        }
        System.out.println();
    }

    public void dictionaryLookup() {
        System.out.println("    -- Look up --");
        System.out.print("Input the word you need to find: ");
        String target = ConsoleC.scan.nextLine();

        if (dictionary.word.containsKey(target)) {
            Word word = dictionary.word.get(target);
            System.out.println("Meaning: " + word.word_explain);
            System.out.println("Synonyms: " + word.word_synonyms);
            System.out.println("Antonyms: " + word.word_antonyms);
        } else {
            System.out.println("Your word is not in dictionary yet");
        }
    }

    public void eraseWord() {
        System.out.print("Input word to erase: ");
        String target = ConsoleC.scan.nextLine();
        dictionary.word.remove(target);
    }

    public void addWord() {
        System.out.println("    -- Add word --");
        System.out.print("Input word target: ");
        String target = ConsoleC.scan.nextLine();
        if (dictionary.word.containsKey(target)) {
            System.out.println("Dictionary has already had word '" + target + "' with meaning '" + dictionary.word.get(target).word_explain + "'");
            System.out.print("Input y to continue to change the meaning: ");
            String temp = ConsoleC.scan.nextLine();
            if (temp.equals("y") || temp.equals("Y")) {
                System.out.print("Input word explain: ");
                String explain = ConsoleC.scan.nextLine();
                System.out.print("Input word synonym: ");
                String synonym = ConsoleC.scan.nextLine();
                System.out.print("Input word antonym: ");
                String antonym = ConsoleC.scan.nextLine();
                dictionary.word.get(target).word_explain = explain;
                dictionary.word.get(target).word_synonyms = synonym;
                dictionary.word.get(target).word_antonyms = antonym;
            }
        } else {
            System.out.print("Input word explain: ");
            String explain = ConsoleC.scan.nextLine();
            System.out.print("Input word synonym: ");
            String synonym = ConsoleC.scan.nextLine();
            System.out.print("Input word antonym: ");
            String antonym = ConsoleC.scan.nextLine();
            dictionary.addWord(target, explain, synonym, antonym);
        }
        System.out.println();
    }

    public void dictionaryExportToFile() {
        try {
            System.out.print("Input y to export dictionary to file: ");
            String temp = ConsoleC.scan.nextLine();
            if (!(temp.equals("y") || temp.equals("Y"))) {
                return;
            }
            System.out.println("    -- Start writing --");
            FileWriter writer = new FileWriter(sourceFile);
            for (String target : dictionary.word.keySet()) {
                writer.write(target + " | "
                        + dictionary.word.get(target).word_explain
                        + " | " + dictionary.word.get(target).word_synonyms
                        + " | " + dictionary.word.get(target).word_antonyms + "\n");
            }
            writer.close();
            System.out.println("    -- Writing completed --");
        } catch (IOException e) {
            System.out.println("Cannot write to file");
        }
        System.out.println();
    }

    public Set<String> getWordHint(String word) {
        return dictionary.word.prefixMap(word).keySet();
    }

    public void importCsvFile() {
        try {
            Scanner scan = new Scanner(new File("dictionaries.csv"));
            while (scan.hasNextLine()) {
                String[] word = scan.nextLine().split(",");
                dictionary.addWord(word[0].toLowerCase(), "(" + word[1] + ") " + word[2], "", "");
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        }
    }

}
