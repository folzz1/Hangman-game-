package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Menu {
    private static final int MIN_WORD_LENGTH = 3;
    private static final int MAX_DIFFICULTY = 3;
    private static final int MIN_DIFFICULTY = 1;

    private final PrintStream out;
    private final InputStream in;
    private final List<Category> categories;
    private final List<Word> words;
    private final Random random;
    private int difficulty;
    private Category selectedCategory;
    private PlayGame playGame;

    public Menu(List<Category> categories, List<Word> words, PrintStream out, InputStream in) {
        this.out = out;
        this.in = in;
        this.categories = categories;
        this.random = new Random();
        //this.difficulty = random.nextInt(MAX_DIFFICULTY) + MIN_DIFFICULTY;
        this.words = words;
        //this.selectedCategory = categories.get(random.nextInt(categories.size()));

        // Проверка на пустые списки
        if (categories.isEmpty() || words.isEmpty()) {
            this.difficulty = 1;
            this.selectedCategory = null;
        } else {
            this.difficulty = random.nextInt(MAX_DIFFICULTY) + MIN_DIFFICULTY;
            this.selectedCategory = categories.get(random.nextInt(categories.size()));
        }

    }

    public void displayMenu() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        boolean startGame = false;

        if (categories.isEmpty() || words.isEmpty()) {
            out.println("Нет доступных категорий или слов для игры. Игра завершена.");
            return;
        }

        while (!startGame) {
            out.println("""
                              Меню
                 1 - Играть
                 2 - Выбрать уровень сложности
                 3 - Выбрать категорию слова
            """);

            try {
                String choice = reader.readLine();
                if (choice == null) {
                    out.println("Ошибка ввода. Пожалуйста, попробуйте снова.");
                    continue;
                }
                switch (choice) {
                    case "1":
                        startGame = true;
                        break;
                    case "2":
                        selectDifficulty(reader);
                        break;
                    case "3":
                        selectCategory(reader);
                        break;
                    default:
                        out.println("Неверный ввод. Пожалуйста, выберите снова.");
                }
            } catch (IOException e) {
                out.println("Произошла ошибка при чтении ввода.");
            }
        }

        out.println("Начинаем игру с уровнем сложности: " + difficulty);
        out.println(" и категорией: " + selectedCategory.name());

        final Word word = selectRandomWord(words);

        if (word == null || word.word().isEmpty()) {
            out.println("Не найдено подходящее слово. Игра завершена.");
        } else {
            if (difficulty != -1) {
                this.playGame = new PlayGame(word, difficulty);
                playGame.start(in);
            } else {
                out.println("Не указана сложность. Игра завершена.");
            }
        }

    }

    public void selectDifficulty(BufferedReader reader) throws IOException {
        out.println("Выберите уровень сложности (1-легкий, 2-средний, 3-сложный) "
                + "или нажмите Enter для случайного выбора:");
        String difficultyInput = reader.readLine();
        try {
            difficulty = Integer.parseInt(difficultyInput);
            if (difficulty < MIN_DIFFICULTY || difficulty > MAX_DIFFICULTY) {
                out.println("Неверно выбран уровень сложности. Сложность была автоматически установлена на лёгкую.");
                difficulty = MIN_DIFFICULTY;
            }
        } catch (NumberFormatException e) {
            out.println("Неверный ввод. Сложность была автоматически установлена на лёгкую.");
            difficulty = MIN_DIFFICULTY;
        }
    }

    public void selectCategory(BufferedReader reader) throws IOException {
        while (true) {
            out.println("Выберите категорию из списка или нажмите Enter для случайного выбора:");
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i) != null
                        && categories.get(i).name() != null
                        && !categories.get(i).name().isEmpty()) {
                    out.println((i + 1) + ". " + categories.get(i).name());
                }
            }
            String categoryInput = reader.readLine();
            if (categoryInput == null || categoryInput.isEmpty()) {
                selectedCategory = categories.get(random.nextInt(categories.size()));
                break;
            }
            try {
                int categoryIndex = Integer.parseInt(categoryInput) - 1;
                if (categoryIndex >= 0 && categoryIndex < categories.size()) {
                    if (categories.get(categoryIndex) != null
                            && categories.get(categoryIndex).name() != null
                            && !categories.get(categoryIndex).name().isEmpty()) {
                        selectedCategory = categories.get(categoryIndex);
                        break;
                    } else {
                        out.println("Выбранная категория недоступна. Пожалуйста, выберите другую.");
                    }
                } else {
                    out.println("Неверный номер категории. Пожалуйста, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                out.println("Неверный ввод. Пожалуйста, введите номер.");
            }
        }
    }


    public Word selectRandomWord(List<Word> words) {
        List<Word> filteredWords = words.stream()
                .filter(word -> word != null
                        && word.word() != null
                        && !word.word().isEmpty()
                        && word.category() != null
                        && word.category().equals(selectedCategory)
                        && word.word().length() >= MIN_WORD_LENGTH)
                .collect(Collectors.toList());

        if (filteredWords.isEmpty()) {
            out.println("Нет доступных слов для выбранной категории. Игра завершена.");
            return null;
        }

        return filteredWords.get(random.nextInt(filteredWords.size()));
    }


    public List<Word> getWords() {
        return words;
    }

    public Category getSelectedCategory() {
        return selectedCategory;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public void setPlayGame(PlayGame playGame) {
        this.playGame = playGame;
    }
}
