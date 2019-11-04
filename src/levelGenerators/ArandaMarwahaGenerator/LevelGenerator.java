package levelGenerators.ArandaMarwahaGenerator;

import java.util.Random;

import engine.core.MarioLevelGenerator;
import engine.core.MarioLevelModel;
import engine.core.MarioTimer;

public class LevelGenerator implements MarioLevelGenerator {
    // class attributes
    public int maxGaps;

    // constants
    private double CHANCE_BLOCK_POWER_UP = 0.1;
    private double CHANCE_BLOCK_COIN = 0.3;
    private double CHANCE_BLOCK_ENEMY = 0.2;
    private double CHANCE_WINGED = 0.5;
    private double CHANCE_COIN = 0.2;
    private double COIN_HEIGHT = 5;
    private double CHANCE_PLATFORM = 0.1;
    private double CHANCE_END_PLATFORM = 0.1;
    private int PLATFORM_HEIGHT = 4;
    private double CHANCE_ENEMY = 0.1;
    private double CHANCE_PIPE = 0.1;
    private int PIPE_MIN_HEIGHT = 2;
    private double PIPE_HEIGHT = 3.0;
    private int minX = 5;
    private double CHANCE_HILL = 0.1;
    private double CHANCE_END_HILL = 0.3;
    private double CHANCE_HILL_ENEMY = 0.3;
    private double HILL_HEIGHT = 4;
    private int GAP_LENGTH = 5;
    private double CHANGE_GAP = 0.1;
    private double CHANGE_HILL_CHANGE = 0.1;
    private double GAP_OFFSET = -5;
    private double GAP_RANGE = 10;
    private int GROUND_MAX_HEIGHT = 5;

    // random seed
    Random seed;

    public LevelGenerator() {
        this.maxGaps = 10;
    }

    public LevelGenerator(int maxGaps) {
        this.maxGaps = maxGaps;
    }

    public int generateTransitionModel() {
        return 1;
    }

    @Override
    public String getGeneratedLevel(MarioLevelModel model, MarioTimer timer) {
        model.clearMap();
        return model.getMap();
    }

    @Override
    public String getGeneratorName() {
        return "ArandaMarwahaGenerator";
    }
}
