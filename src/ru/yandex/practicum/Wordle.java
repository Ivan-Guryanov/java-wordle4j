package ru.yandex.practicum;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

/*
в главном классе нам нужно:
    +создать лог-файл (он должен передаваться во все классы)
    -создать загрузчик словарей WordleDictionaryLoader
    +загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static MyLogWriter printWriter = null;
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        int steps = -1;
        int symbol = -1;
        int choice = -1;
        int choice1 = 1;

        try {
            printWriter = new MyLogWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        printWriter.closeWriter();
        printWriter.newFile();
        printWriter.writeData("Программа запущена.");

        WordleDictionary dictionary = new WordleDictionary();
        ArrayList<String> words = new ArrayList<>(dictionary.getWords());

        choice = start();

        switch (choice) {
            case 1:
                printWriter.writeData("Выбрана стандартная игра. 5 букв, 6 ходов.");
                steps = 6;
                symbol = 5;
                while (choice1 == 1) {
                    WordleGame wordleGame = new WordleGame(steps, newDictionaryGame(words, symbol));
                    wordleGame.game();
                    choice1 = gameOver();
                }
                System.out.println("Выход!");
                break;
            case 2:
                printWriter.writeData("Выбрана игра с пользовательскими параметрами.");
                System.out.println("Введите количество букв в слове:");
                try {
                    symbol = scanner.nextInt();
                } catch (NumberFormatException e) {
                    System.out.println("Нужно было ввести целое число");
                }
                System.out.println("Введите количество ходов:");
                try {
                    steps = scanner.nextInt();
                } catch (NumberFormatException e) {
                    System.out.println("Нужно было ввести целое число");
                }
                printWriter.writeData("Параметры игры: " + symbol + "-букв в слове, " + steps + "-ходов");
                while (choice1 == 1) {
                    WordleGame wordleGame = new WordleGame(steps, newDictionaryGame(words, symbol));
                    wordleGame.game();
                    choice1 = gameOver();
                }
                System.out.println("Выход!");
                break;
            default:
                System.out.println("Выход.");
        }
    }

    public static int start() {
        int choice = -1;
        System.out.println("Добро пожаловать! \n" +
                "Если хотите сыграть в классическую игру нажмите - 1;\n" +
                "Если хотите сыграть в игру со своими параметрами нажмите - 2;\n" +
                "Если хотите выйти нажмите любой другой символ.");

        choice = scanner.nextInt();

        return choice;

    }

    public static int gameOver() {
        int choice1 = -1;
        System.out.println("Ходите сыграть еще? \n" +
                "Да - 1;\n" +
                "Нет - любой другой символ:");
        try {
            choice1 = scanner.nextInt();

        } catch (InputMismatchException e) {
            System.out.println("Выход!");
        }
        if (choice1 == 1) {
            printWriter.writeData("*********************************************************Продолжаем игать!");
        }
        return choice1;
    }

    public static ArrayList<String> newDictionaryGame(ArrayList<String> words, int symbol) {
        ArrayList<String> wordsGame = new ArrayList<>();
        for (String word : words) {
            if (symbol == word.length()) {
                wordsGame.add(word);
            }
        }
        printWriter.writeData("Создан словарь из слов содержащих " + symbol + " букв. Всего вышло "
                + wordsGame.size() + " слов.");
        return wordsGame;
    }


}
