package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Savegame {
    private static final long[] saveStateNumbers = new long[2];

    static {
        saveStateNumbers[0] = 565452221546648L;
        saveStateNumbers[1] = 856235777895645L;
    }

    public static void writeSavestate(int levelToSave) throws IOException {
        //Check if level to save is under already saved level
        if (!isLevelUnlocked(levelToSave)) {
            return;
        }
        //Save to file
        FileWriter fileWriter = new FileWriter("save.txt");
        fileWriter.write(String.valueOf(saveStateNumbers[levelToSave-1]));
        fileWriter.close();
    }

    private static int getCorrespondingLevel(long saveStateToTranslate) {
        int levelCounter = 1;
        for (long saveState : saveStateNumbers) {
            if (saveState == saveStateToTranslate) {
                break;
            }
            levelCounter++;
        }
        return levelCounter;
    }

    private static long getCurrentSaveState () throws FileNotFoundException {
        Scanner fileScanner = new Scanner(new File("save.txt"));
        long currentSaveState = Long.parseLong(fileScanner.nextLine());
        fileScanner.close();
        return currentSaveState;
    }

    public static int getSavedLevel() throws FileNotFoundException {
        return getCorrespondingLevel(getCurrentSaveState());
    }

    public static boolean isLevelUnlocked(int level) throws FileNotFoundException {
        return getSavedLevel() <= level;
    }

}
