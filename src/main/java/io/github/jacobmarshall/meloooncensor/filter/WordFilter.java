package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;

import java.util.regex.Pattern;


public class WordFilter extends ClassicFilter {
    private static final Pattern WORD_SPLIT = Pattern.compile("\\s");

    public WordFilter(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected boolean isCensoredWord(String word) {
        return config.getCensor().contains(word);
    }

    @Override
    protected boolean isIgnoredWord(String word) {
        return config.getIgnore().contains(word);
    }

    @Override
    public boolean violatesPolicy(String message) {
        String[] words = WORD_SPLIT.split(message);
        for (String word : words) {
            if (config.getCensor().contains(word.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}
