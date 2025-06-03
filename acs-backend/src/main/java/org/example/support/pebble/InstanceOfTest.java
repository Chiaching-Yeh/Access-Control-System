package org.example.support.pebble;

import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.error.PebbleException;
import io.pebbletemplates.pebble.extension.Test;
import io.pebbletemplates.pebble.template.EvaluationContext;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.example.support.Lists;

/**
 *  檢測 物件是否為某個 class 的 instance
 */
public class InstanceOfTest implements Test {

    public static final String TEST_NAME = "instanceOf";

    @Override
    public boolean apply(Object input,
                         Map<String, Object> args,
                         PebbleTemplate self,
                         EvaluationContext context,
                         int lineNumber) throws PebbleException {

        String className = String.class.cast(args.get("className"));

        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new PebbleException(e, "Can not find class '" + className + "'", lineNumber, self.getName());
        }

        if (clazz.isInstance(input)) {
            return true;
        } else {
            return false;
        }

    }

    @Override
    public List<String> getArgumentNames() {
        return Lists.newArrayList("className");
    }

}
