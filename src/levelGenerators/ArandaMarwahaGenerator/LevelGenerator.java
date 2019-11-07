package levelGenerators.ArandaMarwahaGenerator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.lang.String;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

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

    public List<List<Integer>> heuristicClassifier(char[][] columns, int finalColumn, int marioColumn) {
        List<List<Integer>> classifications = new ArrayList<>();
        List<Integer> bad = new ArrayList<>();
        List<Integer> lessBad = new ArrayList<>();
        List<Integer> normal = new ArrayList<>();
        List<Integer> good = new ArrayList<>();
        String tempArray;
        for(int i = 0; i < columns.length; i++) {
            if(i != 0 && i != marioColumn && i != finalColumn && i != (columns.length-1)) {
                tempArray = Arrays.toString(columns[i]);
                if(tempArray.contains("@") || tempArray.contains("Q") || tempArray.contains("o") || tempArray.contains("!")) {
                    good.add(i);
                } else if (tempArray.contains("g") || tempArray.contains("k") || tempArray.contains("r") || !(tempArray.contains("X"))) {
                    bad.add(i);
                } else if(tempArray.contains("T") || tempArray.contains("S") || tempArray.contains("#")) {
                    lessBad.add(i);
                } else {
                    normal.add(i);
                }
            }
        }
        classifications.add(bad);
        classifications.add(lessBad);
        classifications.add(normal);
        classifications.add(good);
        return classifications;
    }

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        this.seed = new Random();
        model.clearMap();

        char[][] columns = extractColumns();

        int finalColumn = 0;
        int marioColumn = 0;
        for(int i = 0; i < columns.length; i++) {
            if(Arrays.toString(columns[i]).contains("F")) {
                finalColumn = i;
            } else if(Arrays.toString(columns[i]).contains("M")) {
                marioColumn = i;
            }
        }

        List<List<Integer>> classifications = heuristicClassifier(columns, finalColumn, marioColumn);

        char[] charMap = new char[model.getWidth()*model.getHeight()];

        for(int i = 0; i < model.getWidth(); i++) {
            char[] chosenColumn;
            if (i == 0) {
                chosenColumn = columns[0];
            } else if (i == 1) {
                chosenColumn = columns[marioColumn];
            }else if (i == (model.getWidth() - 2)) {
                chosenColumn = columns[finalColumn];
            } else if (i == (model.getWidth() - 1)) {
                chosenColumn = columns[this.map.indexOf("\n")];
            } else {
                int rand = this.seed.nextInt(100) + 1;
                if (rand <= 20) {
                    // choose random column from good group
                    List<Integer> good = classifications.get(3);
                    chosenColumn = columns[good.get(this.seed.nextInt(good.size()))];
                } else if (rand > 20 && rand <= 60) {
                    // choose random column from bad group
                    List<Integer> bad = classifications.get(0);
                    chosenColumn = columns[bad.get(this.seed.nextInt(bad.size()))];
                } else if (rand > 60 && rand <= 70) {
                    // choose random column from lessBad group
                    List<Integer> lessBad = classifications.get(1);
                    chosenColumn = columns[lessBad.get(this.seed.nextInt(lessBad.size()))];
                } else {
                    // choose random column from normal group
                    List<Integer> normal = classifications.get(2);
                    chosenColumn = columns[normal.get(this.seed.nextInt(normal.size()))];
                }
            }
            for (int j = 0; j < model.getHeight(); j++) {
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
