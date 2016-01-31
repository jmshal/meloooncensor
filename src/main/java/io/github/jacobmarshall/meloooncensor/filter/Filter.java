package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;

import java.util.ArrayList;
import java.util.List;

public abstract class Filter {

    Configuration config;

    public Filter (Configuration config) {
        this.config = config;
    }

    private String getCensorString (int length) {
        return new String(new char[length]).replace("\0", config.getCharString());
    }

    protected boolean isCensoredWord (String word) {
        return wordIsOrContains(word, config.getCensor());
    }

    protected boolean isIgnoredWord (String word) {
        return wordIsOrContains(word, config.getIgnore());
    }

    protected String getCensoredWord (String word, String match, boolean whole) {
        if (whole) {
            // Censor the entire word
            return getCensorString(word.length());
        } else {
            // Replace any occurrences within the word with the censor string
            return word.replaceAll("(?i)" + match, getCensorString(match.length()));
        }
    }

    protected List<String> toLowerCaseStringList (List<String> strings) {
        List<String> lowerCaseStringList = new ArrayList<>();
        for (String string : strings) {
            lowerCaseStringList.add(string.toLowerCase());
        }
        return lowerCaseStringList;
    }

    protected boolean wordIsOrContains (String word, List<String> list) {
        word = word.toLowerCase();
        list = toLowerCaseStringList(list);

        if (list.contains(word)) {
            // If the word is in the list (quick and easy check)
            return true;
        } else {
            for (String test : list) {
                // If the word contains any of the strings in the list
                if (word.contains(test)) {
                    return true;
                }
            }
            return false;
        }
    }

    public abstract boolean violatesPolicy (String message);

    public abstract String censorMessage (String message);

}
