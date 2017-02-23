package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;

import java.util.regex.Pattern;


public class WordFilter extends ClassicFilter {
    private static final Pattern WORD_SPLIT = Pattern.compile("\\s");

    public WordFilter(Configuration configuration) {
        super(configuration);
    }

    @Override
    public boolean violatesPolicy(String message) {
        String[] words = WORD_SPLIT.split(message);
        for (String word : words) {
            if (this.config.getCensor().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
