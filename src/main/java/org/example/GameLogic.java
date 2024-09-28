package org.example;

import java.io.PrintStream;
import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("lombok")
public class GameLogic {

    private final PrintStream out;
    @Getter private final String[] gameStage;
    @Setter @Getter private StringBuilder currentResponse;
    @Setter @Getter private int attemptNumber;
    @Getter private final Word word;
    @Setter @Getter private int mistakesNumber;

    public GameLogic(Word word, int difficult) {
        this.out = new PrintStream(System.out);
        this.word = word;
        this.gameStage = new String[]{
                """
                   O
                  \\|/
                   |
                  / \\
            """,
                """
                   O
                  \\|/
                   |
                  / \\
                 _____
                 |   |
            """,
                """
            |
            |      O
            |     \\|/
            |      |
            |     / \\
            |    _____
            |    |   |
            """,
                """
            _________
            |
            |      O
            |     \\|/
            |      |
            |     / \\
            |    _____
            |    |   |
            """,
                """
            _________
            |      |
            |      O
            |     \\|/
            |      |
            |     / \\
            |    _____
            |    |   |
            """,
                """
            _________
            |      |
            |      O
            |     \\|/
            |      |
            |     / \\
            |
            |
            """,
        };
        attemptNumber = difficult - 1;
        currentResponse = new StringBuilder("_".repeat(word.word().length()));
        mistakesNumber = 0;
    }

    public void drawMan() {
        out.println(gameStage[attemptNumber]);
        out.println(currentResponse);
    }

    public boolean checkLetter(char letter) {
        boolean answer = false;

        String wordCheck = word.word().toLowerCase();

        if (wordCheck.indexOf(letter) >= 0) {
            for (int i = 0; i < wordCheck.length(); i++) {
                if (wordCheck.charAt(i) == letter) {
                    currentResponse.setCharAt(i, letter);
                }
            }
            answer = true;
        } else {
            mistakesNumber++;
            if (mistakesNumber == 2
                    && word.help() != null) {
                out.println("ПОДСКАЗКА: " + word.help());
            }
            attemptNumber++;
        }
        return answer;
    }

    public boolean isWin() {
        return currentResponse.toString().equals(word.word().toLowerCase());
    }

    public boolean isGameNotOver() {
        return attemptNumber < gameStage.length - 1 && !currentResponse.toString().equals(word.word().toLowerCase());
    }

}
