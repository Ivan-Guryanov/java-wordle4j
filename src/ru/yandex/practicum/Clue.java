package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Random;

public class Clue {
    private static ArrayList<String> clue = new ArrayList<>();

    Random random = new Random();


    public ArrayList<String> getClue() {
        return clue;
    }

    public String randomGetWord() {
        return clue.get(random.nextInt(0, clue.size()));
    }

    public Clue(ArrayList<String> clue) {
        this.clue = clue;
    }

    public static void listOfWords(String word, char[] charArray) {
        char[] charClue = word.toCharArray();           //слово
        char[] charClue1 = charArray;                   //служебные символы
        ArrayList<String> clue1 = new ArrayList<>();    //временный список
        ArrayList<String> clue2 = new ArrayList<>();    //временный список 2
        ArrayList<String> clue3 = new ArrayList<>();    //временный список 3

        int numberOfGuessed = 0;
        int numberOfGuessed2 = 0;
        int numberOfGuessed3 = 0;
        for (char char1 : charClue1) {
            if (char1 == '+') {
                numberOfGuessed++;
            }
        }
        for (char char1 : charClue1) {
            if (char1 == '^') {
                numberOfGuessed2++;
            }
        }
        for (char char1 : charClue1) {
            if (char1 == '-') {
                numberOfGuessed3++;
            }
        }

        try {
            for (String words : clue) {
                char[] charClue2 = words.toCharArray();               //слово из списка
                int numberOfGuessed1 = 0;                             //+

                for (int i = 0; i < word.length(); i++) {
                    if (charClue2[i] == charClue[i] && charClue1[i] == '+') {
                        numberOfGuessed1++;
                    }
                }

                if (numberOfGuessed1 == numberOfGuessed) {
                    clue1.add(new String(charClue2));
                }
            }
        } catch (NullPointerException e) {
            System.err.println("Произошла ошибка NullPointer.");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Произошла ошибка выхода за границы массива.");
        }

        clue = clue1;
        Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\"): "
                + clue1.size() + " слов.");

        try {

            char[] charClue4 = new char[numberOfGuessed2];     //проверяемые символы на вхождение
            int cloud = 0;

            for (int i = 0;  i < charClue1.length; i++) {
                if (charClue1[i] == '^') {
                    charClue4[cloud] = charClue[i];
                    cloud++;
                }
            }


            for (String words : clue) {
                char[] charClue5 = words.toCharArray();                 //слово из списка
                for (int i = 0; i < charClue5.length; i++) {
                    if (charClue1[i] == '+') {
                        charClue5[i] = '+';
                    }
                }
                if (containsAllChars(charClue5, charClue4)) {
                    clue2.add(words);
                }
            }

            clue = clue2;
            Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\" и \"^\"): "
                    + clue2.size() + " слов.");

        } catch (NullPointerException e) {
            System.err.println("Произошла ошибка NullPointer2.");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Произошла ошибка выхода за границы массива2.");
        }

        try {
            char[] charClue6 = new char[numberOfGuessed3];                      //символы которых нет

            int cloud = 0;

            for (int i = 0;  i < charClue1.length; i++) {
                if (charClue1[i] == '-') {
                    charClue6[cloud] = charClue[i];
                    cloud++;
                }
            }

            for (String words : clue) {
                boolean bo = true;
                char[] charClue7 = words.toCharArray();                             //слово из списка
                char[] charClue8 = new char[charClue.length - numberOfGuessed];     //проверяемые символы
                int cloud1 = 0;
                for (int i = 0;  i < charClue7.length; i++) {
                    if (charClue1[i] != '+') {
                        charClue8[cloud1] = charClue7[i];
                        cloud1++;
                    }
                }

                for (int i = 0; i < charClue6.length; i++) {
                    for (int j = 0; j < charClue8.length; j++) {
                        if (charClue6[i] == charClue8[j]) {
                            bo = false;
                        }
                    }
                } if (bo) {
                    clue3.add(words);
                }
            }

            clue = clue3;
            Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\" и \"^\" и \"-\"): "
                    + clue3.size() + " слов.");

        } catch (NullPointerException e) {
            System.err.println("Произошла ошибка NullPointer3.");
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Произошла ошибка выхода за границы массива3.");
        }
    }

    public static boolean containsChar(char[] array, char target) {
        for (char element : array) {
            if (element == target) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsAllChars(char[] mainArray, char[] charsToCheck) {
        for (char targetChar : charsToCheck) {

            if (!containsChar(mainArray, targetChar)) {
                return false;
            }
        }

        return true;
    }

}
