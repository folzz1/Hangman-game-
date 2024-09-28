package org.example;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        WordProvider wordProvider = new DefaultWordProvider();

        List<Category> categories = wordProvider.getCategories();
        List<Word> words = wordProvider.getWords();

        Menu menu = new Menu(categories, words, System.out, System.in);
        menu.showMenu();
    }
}
