package main;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class WordNetTest {

    @Test
    public void testHyponymsSimple() {
        // 假设你的文件路径相对项目根目录
        WordNet wn = new WordNet("data/wordnet/synsets11.txt", "data/wordnet/hyponyms11.txt");

        // 预期结果： antihistamine的所有hyponyms，包括自身
        Set<String> expected = Set.of("antihistamine", "actifed");

        Set<String> actual = wn.hyponyms("antihistamine");

        assertEquals(expected, actual);
    }

    @Test
    public void testHyponymsDescent() {
        WordNet wn = new WordNet("data/wordnet/synsets11.txt", "data/wordnet/hyponyms11.txt");

        // descent对应的hyponyms应该包含自身和jump、parachuting（根据示例）
        Set<String> expected = Set.of("descent", "jump", "parachuting");

        Set<String> actual = wn.hyponyms("descent");

        assertEquals(expected, actual);
    }
}