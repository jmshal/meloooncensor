package io.github.behoston.meloooncensor.filter;

import io.github.behoston.meloooncensor.config.Configuration;

public class StrictFilter extends ClassicFilter {

    public StrictFilter (Configuration config) {
        super(config);
    }

    @Override
    public String censorMessage (String message) {
        return null;
    }

}
