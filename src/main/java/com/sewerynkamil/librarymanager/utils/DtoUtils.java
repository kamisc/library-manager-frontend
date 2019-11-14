package com.sewerynkamil.librarymanager.utils;

/**
 * Author Kamil Seweryn
 */

public class DtoUtils {
    public static String generateId(Long id) {
        return ("000000000" + id).substring(id.toString().length());
    }
}