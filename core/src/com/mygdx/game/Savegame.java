package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Saves levels that are completed!
public class Savegame {
    private static final long[] saveCodes = new long[3];

    static {
        //Zero is default placeholder level
        saveCodes[0] = 565452221546648L;
        saveCodes[1] = 856235777895645L;
        saveCodes[2] = 564486518444565L;
    }

    public static void writeSavestate(int levelToSave) throws IOException {
        //Check if level to save is under already saved level
        if (!isLevelUnlocked(levelToSave)) {
            return;
        }
        //Save to file
        FileWriter fileWriter = new FileWriter("save.txt");
        fileWriter.write(String.valueOf(saveCodes[levelToSave]));
        fileWriter.close();
    }

    private static int getCorrespondingLevel(long saveStateToTranslate) {
        int levelCounter = 0;
        for (long saveState : saveCodes) {
            if (saveState == saveStateToTranslate) {
                break;
            }
            levelCounter++;
        }
        return levelCounter;
    }

    private static long getCurrentSaveState () {
        long currentSaveState;
        try {
            Scanner fileScanner = new Scanner(new File("save.txt"));
            currentSaveState = Long.parseLong(fileScanner.nextLine());
            fileScanner.close();

        } catch (Exception ex) {
            currentSaveState = saveCodes[0];
        }
        return currentSaveState;
    }

    private static int getSavedLevel() {
        return getCorrespondingLevel(getCurrentSaveState());
    }

    public static int getCurrentLevel() {
        int savedLevel = getSavedLevel();
        if (getSavedLevel() == saveCodes.length - 1) {
            return getSavedLevel();
        } else {
            return getSavedLevel() + 1;
        }
    }


    public static boolean isLevelUnlocked(int level) throws FileNotFoundException {
        return getSavedLevel() <= level;
    }

}
