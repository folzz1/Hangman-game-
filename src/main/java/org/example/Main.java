package org.example;

import java.util.List;
public class Main {
    public static void main(String[] args) {
        // Создаем категории
        Category animals = new Category("Животные");
        Category fruits = new Category("Фрукты");
        Category vegetables = new Category("Овощи");

        // Создаем слова
        List<Word> words = List.of(
                new Word("Кошка", animals, "Домашнее животное"),
                new Word("Собака", animals, "Лучший друг человека"),
                new Word("Яблоко", fruits, "Сочный фрукт"),
                new Word("Банан", fruits, "Желтый фрукт"),
                new Word("Морковь", vegetables, "Оранжевый овощ"),
                new Word("Картофель", vegetables, "Основной продукт питания")
        );

        // Создаем список категорий
        List<Category> categories = List.of(animals, fruits, vegetables);

        // Создаем меню и запускаем его
        Menu menu = new Menu(categories, words, System.out, System.in);
        menu.displayMenu();
    }
}