package org.example;

import java.util.List;

public class DefaultWordProvider implements WordProvider {

    private static final String ANIMALS = "Животные";
    private static final String FRUITS = "Фрукты";
    private static final String VEGETABLES = "Овощи";

    @Override
    public List<Word> getWords() {
        Category animals = new Category(ANIMALS);
        Category fruits = new Category(FRUITS);
        Category vegetables = new Category(VEGETABLES);

        return List.of(
                new Word("Кошка", animals, "Домашнее животное"),
                new Word("Собака", animals, "Лучший друг человека"),
                new Word("Яблоко", fruits, "Сочный фрукт"),
                new Word("Банан", fruits, "Желтый фрукт"),
                new Word("Морковь", vegetables, "Оранжевый овощ"),
                new Word("Картофель", vegetables, "Основной продукт питания")
        );
    }

    @Override
    public List<Category> getCategories() {
        return List.of(
                new Category(ANIMALS),
                new Category(FRUITS),
                new Category(VEGETABLES)
        );
    }
}