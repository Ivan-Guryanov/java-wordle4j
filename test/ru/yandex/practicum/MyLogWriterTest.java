package ru.yandex.practicum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class MyLogWriterTest {

    private static String testFileName;
    private Path testFilePath;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        Field fileNameField = MyLogWriter.class.getDeclaredField("fileName");
        fileNameField.setAccessible(true);
        testFileName = (String) fileNameField.get(null);
        testFilePath = Paths.get(testFileName);

        try {
            Files.deleteIfExists(testFilePath);
        } catch (IOException e) {
            fail("Не удалось удалить файл перед тестом: " + e.getMessage());
        }
    }

    @AfterEach
    public void tearDown() {

        try {
            Files.deleteIfExists(testFilePath);
        } catch (IOException e) {
            System.err.println("Ошибка при очистке файла после теста: " + e.getMessage());
        }
    }

    @Test
    @DisplayName("Проверка конструктора: файл создается при инициализации")
    public void testConstructorCreatesFile() throws IOException {
        // Конструктор должен создать или открыть файл
        MyLogWriter writer = new MyLogWriter();

        assertTrue(Files.exists(testFilePath), "Файл должен существовать после инициализации MyLogWriter");

        writer.closeWriter();
    }


    @Test
    @DisplayName("Проверка closeWriter(): файл должен быть доступен для чтения после закрытия")
    public void testCloseWriter() throws IOException {
        MyLogWriter writer = new MyLogWriter();
        writer.writeData("Тестовая запись");

        // Закрываем ресурс
        writer.closeWriter();

        // Попытка прочитать файл после закрытия не должна вызывать ошибок
        String content = Files.readString(testFilePath);
        assertFalse(content.isEmpty(), "Данные должны сохраниться в файле после закрытия писателя.");
    }

    @Test
    @DisplayName("Проверка deleteFile(): файл должен быть удален")
    public void testDeleteFileMethod() throws IOException {
        MyLogWriter writer = new MyLogWriter();
        writer.writeData("Какие-то данные");
        assertTrue(Files.exists(testFilePath), "Файл должен существовать до удаления.");

        // Удаляем файл
        writer.deleteFile();

        assertFalse(Files.exists(testFilePath), "Файл должен быть удален после вызова deleteFile().");

        // Note: deleteFile() вызывает closeWriter() внутри себя, что корректно.
    }
}