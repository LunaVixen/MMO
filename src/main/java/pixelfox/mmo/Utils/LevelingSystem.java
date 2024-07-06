package pixelfox.mmo.Utils;

public class LevelingSystem {

    public static int miningExp(int level) {
        return (int) (100 * Math.pow(1.5, level - 1));
    }

    public static int getNextLevelExperience(int currentLevel) {
        return miningExp(currentLevel + 1);
    }
}
