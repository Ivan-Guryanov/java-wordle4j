package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ClueTest {

    private ArrayList<String> getStaticClue() throws NoSuchFieldException, IllegalAccessException {
        Field field = Clue.class.getDeclaredField("clue");
        field.setAccessible(true);
        return (ArrayList<String>) field.get(null);
    }

    private void setStaticClue(ArrayList<String> newClueList) throws NoSuchFieldException, IllegalAccessException {
        Field field = Clue.class.getDeclaredField("clue");
        field.setAccessible(true);
        field.set(null, newClueList);
    }

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException, IOException {
        if (Wordle.printWriter == null) {
            Wordle.printWriter = new MyLogWriter();
        }

        ArrayList<String> initialDictionary = new ArrayList<>(Arrays.asList(
                "ТЕПЛО", "СВЕТО", "РУЧКА", "СЛОВО", "КВАРК", "АВТОР", "СПОРТ", "ЛАЙКА"
        ));
        setStaticClue(initialDictionary);
    }

    @AfterEach
    public void tearDown() {

    }


    @Test
    @DisplayName("Проверка randomGetWord возвращает слово из списка")
    public void testRandomGetWord() throws IllegalAccessException, NoSuchFieldException {
        Clue clueInstance = new Clue(getStaticClue()); // Создаем экземпляр с текущим словарем
        String randomWord = clueInstance.randomGetWord();

        assertNotNull(randomWord);
        assertTrue(getStaticClue().contains(randomWord));
    }

    @Test
    @DisplayName("Проверка containsChar")
    public void testContainsChar() {
        char[] array = {'a', 'b', 'c'};
        assertTrue(Clue.containsChar(array, 'b'));
        assertFalse(Clue.containsChar(array, 'd'));
    }

    @Test
    @DisplayName("Проверка containsAllChars")
    public void testContainsAllChars() {
        char[] main = {'a', 'b', 'c', 'd'};
        char[] check1 = {'a', 'c'};
        char[] check2 = {'a', 'z'};

        assertTrue(Clue.containsAllChars(main, check1));
        assertFalse(Clue.containsAllChars(main, check2));
    }
}