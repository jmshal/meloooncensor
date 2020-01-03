package io.github.behoston.meloooncensor.lang;

import io.github.behoston.meloooncensor.config.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Translation {

    public static final Translation DEFAULT_TRANSLATION = new Translation(Configuration.DEFAULT_LANGUAGE, false);

    private boolean allowFallback;
    protected String language;
    protected Properties translations;

    public Translation (String language) {
        this(language, true);
    }

    private Translation (String language, boolean allowFallback) {
        this.language = language;
        this.translations = getTranslations(language);
        this.allowFallback = allowFallback;
    }

    public String getText (String key) {
        String text = this.translations.getProperty(key, this.allowFallback ?
            DEFAULT_TRANSLATION.getText(key) : "i18n:" + key);

        try {
            text = new String(text.getBytes("ISO-8859-1"), "UTF-8");
        } catch(Exception e) {
            // Ignore, keep old encoding
        }

        return text;
    }

    private static Properties getTranslations (String language) {
        Properties properties = new Properties();
        InputStream inputStream = Translation.class.getResourceAsStream("/lang/" + language + ".properties");

        try {
            properties.load(inputStream);
        } catch (IOException err) {
            // Ignore (load the defaults)
        }

        return properties;
    }

}
