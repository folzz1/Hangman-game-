package org.example;

import java.io.PrintStream;


@SuppressWarnings("lombok")
public class GameLogic {
    private final PrintStream out; // поток вывода
    private final String[] gameStage; // стадии игры
    private StringBuilder currentResponse; // текущий ответ игрока
    private int attemptNumber; // номер попытки
    private Word word; // выбранное слово
    private int mistakesNumber;

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

    public boolean check(char letter) {
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

    public boolean isGameOver() {
        return attemptNumber < gameStage.length - 1 && !currentResponse.toString().equals(word.word().toLowerCase());
    }

    public Word getWord() {
        return word;
    }

    public int getAttemptNumber() {
        return attemptNumber;
    }

    public StringBuilder getCurrentResponse() {
        return currentResponse;
    }

    public int getMistakesNumber() {
        return mistakesNumber;
    }

    public String[] getGameStage() {
        return gameStage;
    }

    public void setCurrentResponse(StringBuilder currentResponse) {
        this.currentResponse = currentResponse;
    }

    public void setAttemptNumber(int attemptNumber) {
        this.attemptNumber = attemptNumber;
    }

    public void setMistakesNumber(int mistakesNumber) {
        this.mistakesNumber = mistakesNumber;
    }
}
