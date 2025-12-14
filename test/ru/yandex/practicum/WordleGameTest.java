package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class WordleGameTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private static final ArrayList<String> TEST_DICTIONARY = new ArrayList<>(Arrays.asList("АВТОР", "ТЕПЛО", "СВЕТО", "РУЧКА"));

    @BeforeEach
    public void setUp() throws IOException, NoSuchFieldException, IllegalAccessException {
        if (Wordle.printWriter == null) {
            Wordle.printWriter = new MyLogWriter();
        }

        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        if (Wordle.printWriter != null) {
            Wordle.printWriter.closeWriter();
        }
    }

    private void setGameAnswer(WordleGame game, String answer) throws NoSuchFieldException, IllegalAccessException {
        Field field = WordleGame.class.getDeclaredField("answer");
        field.setAccessible(true);
        field.set(game, answer);
    }

    private void simulateUserInput(String inputString, WordleGame gameInstance) {
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        // Необходимо переинициализировать Scanner класса WordleGame, чтобы он использовал новый System.in
        gameInstance.scanner = new Scanner(in);
    }


    @Test
    @DisplayName("wordCheck: Частичное совпадение ('+', '^', '-')")
    public void testWordCheck_PartialMatch() throws NoSuchFieldException, IllegalAccessException {
        WordleGame game = new WordleGame(6, TEST_DICTIONARY);
        setGameAnswer(game, "АВТОР");

        game.wordCheck("ТЕПЛО");

        ArrayList<String> inList = (ArrayList<String>) getPrivateField(game, "in");
        assertEquals("^---^", inList.get(0));
    }

    @Test
    @DisplayName("game: Неверное слово, затем выигрыш")
    public void testGame_WinOnSecondAttempt() throws NoSuchFieldException, IllegalAccessException {
        WordleGame game = new WordleGame(6, TEST_DICTIONARY);
        String secretAnswer = "ТЕПЛО";
        setGameAnswer(game, secretAnswer);

        // Симулируем ввод неверного слова, затем верного
        String input = "РУЧКА\n" + secretAnswer + "\n";
        simulateUserInput(input, game);

        game.game();

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Поздравляем! Вы отгадали слово)"));

        // Проверяем, что шаги уменьшились
        assertEquals(5, getPrivateField(game, "steps"));

        // Проверяем, что в выводе есть обе попытки и фидбек
        assertTrue(consoleOutput.contains("РУЧКА"));
        assertTrue(consoleOutput.contains("-----")); // Фидбек для РУЧКА
        assertTrue(consoleOutput.contains("ТЕПЛО"));
    }

    @Test
    @DisplayName("game: Ввод слова неверной длины, затем корректный ввод")
    public void testGame_InvalidLengthInput() throws NoSuchFieldException, IllegalAccessException {
        WordleGame game = new WordleGame(6, TEST_DICTIONARY);
        setGameAnswer(game, "АВТОР"); // Длина 5

        // Вводим "СВЕТО" (длина 5) - это нормально.
        // Вводим "ДЛИННОЕ" (длина 7) - должно выдать ошибку и попросить ввод снова.
        // Вводим "АВТОР" (выигрыш)
        String input = "СВЕТО\n" + "ДЛИННОЕ\n" + "АВТОР\n";
        simulateUserInput(input, game);

        game.game();

        String consoleOutput = outContent.toString();
        assertTrue(consoleOutput.contains("Длина слова не соответствует требованиям."));
        assertTrue(consoleOutput.contains("Поздравляем! Вы отгадали слово)"));

        // Проверяем, что попытка с неверной длиной не уменьшила счетчик шагов
        // Должно было быть 6 -> 5 (за СВЕТО) -> 5 (за ДЛИННОЕ, ошибка) -> игра закончена
        assertEquals(5, getPrivateField(game, "steps"));
    }


    // Вспомогательный метод для получения доступа к приватным полям (для проверок состояния)
    private Object getPrivateField(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }
}