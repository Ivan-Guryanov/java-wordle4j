package ru.yandex.practicum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private List<String> words;

    public WordleDictionary() {
        this.words = new ArrayList<>();
        try (BufferedReader br =
                     new BufferedReader(new InputStreamReader(new FileInputStream("words_ru.txt"),
                             StandardCharsets.UTF_8))) {

            while (br.ready()) {
                this.words.add(br.readLine());

            }
            Wordle.printWriter.writeData("Загружен полный список слов: " + words.size());
        } catch (FileNotFoundException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }

}
