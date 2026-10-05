package com.example.hang.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class WordBank {

    private static final Map<String, String[]> WORD_CATEGORIES = new HashMap<>();
    private static final Random RANDOM = new Random();

    static {
        WORD_CATEGORIES.put("ANIMALS", new String[]{
                "PELICAN", "PANTHER", "DOLPHIN", "LEOPARD", "GIRAFFE",
                "CHEETAH", "FALCON", "HAMSTER", "BUFFALO", "PENGUIN"
        });
        WORD_CATEGORIES.put("OBJECTS", new String[]{
                "LANTERN", "MIRROR", "COMPASS", "PENDULUM", "SCISSORS",
                "WHISTLE", "TRUMPET", "PACKAGE", "PADLOCK", "CRYSTAL"
        });
        WORD_CATEGORIES.put("CELEBRITIES", new String[]{
                "EINSTEIN", "PICASSO", "BEETHOVEN", "CHAPLIN", "MOZART",
                "DARWIN", "NEWTON", "TESLA", "DISNEY", "SHAKESPEARE"
        });
    }

    public static String getRandomWord(String category) {
        String key = getCategoryKey(category);
        String[] bank = WORD_CATEGORIES.get(key);
        if (bank != null && bank.length > 0) {
            return bank[RANDOM.nextInt(bank.length)];
        }
        return "PELICAN";
    }

    private static String getCategoryKey(String cat) {
        if (cat != null) {
            String upper = cat.toUpperCase();
            if (upper.contains("OBJECT")) {
                return "OBJECTS";
            }
            if (upper.contains("CELEBRITY") || upper.contains("CELEBRITIES")) {
                return "CELEBRITIES";
            }
        }
        return "ANIMALS";
    }
}