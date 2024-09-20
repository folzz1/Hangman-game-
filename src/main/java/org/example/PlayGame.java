package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayGame {
    private static final Logger LOGGER = Logger.getLogger(PlayGame.class.getName());
    private GameLogic gameLogic;
    private PrintStream out;

    public PlayGame(Word word, int difficulty) {
        this.out = System.out;
        this.gameLogic = new GameLogic(word, difficulty);
    }

    public void start(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            while (gameLogic.isGameOver()) {
                gameLogic.drawMan();
                out.print("Введите букву: ");
                String input = reader.readLine();
                if (input != null && !input.isEmpty()) {
                    gameLogic.check(Character.toLowerCase(input.charAt(0)));
                }
            }
            if (gameLogic.isWin()) {
                out.println("Вы победили!");
            } else {
                gameLogic.drawMan();
                out.println("Вы проиграли!");
            }
            out.println("Загаданное слово: " + gameLogic.getWord().word());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Произошла ошибка при чтении ввода ", e);
        }
    }

    public GameLogic getGameLogic() {
        return gameLogic;
    }

    public void setGameLogic(GameLogic gameLogic) {
        this.gameLogic = gameLogic;
    }

    public void setOut(PrintStream out) {
        this.out = out;
    }
}
