

import org.example.Category;
import org.example.GameLogic;
import org.example.PlayGame;
import org.example.Word;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PlayGameTest {
    @Test
    public void testConstructorInitializesGameLogic() {
        Category animals = new Category("Животные");
        Word word = new Word("Кошка", animals, "Домашнее животное");
        int difficulty = 1;
        PlayGame playGame = new PlayGame(word, difficulty);

        assertNotNull(playGame.getGameLogic(), "GameLogic должен быть инициализирован.");
    }

    @Test
    public void testConstructorSetsDifficulty() {
        Word mockWord = new Word("тест", new Category("Фрукты"), "Описание");
        int difficulty = 1;
        PlayGame playGame = new PlayGame(mockWord, difficulty);

        assertEquals(difficulty, playGame.getGameLogic().getAttemptNumber()+1, "Уровень сложности должен быть установлен.");
    }


    @Test
    public void testGameWin() throws Exception {
        Word mockWord = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic mockGameLogic = mock(GameLogic.class);
        when(mockGameLogic.isGameNotOver()).thenReturn(true, false);
        when(mockGameLogic.isWin()).thenReturn(true);
        when(mockGameLogic.getWord()).thenReturn(mockWord);

        PlayGame playGame = new PlayGame(mockWord, 1);
        playGame.setGameLogic(mockGameLogic);

        InputStream in = new ByteArrayInputStream("а\n".getBytes());
        PrintStream out = mock(PrintStream.class);
        playGame.setOut(out);

        playGame.start(in);

        verify(out).println("Вы победили!");
        verify(out).println("Загаданное слово: тест");
    }

    @Test
    public void testGameLose() throws Exception {
        Word mockWord = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic mockGameLogic = mock(GameLogic.class);
        when(mockGameLogic.isGameNotOver()).thenReturn(true, false);
        when(mockGameLogic.isWin()).thenReturn(false);
        when(mockGameLogic.getWord()).thenReturn(mockWord);

        PlayGame playGame = new PlayGame(mockWord, 1);
        playGame.setGameLogic(mockGameLogic);

        InputStream in = new ByteArrayInputStream("l\n".getBytes());
        PrintStream out = mock(PrintStream.class);
        playGame.setOut(out);

        playGame.start(in);

        verify(out).println("Вы проиграли!");
        verify(out).println("Загаданное слово: тест");
    }


    @Test
    public void testStartWithEmptyInput() throws Exception {
        Word mockWord = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic mockGameLogic = mock(GameLogic.class);
        when(mockGameLogic.isGameNotOver()).thenReturn(true, false);
        when(mockGameLogic.isWin()).thenReturn(false);
        when(mockGameLogic.getWord()).thenReturn(mockWord);

        PlayGame playGame = new PlayGame(mockWord, 1);
        playGame.setGameLogic(mockGameLogic);
        InputStream in = new ByteArrayInputStream("\n".getBytes());
        PrintStream out = mock(PrintStream.class);
        playGame.setOut(out);

        playGame.start(in);
        verify(mockGameLogic, never()).checkLetter(anyChar());
        verify(out).println("Вы проиграли!");
        verify(out).println("Загаданное слово: тест");
    }

    @Test
    public void testStartWithInvalidInput() throws Exception {
        Word mockWord = new Word("тест", new Category("Фрукты"), "Описание");
        GameLogic mockGameLogic = mock(GameLogic.class);
        when(mockGameLogic.isGameNotOver()).thenReturn(true, false);
        when(mockGameLogic.isWin()).thenReturn(false);
        when(mockGameLogic.getWord()).thenReturn(mockWord);
        PlayGame playGame = new PlayGame(mockWord, 1);
        playGame.setGameLogic(mockGameLogic);
        InputStream in = new ByteArrayInputStream("!\n".getBytes());
        PrintStream out = mock(PrintStream.class);
        playGame.setOut(out);

        playGame.start(in);
        verify(mockGameLogic).checkLetter('!');
        verify(out).println("Вы проиграли!");
        verify(out).println("Загаданное слово: тест");
    }
}
