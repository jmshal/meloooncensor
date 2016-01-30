package io.github.jacobmarshall.meloooncensor.filter;

import io.github.jacobmarshall.meloooncensor.MelooonCensor;
import io.github.jacobmarshall.meloooncensor.config.Configuration;

import java.util.List;

public class StrictFilter extends ClassicFilter {

    public StrictFilter (Configuration config) {
        super(config);
    }

    @Override
    public String censorMessage (String message) {
        return null;
    }

}
