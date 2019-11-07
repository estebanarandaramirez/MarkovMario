package levelGenerators.ArandaMarwahaGenerator;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.lang.String;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

public class LevelGenerator implements MarioLevelGenerator {
    // class attributes
    public String map;

    // random seed
    Random seed;

    // constructors
    public LevelGenerator(String filePath) {
        this.map = getLevel(filePath);
    }

    public LevelGenerator() {
        this.map = getLevel("levels/original/lvl-1.txt");
    }

    // getLevel specified by input
    // TAKEN FROM PlayerLevel.java
    public static String getLevel(String filepath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filepath)));
        } catch (IOException e) {
        }
        return content;
    }

    public char[][] extractColumns() {
        int width = this.map.indexOf("\n")+1;
        int height = this.map.split("\n").length;
        char[] arrayChar = this.map.toCharArray();
        char[][] columns = new char[width][height];
        for(int i = 0; i < width; i++) {
            for(int j = 0; j < height; j++) {
                if(i != (width-1)) {
                    columns[i][j] = arrayChar[i+(width*j)];
                } else {
                    columns[i][j] = '\n';
                }
            }
        }
        return columns;
    }

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        this.seed = new Random();
        model.clearMap();

        char[][] columns = extractColumns();

        int finalColumn = 0;
        for(int i = 0; i < columns.length; i++) {
            if(Arrays.toString(columns[i]).contains("F")) {
                finalColumn = i;
                break;
            }
        }

        char[] charMap = new char[model.getWidth()*model.getHeight()];

        for(int i = 0; i < model.getWidth(); i++) {
            char[] chosenColumn;
            if(i == 0) {
                chosenColumn = columns[0];
            } else if(i == (model.getWidth()-2)) {
                chosenColumn = columns[finalColumn];
            } else if(i == (model.getWidth()-1)) {
                chosenColumn = columns[this.map.indexOf("\n")];
            } else {
                int rand = this.seed.nextInt(columns.length-2)+1;
                chosenColumn = columns[rand];
            }

            for(int j = 0; j < model.getHeight(); j++) {
                charMap[i + (model.getWidth() * j)] = chosenColumn[j];
            }
        }

        String result = new String(charMap);
        return result;
    }

    @Override
    public String getGeneratorName() {
        return "ArandaMarwahaGenerator";
    }
}
