package com.sewerynkamil.librarymanager.utils;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Author Kamil Seweryn
 */

public class StringLongConverter implements Converter<String, Long> {
    @Override
    public Result<Long> convertToModel(String value, ValueContext context) {
        try {
            return Result.ok(Long.valueOf(value));
        } catch (NumberFormatException e) {
            return Result.error("Please enter a number");
        }
    }

    @Override
    public String convertToPresentation(Long value, ValueContext context) {
        if(value == null) {
            return "";
        }
        return String.valueOf(value);
    }
}