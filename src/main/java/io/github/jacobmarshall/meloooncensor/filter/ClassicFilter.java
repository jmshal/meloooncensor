package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;

import java.util.regex.Pattern;

public class ClassicFilter extends Filter {

    private static final Pattern WORD_SPLIT = Pattern.compile("\\s");

    public ClassicFilter (Configuration config) {
        super(config);
    }

    @Override
    public boolean violatesPolicy (String message) {
        String[] words = WORD_SPLIT.split(message);

        for (int index = 0; index < words.length; index++) {
            String word = words[index];

            if (isCensoredWord(word) && ! isIgnoredWord(word)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String censorMessage (String message) {
        String[] words = WORD_SPLIT.split(message);
        StringBuilder censoredMessage = new StringBuilder();

        for (int index = 0; index < words.length; index++) {
            String word = words[index];

            if (index > 0) {
                // Add a space back in (as long as it's not the first word)
                censoredMessage.append(" ");
            }

            if (isCensoredWord(word)) {
                if (isIgnoredWord(word)) {
                    // If the word is/contains an ignored word, allow the word
                    censoredMessage.append(word);
                } else {
                    // Censor the word if it contains a bad word & isn't an ignored one
                    censoredMessage.append(getCensoredWord(word, word, true));
                }
            } else {
                // Doesn't match a censored word, continue as normal
                censoredMessage.append(word);
            }
        }

        return censoredMessage.toString();
    }

}
