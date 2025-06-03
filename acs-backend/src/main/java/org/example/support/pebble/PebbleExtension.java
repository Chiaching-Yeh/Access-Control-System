package org.example.support.pebble;

import java.util.List;
import java.util.Map;

import io.pebbletemplates.pebble.extension.AbstractExtension;
import io.pebbletemplates.pebble.extension.Filter;
import io.pebbletemplates.pebble.extension.Function;
import io.pebbletemplates.pebble.extension.Test;
import io.pebbletemplates.pebble.tokenParser.TokenParser;
import io.pebbletemplates.spring.extension.function.HrefFunction;
import io.pebbletemplates.spring.extension.function.MessageSourceFunction;
import io.pebbletemplates.spring.extension.function.bindingresult.*;
import org.example.support.Lists;
import org.example.support.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PebbleExtension extends AbstractExtension {
	
	@Autowired
	private MessageSource messageSource;

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> map = super.getFilters();
        if (map == null) {
            map = Maps.newHashMap();
        }
        map.put(Nl2brFilter.FILTER_NAME, new Nl2brFilter());
        map.put(Base64DecodeFilter.FILTER_NAME, new Base64DecodeFilter());
        return map;
    }

    @Override
    public Map<String, Test> getTests() {
        Map<String, Test> map = super.getTests();
        if (map == null) {
            map = Maps.newHashMap();
        }
        map.put(InstanceOfTest.TEST_NAME, new InstanceOfTest());
        return map;
    }

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> map = super.getFunctions();
        if (map == null) {
            map = Maps.newHashMap();
        }
        map.put(FileNameImageFunction.FUNCTION_NAME, new FileNameImageFunction());
        map.put(MessageSourceFunction.FUNCTION_NAME, new MessageSourceFunction(this.messageSource));
        map.put(HasErrorsFunction.FUNCTION_NAME, new HasErrorsFunction());
        map.put(HasGlobalErrorsFunction.FUNCTION_NAME, new HasGlobalErrorsFunction());
        map.put(HasFieldErrorsFunction.FUNCTION_NAME, new HasFieldErrorsFunction());
        map.put(GetAllErrorsFunction.FUNCTION_NAME, new GetAllErrorsFunction(this.messageSource));
        map.put(GetGlobalErrorsFunction.FUNCTION_NAME, new GetGlobalErrorsFunction(this.messageSource));
        map.put(GetFieldErrorsFunction.FUNCTION_NAME, new GetFieldErrorsFunction(this.messageSource));
        map.put(HrefFunction.FUNCTION_NAME, new HrefFunction());
        return map;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        List<TokenParser> parsers = super.getTokenParsers();
        if (parsers == null) {
            parsers = Lists.newArrayList();
        }
        parsers.add(new SliceTokenParser());
        return parsers;
    }
}
