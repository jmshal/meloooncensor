package io.github.jacobmarshall.meloooncensor.filter;

import java.util.List;

public class StrictFilter extends ClassicFilter {

    public StrictFilter (char replace, List<String> censor, List<String> ignore) {
        super(replace, censor, ignore);
    }

    @Override
    public String censorMessage (String message) {
        return null;
    }

}
