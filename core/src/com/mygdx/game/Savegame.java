package com.mygdx.game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class Savegame {
    private static final long[] ma_saveLevels = new long[2];

    static {
        ma_saveLevels[0] = 565452221546648L;
        ma_saveLevels[1] = 856235777895645L;
    }

    public static void writeSavestate(int iv_level) throws IOException {
        //Check if level to save is under already saved level
        if (!isLevelUnlocked(iv_level)) {
            return;
        }
        //Save to file
        FileWriter lr_fileWriter = new FileWriter("save.txt");
        lr_fileWriter.write(String.valueOf(ma_saveLevels[iv_level-1]));
        lr_fileWriter.close();
    }

    private static int getCorrespondingLevel(long iv_savestate) {
        int lv_levelCounter = 1;
        for (long ma_saveLevel : ma_saveLevels) {
            if (ma_saveLevel == iv_savestate) {
                break;
            }
            lv_levelCounter++;
        }
        return lv_levelCounter;
    }

    private static long getCurrentSaveState () throws FileNotFoundException {
        Scanner lr_scanner = new Scanner(new File("save.txt"));
        long lv_savestate = Long.parseLong(lr_scanner.nextLine());
        lr_scanner.close();
        return lv_savestate;
    }

    public static int getSavedLevel() throws FileNotFoundException {
        return getCorrespondingLevel(getCurrentSaveState());
    }

    public static boolean isLevelUnlocked(int iv_level) throws FileNotFoundException {
        return getSavedLevel() <= iv_level;
    }

}
