package io.github.jacobmarshall.meloooncensor.filter;

import java.util.List;

public abstract class Filter {

    char replace;
    List<String> censor;
    List<String> ignore;

    public Filter (char replace, List<String> censor, List<String> ignore) {
        this.replace = replace;
        this.censor = censor;
        this.ignore = ignore;
    }

    private String getCensorString (int length) {
        return new String(new char[length]).replace("\0", String.valueOf(replace));
    }

    protected String censorWord (String word, String match, boolean whole) {
        if (whole) {
            // Censor the entire word
            return getCensorString(word.length());
        } else {
            // Replace any occurrences within the word with the censor string
            return word.replaceAll(match, getCensorString(match.length()));
        }
    }

    protected boolean wordIsOrContains (String word, List<String> list) {
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
