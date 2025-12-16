package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WordleDictionaryTest {



    @BeforeEach
    public void setUp() throws IOException {


        if (Wordle.printWriter == null) {

            Wordle.printWriter = new MyLogWriter();
        }
    }

    @AfterEach
    public void tearDown() throws IOException {

        if (Wordle.printWriter != null) {
            Wordle.printWriter.closeWriter();
        }
    }



    @Test
    @DisplayName("Проверка успешной загрузки слов из существующего файла")
    public void testDictionaryLoadsWordsFromFile() throws IOException {

        WordleDictionary dictionary = new WordleDictionary();
        List<String> loadedWords = dictionary.getWords();

        assertNotNull(loadedWords);
        assertEquals(67774, loadedWords.size(), "Должны загрузиться все 67774 тестовых слова.");

    }
}

