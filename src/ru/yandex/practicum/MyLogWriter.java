package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyLogWriter {
    private static final String fileName = "print_writer.txt";

    private Writer fileWriter;
    private int count = 0;

    public MyLogWriter() throws IOException {
        this.fileWriter = new FileWriter(fileName, true);
    }

    public void newFile() {

        try {
            Files.deleteIfExists(Path.of(fileName)); // Сначала удаляем, если есть
            Files.createFile(Path.of(fileName));

            this.fileWriter = new FileWriter(fileName, true);

        } catch (FileAlreadyExistsException e) {
            System.out.println("Ошибка: Файл  уже существует.");
        } catch (IOException e) {
            System.err.println("Произошла ошибка ввода-вывода: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void writeData(String line) {
        count++;
        try (FileWriter fw = new FileWriter(fileName, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println(count + ". " + line);
        } catch (IOException e) {
            System.out.println("Произошла ошибка при записи в файл: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void closeWriter() {
        try {
            if (fileWriter != null) {
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteFile() {
        closeWriter();

        Path pathToFile = Paths.get(fileName);

        try {
            boolean isDeleted = Files.deleteIfExists(pathToFile);

        } catch (IOException e) {
            System.out.println("Ошибка при удалении файла: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
