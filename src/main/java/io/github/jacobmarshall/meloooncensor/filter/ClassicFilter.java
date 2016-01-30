package io.github.jacobmarshall.meloooncensor.filter;

import java.util.List;
import java.util.regex.Pattern;

public class ClassicFilter extends Filter {

    public ClassicFilter(char replace, List<String> censor, List<String> ignore) {
        super(replace, censor, ignore);
    }

    @Override
    public boolean violatesPolicy (String message) {
        return ! message.equals(this.censorMessage(message));
    }

    @Override
    public String censorMessage (String message) {
        String[] words = Pattern.compile("\\s").split(message);
        StringBuilder censoredMessage = new StringBuilder();

        for (int index = 0; index < words.length; index++) {
            String word = words[index];

            if (index > 0) {
                // Add a space back in (as long as it's not the first word)
                censoredMessage.append(" ");
            }

            if (wordIsOrContains(word, censor)) {
                if (wordIsOrContains(word, ignore)) {
                    // If the word is/contains an ignored word, allow the word
                    censoredMessage.append(word);
                } else {
                    // Censor the word if it contains a bad word & isn't an ignored one
                    censoredMessage.append(censorWord(word, word, true));
                }
            } else {
                // Doesn't match a censored word, continue as normal
                censoredMessage.append(word);
            }
        }

        return censoredMessage.toString();
    }

}
