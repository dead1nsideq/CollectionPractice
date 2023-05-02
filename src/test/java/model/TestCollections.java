package model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestCollections {

    // 1 --------------------------------
    @Test
    void testPrintList() {
        list.forEach(System.out::println);
    }

    // 2 --------------------------------
    @Test
    void testChangeWeightOfFirstByOne() {
        //todo Изменить вес первой коробки на 1.
        HeavyBox box = new HeavyBox(1,2,3,5);
        box.setWeight(box.getWeight()-1);
        assertEquals(box, list.get(0));
    }

    // 3 --------------------------------
    @Test
    void testDeleteLast() {
        //todo Удалить предпоследнюю коробку.
        list.remove(list.size()-1);
        assertEquals(6, list.size());
        assertEquals(new HeavyBox(1,2,3,4), list.get(0));
        assertEquals(new HeavyBox(1,3,3,4), list.get(list.size()-2));
    }

    // 4 --------------------------------
    @Test
    void testConvertToArray() {
        //todo Получить массив содержащий коробки из коллекции тремя способами и вывести на консоль.
        HeavyBox[] arr = list.toArray(HeavyBox[]::new);
        // інший спосіб
        // list.toArray(new HeavyBox[0])
        assertArrayEquals(new HeavyBox[]{
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        }, arr);
    }

    // 5 --------------------------------
    @Test
    void testDeleteBoxesByWeight() {
        // todo удалить все коробки, которые весят 4
        list = list.stream().filter(s -> s.getWeight() != 4).collect(Collectors.toList());
        assertEquals(3, list.size());
    }

    // 6 --------------------------------
    @Test
    void testSortBoxesByWeight() {
        // отсортировать коробки по возрастанию веса. При одинаковом весе - по возрастанию объема
        list.sort(Comparator
                .comparingInt(HeavyBox::getWeight)
                .thenComparing(Box::getVolume));
        assertEquals(new HeavyBox(1,1,1,1), list.get(0));
        assertEquals(new HeavyBox(2,3,4,7), list.get(6));
        assertEquals(new HeavyBox(1,2,3,4), list.get(3));
        assertEquals(new HeavyBox(1,3,3,4), list.get(4));
    }

    // 7 --------------------------------
    @Test
    void testClearList() {
        list.clear();
        assertTrue(list.isEmpty());
    }

    // 8 --------------------------------
    @Test
    void testReadAllLinesFromFileToList() throws IOException {
        // todo Прочитать все строки в коллекцию
        List<String> lines = new ArrayList<>();
        while (reader.ready()) {
            String str = reader.readLine();
            lines.add(str);
        }
        assertEquals(19, lines.size());
        assertEquals("", lines.get(8));
    }

    // 9 --------------------------------
    @Test
    void testReadAllWordsFromFileToList() throws IOException {
        // todo прочитать все строки, разбить на слова и записать в коллекцию
        List<String> words = readAllWordsFromFileToList();
        words.forEach(System.out::println);
        assertEquals(257, words.size());
    }

    List<String> readAllWordsFromFileToList() throws IOException {
        List<String> lines = new ArrayList<>();
        while (reader.ready()) {
            String str = reader.readLine();
            for (String s : str.split(REGEXP)) {
                if (!s.isEmpty()) {
                    lines.add(s);
                }
            }
        }
        return lines;
    }

    // 10 -------------------------------
    @Test
    void testFindLongestWord() throws IOException {
        // todo Найти самое длинное слово
        assertEquals("conversations", findLongestWord());
    }

    private String findLongestWord() throws IOException {
        List<String> words = readAllWordsFromFileToList();
        return words.stream().max(Comparator.comparingInt(String::length)).get();
    }

    // 11 -------------------------------
    @Test
    void testAllWordsByAlphabetWithoutRepeat() throws IOException {
        // todo Получить список всех слов по алфавиту без повторов
        List<String> result = readAllWordsFromFileToList();
        result = result.stream().map(String::toLowerCase).distinct().sorted(String.CASE_INSENSITIVE_ORDER).toList();
        result.forEach(System.out::println);
        assertEquals("alice", result.get(5));
        assertEquals("all", result.get(6));
        assertEquals("without", result.get(134));
        assertEquals(138, result.size());
    }

    // 12 -------------------------------
    @Test
    void testFindMostFrequentWord() throws IOException {
        // todo Найти самое часто вcтречающееся слово
        assertEquals("the", mostFrequentWord());
    }

    // 13 -------------------------------
    @Test
    void testFindWordsByLengthInAlphabetOrder() throws IOException {
        // todo получить список слов, длиной не более 5 символов, переведенных в нижний регистр, в порядке алфавита, без повторов
        List<String> strings = readAllWordsFromFileToList();
        strings = strings.stream()
                .map(String::toLowerCase)
                .distinct()
                .filter(x -> x.length() <= 5)
                .sorted(String.CASE_INSENSITIVE_ORDER)
                .collect(Collectors.toList());
        assertEquals(94, strings.size());
        assertEquals("a", strings.get(0));
        assertEquals("alice", strings.get(2));
        assertEquals("would", strings.get(strings.size() - 1));
    }

    private String mostFrequentWord() throws IOException {
        List<String> result = readAllWordsFromFileToList();
        Map<String,Integer> map = new HashMap<>();
        for (String str: result) {
            map.merge(str,1,Integer::sum);
        }
        return map.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
    }

    List<HeavyBox> list;

    @BeforeEach
    void setUp() {
        list = new ArrayList<>(List.of(
                new HeavyBox(1,2,3,4),
                new HeavyBox(3,3,3,4),
                new HeavyBox(2,6,5,3),
                new HeavyBox(2,3,4,7),
                new HeavyBox(1,3,3,4),
                new HeavyBox(1,2,3,4),
                new HeavyBox(1,1,1,1)
        ));
    }

    static final String REGEXP = "\\W+"; // for splitting into words

    private BufferedReader reader;

    @BeforeEach
    public void setUpBufferedReader() throws IOException {
        reader = Files.newBufferedReader(
                Paths.get("Text.txt"), StandardCharsets.UTF_8);
    }

    @AfterEach
    public void closeBufferedReader() throws IOException {
        reader.close();
    }
}
