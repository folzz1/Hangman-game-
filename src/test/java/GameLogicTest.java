import org.example.Category;
import org.example.GameLogic;
import org.example.Word;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class GameLogicTest {
    @Test
    public void testConstructorInitializesFields() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        int difficulty = 3;

        GameLogic gameLogic = new GameLogic(word, difficulty);

        assertThat(gameLogic.getWord()).isEqualTo(word);
        assertThat(gameLogic.getAttemptNumber()).isEqualTo(2);
        assertThat(gameLogic.getCurrentResponse().toString()).isEqualTo("____");
        assertThat(gameLogic.getMistakesNumber()).isEqualTo(0);
    }

    @Test
    public void testConstructorInitializesGameStage() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        int difficulty = 3;

        GameLogic gameLogic = new GameLogic(word, difficulty);

        assertNotNull(gameLogic.getGameStage());
        assertEquals(6, gameLogic.getGameStage().length);
    }

    @Test
    public void testCheckWithCorrectLetter() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);

        gameLogic.setCurrentResponse(new StringBuilder("____"));
        gameLogic.setAttemptNumber(0);
        gameLogic.setMistakesNumber(0);

        boolean result = gameLogic.checkLetter('т');

        assertThat(result).isTrue();
        assertThat(gameLogic.getCurrentResponse().toString()).isEqualTo("т__т");
        assertThat(gameLogic.getMistakesNumber()).isEqualTo(0);
    }

    @Test
    public void testCheckWithIncorrectLetter() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);

        gameLogic.setCurrentResponse(new StringBuilder("____"));
        gameLogic.setAttemptNumber(0);
        gameLogic.setMistakesNumber(0);

        boolean result = gameLogic.checkLetter('а');

        assertThat(result).isFalse();
        assertThat(gameLogic.getMistakesNumber()).isEqualTo(1);
        assertThat(gameLogic.getAttemptNumber()).isEqualTo(1);
    }

    @Test
    public void testIsWinWhenPlayerWins() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);

        gameLogic.setCurrentResponse(new StringBuilder("тест"));
        assertThat(gameLogic.isWin()).isTrue();
    }

    @Test
    public void testIsWinWhenPlayerLoses() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);
        gameLogic.setCurrentResponse(new StringBuilder("____"));

        assertThat(gameLogic.isWin()).isFalse();
    }

    @Test
    public void testIsGameOverWhenAttemptsLeft() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);

        gameLogic.setAttemptNumber(1);
        gameLogic.setCurrentResponse(new StringBuilder("____"));

        assertThat(gameLogic.isGameNotOver()).isTrue();
    }

    @Test
    public void testIsGameOverWhenNoAttemptsLeft() {
        Word word = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic gameLogic = new GameLogic(word, 3);

        gameLogic.setAttemptNumber(5);
        gameLogic.setCurrentResponse(new StringBuilder("тест"));

        assertThat(gameLogic.isGameNotOver()).isFalse();
    }
}
