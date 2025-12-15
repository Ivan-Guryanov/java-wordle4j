package ru.yandex.practicum;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class WordleTest {

    private final InputStream originalIn = System.in;
    private final PrintStream originalOut = System.out;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setIn(originalIn);
        System.setOut(originalOut);
        if (Wordle.printWriter != null) {
            Wordle.printWriter.closeWriter();
        }
    }

    private void simulateUserInput(String inputString) {
        ByteArrayInputStream in = new ByteArrayInputStream(inputString.getBytes());
        System.setIn(in);
        Wordle.scanner = new Scanner(in);
    }

    @Test
    @DisplayName("Проверка функции start() при вводе '1'")
    public void testStart_ClassicChoice() {
        simulateUserInput("1\n");
        int result = Wordle.start();
        assertEquals(1, result, "start() должен вернуть 1 при вводе '1'");
        assertTrue(outContent.toString().contains("Добро пожаловать!"));
    }

    @Test
    @DisplayName("Проверка функции start() при вводе '2'")
    public void testStart_CustomChoice() {
        simulateUserInput("2\n");
        int result = Wordle.start();
        assertEquals(2, result, "start() должен вернуть 2 при вводе '2'");
    }

    @Test
    @DisplayName("Проверка функции start() при нечисловом вводе")
    public void testStart_InvalidInput() {
        simulateUserInput("abc\n");
        int result = Wordle.start();
        assertEquals(-1, result, "start() должен вернуть -1 при неверном вводе");
        assertTrue(outContent.toString().contains("Нужно ввести целое число!"));
    }


    @Test
    @DisplayName("Проверка функции gameOver() при вводе 'нет' (Не играть еще)")
    public void testGameOver_Exit() {
        simulateUserInput("нет\n"); // Любой нечисловой ввод
        int result = Wordle.gameOver();
        // При нечисловом вводе будет пойман InputMismatchException и result останется -1
        assertEquals(-1, result, "gameOver() должен вернуть -1 при нечисловом вводе");
        assertTrue(outContent.toString().contains("Выход!"));
    }

    @Test
    @DisplayName("Фильтрация словаря по длине 5 букв")
    public void testNewDictionaryGame_FilterByLength5() {
        try {
            Wordle.printWriter = new MyLogWriter();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> initialWords = new ArrayList<>(Arrays.asList("слово", "пять", "тест", "длинное", "джава"));
        int length = 5;
        ArrayList<String> result = Wordle.newDictionaryGame(initialWords, length);
        assertNotNull(result);
        assertEquals(2, result.size(), "Должно остаться 2 слова длиной 5");
        assertTrue(result.contains("слово"), "Слово 'слово' должно присутствовать");
        assertTrue(result.contains("джава"), "Слово 'джава' должно присутствовать");
        assertFalse(result.contains("пять"), "Слово 'пять' (длина 4) должно отсутствовать");
        assertFalse(result.contains("тест"), "Слово 'тест' (длина 4) должно отсутствовать");
        assertFalse(result.contains("длинное"), "Слово 'длинное' (длина 7) должно отсутствовать");
    }


    @Test
    @DisplayName("Запуск main() в режиме 'Выход' (любой другой ввод)")
    public void testMain_ExitPath() {
        simulateUserInput("3\n");
        Wordle.main(new String[]{});
        assertTrue(outContent.toString().contains("Выход."));
    }
}

