package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;


public class WordFilter extends ClassicFilter {
    public WordFilter(Configuration configuration) {
        super(configuration);
    }

    @Override
    protected boolean isCensoredWord(String word) {
        return config.getCensor().contains(word.toLowerCase());
    }

    @Override
    protected boolean isIgnoredWord(String word) {
        return config.getIgnore().contains(word.toLowerCase());
    }
}
