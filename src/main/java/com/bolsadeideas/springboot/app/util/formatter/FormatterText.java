package com.bolsadeideas.springboot.app.util.formatter;

import java.text.Normalizer;

public class FormatterText {

    // Función para eliminar acentos y caracteres especiales
    public static String removeAccents(String input) {
        return Normalizer.normalize(input, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .replaceAll("[^\\p{ASCII}]+", "");
    }

}
