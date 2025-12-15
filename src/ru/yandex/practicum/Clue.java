package ru.yandex.practicum;

import java.util.ArrayList;
import java.util.Random;

public class Clue {
    private ArrayList<String> clue = new ArrayList<>();

    private Random random = new Random();


    public ArrayList<String> getClue() {
        return clue;
    }

    public String randomGetWord() {
        return clue.get(random.nextInt(0, clue.size()));
    }

    public Clue(ArrayList<String> clue) {
        this.clue = clue;
    }

    public void listOfWords(String word, char[] charArray) {
        char[] wordSpelled = word.toCharArray();                //слово
        char[] serviceSymbols = charArray;                   //служебные символы

        int numberOfGreen = 0;                              // отгаданы
        int numberOfYellow = 0;                             // есть в слове, но не на своем месте
        int numberOfGrays = 0;                              // нет в слове

        for (char ch : serviceSymbols) {
            if (ch == '+') {
                numberOfGreen++;
            }
            if (ch == '^') {
                numberOfYellow++;
            }
            if (ch == '-') {
                numberOfGrays++;
            }
        }

        clue = сhoiceByGreen(wordSpelled, serviceSymbols, numberOfGreen);
        clue = сhoiceByYellow(wordSpelled, serviceSymbols, numberOfYellow);
        clue = сhoiceByGrays(wordSpelled, serviceSymbols, numberOfGrays, numberOfGreen);
    }

    private ArrayList<String> сhoiceByGreen(char[] wordSpelled, char[] serviceSymbols, int numberOfGreen) {

        ArrayList<String> wordsOfGreen = new ArrayList<>();       //временный список для слов прошедших по +

        for (String words : clue) {
            char[] wordFromTheList = words.toCharArray();               //слово из списка
            int numberOfGreen1 = 0;                                     //+

            for (int i = 0; i < words.length(); i++) {
                if (wordFromTheList[i] == wordSpelled[i] && serviceSymbols[i] == '+') {
                    numberOfGreen1++;
                }
            }

            if (numberOfGreen1 == numberOfGreen) {
                wordsOfGreen.add(new String(wordFromTheList));
            }
        }

        Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\"): "
                + wordsOfGreen.size() + " слов.");

        return wordsOfGreen;
    }

    private ArrayList<String> сhoiceByYellow(char[] wordSpelled, char[] serviceSymbols, int numberOfYellow) {

        ArrayList<String> wordsOfYellow = new ArrayList<>();         //временный список проверенных по ^
        char[] ltersToCheck = new char[numberOfYellow];            //проверяемые символы на вхождение
        int cloud = 0;

        for (int i = 0; i < serviceSymbols.length; i++) {
            if (serviceSymbols[i] == '^') {
                ltersToCheck[cloud] = wordSpelled[i];
                cloud++;
            }
        }

        for (String words : clue) {
            char[] wordFromTheList = words.toCharArray();                 //слово из списка
            for (int i = 0; i < wordFromTheList.length; i++) {
                if (serviceSymbols[i] == '+') {
                    wordFromTheList[i] = '+';
                }
            }
            if (containsAllChars(wordFromTheList, ltersToCheck)) {
                wordsOfYellow.add(words);
            }
        }

        Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\" и \"^\"): "
                + wordsOfYellow.size() + " слов.");

        return wordsOfYellow;

    }

    private ArrayList<String> сhoiceByGrays(char[] wordSpelled, char[] serviceSymbols,
                                            int numberOfGrays, int numbertOfGreen) {

        ArrayList<String> wordsOfGrays = new ArrayList<>();                 //временный список проверенных по -
        char[] missingLetters = new char[numberOfGrays];                         //символы которых нет
        int cloud = 0;

        for (int i = 0; i < serviceSymbols.length; i++) {
            if (serviceSymbols[i] == '-') {
                missingLetters[cloud] = wordSpelled[i];
                cloud++;
            }
        }

        for (String words : clue) {
            boolean bo = true;
            char[] wordFromTheList = words.toCharArray();                                       //слово из списка
            char[] lettersBeingChecked = new char[wordSpelled.length - numbertOfGreen];         //проверяемые символы
            int cloud1 = 0;
            for (int i = 0; i < wordFromTheList.length; i++) {
                if (serviceSymbols[i] != '+') {
                    lettersBeingChecked[cloud1] = wordFromTheList[i];
                    cloud1++;
                }
            }

            for (int i = 0; i < missingLetters.length; i++) {
                for (int j = 0; j < lettersBeingChecked.length; j++) {
                    if (missingLetters[i] == lettersBeingChecked[j]) {
                        bo = false;
                    }
                }
            }

            if (bo) {
                wordsOfGrays.add(words);
            }
        }

        Wordle.printWriter.writeData("Создан список возможных вариантов ответов(по \"+\" и \"^\" и \"-\"): "
                + wordsOfGrays.size() + " слов.");

        return wordsOfGrays;
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
