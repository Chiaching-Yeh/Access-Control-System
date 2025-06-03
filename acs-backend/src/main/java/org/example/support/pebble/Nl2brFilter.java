package org.example.support.pebble;

import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.example.support.Lists;

public class Nl2brFilter implements Filter {

    public static final String FILTER_NAME = "nl2br";

    @Override
    public Object apply(Object input,
                        Map<String, Object> args,
                        PebbleTemplate self,
                        EvaluationContext context,
                        int lineNumber) throws PebbleException {

        if (input == null)
            return null;

        String str = String.class.cast(input);
        return str.trim().replaceAll("(\r\n|\n|\r)", "<br>");
    }

    @Override
    public List<String> getArgumentNames() {
        return Lists.newArrayList();
    }

}
