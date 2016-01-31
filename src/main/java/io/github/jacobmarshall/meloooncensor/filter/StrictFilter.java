package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.config.Configuration;

public class StrictFilter extends ClassicFilter {

    public StrictFilter (Configuration config) {
        super(config);
    }

    @Override
    public String censorMessage (String message) {
        return null;
    }

}
