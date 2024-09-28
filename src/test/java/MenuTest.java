
import org.example.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MenuTest {
    private PrintStream out;
    private InputStream in;
    private ByteArrayOutputStream outputStream;
    private Menu menu;

    @BeforeEach
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        out = new PrintStream(outputStream);
        in = System.in;


        List<Word> words = getTestWords();
        List<Category> categories = getTestCategories();
        menu = new Menu(categories, words, out, in);
    }

    @Test
    public void testMenuConstructorWithNoCategoriesOrWords() {
        Menu menu = new Menu(Collections.emptyList(), Collections.emptyList(), out, in);
        assertThat(outputStream.toString()).isEqualTo("");
    }

    @Test
    public void testMenuConstructorWithOneCategoryAndWord() {
        Menu menu = new Menu(
                Arrays.asList(new Category("Test")),
                Arrays.asList(new Word("test", new Category("Test"), "help")),
                out,
                in
        );
        assertThat(outputStream.toString()).isEqualTo("");
    }

    @Test
    public void testMenuConstructorWithMultipleCategoriesAndWords() {
        Menu menu = new Menu(
                Arrays.asList(new Category("Category1"), new Category("Category2")),
                Arrays.asList(new Word("word1", new Category("Category1"), "help1"), new Word("word2", new Category("Category2"), "help2")),
                out,
                in
        );

        assertThat(outputStream.toString()).isEqualTo("");
    }


    private List<Word> getTestWords() {
        Category animals = new Category("Животные");
        Category fruits = new Category("Фрукты");
        Category vegetables = new Category("Овощи");

        return List.of(
                new Word("Кошка", animals, "Домашнее животное"),
                new Word("Собака", animals, "Лучший друг человека"),
                new Word("Яблоко", fruits, "Сочный фрукт"),
                new Word("Банан", fruits, "Желтый фрукт"),
                new Word("Морковь", vegetables, "Оранжевый овощ"),
                new Word("Картофель", vegetables, "Основной продукт питания")
        );
    }

    private List<Category> getTestCategories() {
        Category animals = new Category("Животные");
        Category fruits = new Category("Фрукты");
        Category vegetables = new Category("Овощи");

        return List.of(animals, fruits, vegetables);
    }

    @Test
    public void testMenuConstructorWithTestData() {
        List<Word> words = getTestWords();
        List<Category> categories = getTestCategories();

        Menu menu = new Menu(categories, words, out, in);
        assertThat(outputStream.toString()).isEqualTo("");
    }



    @Test
    public void testSelectRandomWordWithEmptyList() {
        List<Word> emptyWords = Collections.emptyList();
        Word result = menu.selectRandomWord(emptyWords);
        assertThat(result).isNull();
        assertThat(outputStream.toString()).contains("Нет доступных слов для выбранной категории. Игра завершена.");
    }



    @Test
    public void testSelectRandomWordWithAllWordsInvalid() {
        List<Word> words = Arrays.asList(
                new Word("Кот", new Category("Млекопитающие"), "Животное"),
                new Word("Собака", new Category("Млекопитающие"), "Животное")
        );
        Word result = menu.selectRandomWord(words);
        assertThat(result).isNull();
        assertThat(outputStream.toString()).contains("Нет доступных слов для выбранной категории. Игра завершена.");
    }


    @Test
    public void testGetListWords() {
        Category testCategory = new Category("test");

        List<Word> words = List.of(
                new Word("test", testCategory, "test description")
        );
        List<Category> categories = List.of(testCategory);

        Menu menu = new Menu(categories, words, out, in);

        Word result = menu.selectRandomWord(menu.getWords());

        assertThat(result).isNotNull();
        assertThat(result.word()).isEqualTo("test");
    }


    @Test
    public void testSelectValidCategory() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);

        when(userInputHandler.getUserInput()).thenReturn("1");

        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Яблоко", categories.get(0), "Описание яблока"));

        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isEqualTo(categories.get(0));
    }

    @Test
    public void testSelectInvalidInput() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream input = new ByteArrayInputStream("abc\n1\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Яблоко", categories.get(0), "Описание яблока"));
        Menu menu = new Menu(categories, words, out, input);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isEqualTo(categories.get(0));
        verify(out).println("Неверный ввод. Пожалуйста, введите номер.");
    }

    @Test
    public void testSelectSecondCategory() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream input = new ByteArrayInputStream("2\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Морковь", categories.get(1), "Описание моркови"));
        Menu menu = new Menu(categories, words, out, input);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isEqualTo(categories.get(1));
    }

    @Test
    public void testSelectRandomCategory() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream input = new ByteArrayInputStream("\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Яблоко", categories.get(0), "Описание яблока"));
        Menu menu = new Menu(categories, words, out, input);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isIn(categories);
    }


    @Test
    public void testSelectInvalidCategoryNumber() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream input = new ByteArrayInputStream("3\n1\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Яблоко", categories.get(0), "Описание яблока"));
        Menu menu = new Menu(categories, words, out, input);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isEqualTo(categories.get(0));
        verify(out).println("Неверный номер категории. Пожалуйста, попробуйте снова.");
    }




    @Test
    public void testSelectEmptyInput() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream input = new ByteArrayInputStream("\n".getBytes());
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        List<Category> categories = List.of(new Category("Фрукты"), new Category("Овощи"));
        List<Word> words = List.of(new Word("Яблоко", categories.get(0), "Описание яблока"));
        Menu menu = new Menu(categories, words, out, input);

        menu.selectCategory();

        assertThat(menu.getSelectedCategory()).isIn(categories);
    }


    @Test
    public void testSelectValidDifficultyInput() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("1");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        assertThat(menu.getDifficulty()).isEqualTo(1);
        verify(out).println("Выберите уровень сложности (1-легкий, 2-средний, 3-сложный) или нажмите Enter для случайного выбора:");
    }

    @Test
    public void testSelectValidDifficultyInput2() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("3");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        assertThat(menu.getDifficulty()).isEqualTo(3);
        verify(out).println("Выберите уровень сложности (1-легкий, 2-средний, 3-сложный) или нажмите Enter для случайного выбора:");
    }


    @Test
    public void testSelectInvalidDifficultyInput() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("7");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        int difficulty = menu.getDifficulty();
        assertThat(difficulty).isGreaterThanOrEqualTo(1);
        verify(out).println("Неверно выбран уровень сложности. Сложность была автоматически установлена на лёгкую.");
    }


    @Test
    public void testSelectInvalidDifficultyInput2() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("-1");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        int difficulty = menu.getDifficulty();
        assertThat(difficulty).isGreaterThanOrEqualTo(1);
        verify(out).println("Неверно выбран уровень сложности. Сложность была автоматически установлена на лёгкую.");
    }


    @Test
    public void testSelectEmptyDifficultyInput() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        int difficulty = menu.getDifficulty();
        assertThat(difficulty).isGreaterThanOrEqualTo(1);
        verify(out).println("Неверный ввод. Сложность была автоматически установлена на лёгкую.");
    }



    @Test
    public void testSelectInvalidDifficultyInput3() throws IOException {
        PrintStream out = mock(PrintStream.class);
        UserInputHandler userInputHandler = mock(UserInputHandler.class);
        when(userInputHandler.getUserInput()).thenReturn("dfgdsfg");
        List<Category> categories = Arrays.asList(new Category("Фрукты"));
        List<Word> words = Arrays.asList(new Word("Яблоко", categories.get(0), "Сочный фрукт"));
        Menu menu = new Menu(categories, words, out, System.in);
        menu.setUserInputHandler(userInputHandler);
        menu.selectDifficulty();
        int difficulty = menu.getDifficulty();
        assertThat(difficulty).isGreaterThanOrEqualTo(1);
        verify(out).println("Неверный ввод. Сложность была автоматически установлена на лёгкую.");
    }

    @Test
    public void testDisplayMenuWithEmptyCategoriesAndWords() {
        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream("1\n".getBytes());
        Menu menu = new Menu(Arrays.asList(), Arrays.asList(), out, in);

        menu.showMenu();

        verify(out).println("Нет доступных категорий или слов для игры. Игра завершена.");
    }

    @Test
    public void testDisplayMenuPlayOption() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream("1\n".getBytes());
        Menu menu = new Menu(Arrays.asList(new Category("Фрукты")), Arrays.asList(new Word("Яблоко", new Category("Фрукты"), "Сочный фрукт")), out, in);

        PlayGame mockPlayGame = mock(PlayGame.class);
        menu.setPlayGame(mockPlayGame);
        menu.setDifficulty(-1);
        menu.showMenu();
        verify(out).println("Начинаем игру с уровнем сложности: " + menu.getDifficulty());
        verify(out).println(" и категорией: " + menu.getSelectedCategory().name());
        verify(mockPlayGame, never()).start(any());
    }


    @Test
    public void testDisplayMenuWithEmptyWordName() throws IOException {
        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream("1\n".getBytes());

        Category animals = new Category("Животные");
        List<Category> categories = List.of(animals);
        List<Word> words = List.of(
                new Word(null, animals, "Домашнее животное"),
                new Word(null, animals, "Лучший друг человека"));

        Menu menu = new Menu(categories, words, out, in);

        PlayGame mockPlayGame = mock(PlayGame.class);
        menu.setPlayGame(mockPlayGame);
        menu.setDifficulty(-1);
        menu.showMenu();
        verify(out).println("Начинаем игру с уровнем сложности: " + menu.getDifficulty());
        verify(out).println(" и категорией: " + menu.getSelectedCategory().name());
        verify(out).println("Нет доступных слов для выбранной категории. Игра завершена.");
        verify(out).println("Не найдено подходящее слово. Игра завершена.");
        verify(mockPlayGame, never()).start(any());
    }



    @Test
    public void testDisplayMenuWithEmptyWords() {
        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream("1\n".getBytes());

        Category animals = new Category("Животные");
        List<Category> categories = List.of(animals);
        List<Word> words = new ArrayList<>();

        Menu menu = new Menu(categories, words, out, in);

        PlayGame mockPlayGame = mock(PlayGame.class);
        menu.setPlayGame(mockPlayGame);
        menu.setDifficulty(-1);
        menu.showMenu();
        verify(out).println("Нет доступных категорий или слов для игры. Игра завершена.");
        verify(mockPlayGame, never()).start(any());
    }


    @Test
    public void testDisplayMenuWithEmptyCategories() {
        PrintStream out = mock(PrintStream.class);
        InputStream in = new ByteArrayInputStream("1\n".getBytes());

        Category animals = new Category("Животные");
        List<Word> words = List.of(
                new Word(null, animals, "Домашнее животное"),
                new Word(null, animals, "Лучший друг человека"));

        Menu menu = new Menu(new ArrayList<>(), words, out, in);

        PlayGame mockPlayGame = mock(PlayGame.class);
        menu.setPlayGame(mockPlayGame);
        menu.setDifficulty(-1);
        menu.showMenu();
        verify(out).println("Нет доступных категорий или слов для игры. Игра завершена.");
        verify(mockPlayGame, never()).start(any());
    }


}
