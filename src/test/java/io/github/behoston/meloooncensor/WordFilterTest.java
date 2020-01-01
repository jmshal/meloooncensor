package io.github.behoston.meloooncensor;

import io.github.jacobmarshall.meloooncensor.config.Configuration;
import io.github.jacobmarshall.meloooncensor.filter.WordFilter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;


public class WordFilterTest {

    @Test
    public void testMessageViolatesPolicy() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("wrucić"));
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertTrue(filter.violatesPolicy("Jak wrucić do domu?"));
    }

    @Test
    public void testMessageDoesNotViolatesPolicy() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("wrucić"));
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertFalse(filter.violatesPolicy("Jak wrócić do domu?"));
    }

    @Test
    public void testMessageCensored() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("wrucić"));
        Mockito.when(configuration.getCharString()).thenReturn("*");
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertEquals("Jak ****** do domu?", filter.censorMessage("Jak wrucić do domu?"));
    }

    @Test
    public void testMessageNotCensored() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("wrucić"));
        Mockito.when(configuration.getCharString()).thenReturn("*");
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertEquals("Jak wrócić do domu?", filter.censorMessage("Jak wrócić do domu?"));
    }

    @Test
    public void testWordFilterShouldBeExact() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Collections.singletonList("hyba"));
        Mockito.when(configuration.getCharString()).thenReturn("*");
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertFalse(filter.violatesPolicy("Chyba nie?"));
        Assertions.assertEquals("Chyba nie?", filter.censorMessage("Chyba nie?"));
    }

    @Test
    public void testManyCensoredWords() {
        Configuration configuration = Mockito.mock(Configuration.class);
        Mockito.when(configuration.getCensor()).thenReturn(Arrays.asList("hyba", "wrucić"));
        Mockito.when(configuration.getCharString()).thenReturn("*");
        WordFilter filter = new WordFilter(configuration);

        Assertions.assertEquals(
                "Chyba wrócić a nie **** ******",
                filter.censorMessage("Chyba wrócić a nie hyba wrucić")
        );
    }
}
