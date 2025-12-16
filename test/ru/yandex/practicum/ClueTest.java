package ru.yandex.practicum;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ClueTest {

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        if (Wordle.printWriter == null) {
            Wordle.printWriter = new MyLogWriter();
        }

    }

    @AfterEach

    @Test
    @DisplayName("Проверка randomGetWord возвращает слово из списка")
    public void testRandomGetWord() {
        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "СЛОВО", "КВАРК", "АВТОР", "СПОРТ", "ЛАЙКА"
        ));
        Clue clueInstance = new Clue(initialDictionary);
        String randomWord = clueInstance.randomGetWord();

        assertNotNull(randomWord);
        assertTrue(initialDictionary.contains(randomWord));
    }

    @Test
    @DisplayName("Проверка listOfWords уберает из списка не подходящие слова")
    public void testlistOfWords() {
        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "СЛОВО", "КВАРК", "АВТОР", "СПОРТ", "ЛАЙКА"
        ));
        char[] charArray = {'+', '^', '-', '+', '-'};
        Clue clueInstance = new Clue(initialDictionary);
        clueInstance.listOfWords("СЕКТА", charArray);

        assertEquals(1, clueInstance.getClue().size());
        assertTrue(clueInstance.getClue().contains("СВЕТО"));

    }

    @Test
    @DisplayName("Проверка choiceByGreen выбирает слова по +")
    void testChoiceByGreen() {
        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "СЛОВО", "КВАРК", "АВТОР", "СПОРТ", "ЛАЙКА"
        ));
        char[] word = {'С','Л','Ю','Д','А'};
        char[] charArray = {'+', '+', '-', '-', '-'};
        int numberOfGreen = 2;
        Clue clueInstance = new Clue(initialDictionary);

        assertEquals(1, clueInstance.choiceByGreen(word, charArray, numberOfGreen).size());
        assertTrue(clueInstance.choiceByGreen(word, charArray, numberOfGreen).contains("СЛОВО"));
    }

    @Test
    @DisplayName("Проверка choiceByGreen выбирает слова по ^")
    void testChoiceByYellow() {
        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "СЛОВО", "КВАРК", "БРОНЯ", "СПОРТ", "ЛАЙКА"
        ));
        char[] word = {'А','Ф','Ф','Ф','Ф'};
        char[] charArray = {'^', '-', '-', '-', '+'};
        int numberOfYellow = 1;
        Clue clueInstance = new Clue(initialDictionary);

        //clue = choiceByYellow(wordSpelled, serviceSymbols, numberOfYellow);

        assertEquals(2, clueInstance.choiceByYellow(word, charArray, numberOfYellow).size());
        assertTrue(clueInstance.choiceByYellow(word, charArray, numberOfYellow).contains("ЛАЙКА"));
        assertTrue(clueInstance.choiceByYellow(word, charArray, numberOfYellow).contains("КВАРК"));
    }

    @Test
    @DisplayName("Проверка choiceByGrays выбирает слова по -")
    void testChoiceByGrays() {
        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "БЮВЕТ", "КВАРК", "БРОНЯ", "СПОРТ", "ЛАЙКА"
        ));
        char[] word = {'К','Р','А','Ф','Т'};
        char[] charArray = {'-', '-', '-', '-', '+'};
        int numberOfGrays = 4;
        int numberOfGreen = 1;
        Clue clueInstance = new Clue(initialDictionary);

        //clue = choiceByYellow(wordSpelled, serviceSymbols, numberOfYellow);

        assertEquals(3, clueInstance.choiceByGrays(word, charArray, numberOfGrays, numberOfGreen).size());
        assertTrue(clueInstance.choiceByGrays(word, charArray, numberOfGrays, numberOfGreen).contains("БЮВЕТ"));
        assertFalse(clueInstance.choiceByGrays(word, charArray, numberOfGrays, numberOfGreen).contains("КВАРК"));
    }

    @Test
    @DisplayName("Проверка randomGetWord возвращает слово из списка")
    public void testContainsAllChars() {
        char[] main = {'A', 'B', 'C', 'D'};
        char[] toCheck = {'B', 'D'};
        char[] toCheckFail = {'B', 'Z'};

        assertTrue(Clue.containsAllChars(main, toCheck), "Должно найти все символы");
        assertFalse(Clue.containsAllChars(main, toCheckFail), "Не должно найти отсутствующий Z");
    }


}