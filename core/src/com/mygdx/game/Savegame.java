package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

//Saves levels that can be played
public class Savegame {
    private static final long[] saveCodes = new long[10];

    static {
        saveCodes[0] = 565452221546648L;
        saveCodes[1] = 856235777895645L;
        saveCodes[2] = 423654451232564L;
        saveCodes[3] = 447888448934278L;
        saveCodes[4] = 879324775891129L;
        saveCodes[5] = 938477438990208L;
        saveCodes[6] = 546822344685451L;
        saveCodes[7] = 894123125484886L;
        saveCodes[8] = 845465561225468L;
        saveCodes[9] = 345546889113849L;
    }

    public static void writeSavestate(int levelToSave) {
        if (isLevelPlayable(levelToSave+1) || isLevelEndScreen(levelToSave+1)) {
            return;
        }
        //Save to file
        try {
            FileWriter fileWriter = new FileWriter("save.txt");
            fileWriter.write(String.valueOf(saveCodes[levelToSave]));
            fileWriter.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static float getCurrentSaveState() {
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

    public static int getCurrentSavedLevel() {
        float savestate = getCurrentSaveState();
        return getLevelToSavestate(savestate);
    }

    private static int getLevelToSavestate(float saveStateToTranslate) {
        int levelCounter = 1;
        for (long savestate : saveCodes) {
            if (savestate == saveStateToTranslate) {
                break;
            }
            levelCounter++;
        }
        return levelCounter;
    }

    public static boolean isLevelPlayable(int level) {
        return level <= getCurrentSavedLevel();
    }

    public static int getAmountOfLevels() {
        return saveCodes.length;
    }

    public static boolean isLevelEndScreen(int level) {
        return level > saveCodes.length;
    }

}
