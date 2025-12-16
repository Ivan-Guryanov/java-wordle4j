package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;
    private int steps;
    private ArrayList<String> dictionary;
    private ArrayList<String> in = new ArrayList<>();

    Random random = new Random();
    Scanner scanner = new Scanner(System.in);

    private Clue clue1;


    public WordleGame(int steps, ArrayList<String> dictionary) {

        this.steps = steps;
        this.dictionary = dictionary;
        this.answer = dictionary.get(random.nextInt(0, dictionary.size()));
        Wordle.printWriter.writeData("Загадано слово: " + this.answer);
        clue1 = new Clue(dictionary);
    }

    public void game() {

        String word = "";
        System.out.println("Загадано слово из " + answer.length() + " букв.\n" +
                "У вас есть " + steps + " попыток.");

        while (steps > 0) {


            word = "";
            System.out.println("Введите слово или если хотите получить подсказку \"?\":");

            try {
                word = scanner.nextLine();
                in.add(word);

                Wordle.printWriter.writeData("Пользователь ввел слово: " + word);
            } catch (Exception e) {
                System.out.println("Ошибка ввода данных. Попробуйте снова.");
                continue;
            }
            if (word.length() != answer.length()) {
                if (word.equals("?")) {
                    word = clue1.randomGetWord();
                    Wordle.printWriter.writeData("Введено слово подсказка: " + word);
                    in.add(word);
                } else {
                    System.out.println("Длина слова не соответствует требованиям.");
                    Wordle.printWriter.writeData("Длина слова не соответствует требованиям.");
                    continue;
                }
            }
            if (!dictionary.contains(word)) {
                System.out.println("Слова нет в словаре.");
                continue;
            }

            if (word.equals(answer)) {
                print();
                System.out.println("Поздравляем! Вы отгадали слово)");
                Wordle.printWriter.writeData("Пользователь отгадал слово.");
                break;
            }
            steps--;
            Wordle.printWriter.writeData("Пользователь сделал ход. Осталось: " + steps + " ходов.");
            wordCheck(word);

        }
        if (!word.equals(answer)) {
            System.out.println("Вам не удалось отгадать слово");
        }


    }

    public void wordCheck(String word) {

        String clue = word;
        char[] charArray = word.toCharArray();
        char[] charArray1 = answer.toCharArray();
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) == answer.charAt(i)) {
                charArray[i] = '+';
                charArray1[i] = '+';
            }
        }

        for (int i = 0; i < word.length(); i++) {
            if (charArray[i] != '+') {
                for (char ch : charArray1) {
                    if (ch == word.charAt(i)) {
                        charArray[i] = '^';
                    }
                }
            }
        }

        for (int i = 0; i < word.length(); i++) {
            if (charArray[i] != '+' && charArray[i] != '^') {
                charArray[i] = '-';
            }
        }

        Wordle.printWriter.writeData("Результат: " + new String(charArray));
        in.add(new String(charArray));
        clue1.getClue().remove(word);
        clue1.listOfWords(clue, charArray);
        print();

    }

    void print() {
        System.out.println("Ваши результаты:\n" +
                "*************");
        for (int i = 0; i < in.size(); i++) {
            System.out.println(in.get(i));
        }
        System.out.println("*************");

    }

}
