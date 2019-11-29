package com.sewerynkamil.librarymanager.ui.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Author Kamil Seweryn
 */

public class StringIntegerConverter implements Converter<String, Integer> {
    @Override
    public Result<Integer> convertToModel(String value, ValueContext context) {
        try {
            return Result.ok(Integer.valueOf(value));
        } catch (NumberFormatException e) {
            return Result.error("Please enter a number");
        }
    }

    @Override
    public String convertToPresentation(Integer value, ValueContext context) {
        if(value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}