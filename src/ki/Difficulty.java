package ki;

public enum Difficulty {
    EASY(7),
    NORMAL(8),
    HARD(9),
    EXTREME(11);

    public final int value;
    private Difficulty(int difficulty) {
        this.value = difficulty;
    }
}
