package com.es.phoneshop.validator;

import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private String regexNameOrSurname = "^[\\p{L} .'-]+$";
    private String regexPhone = "^\\+\\d{12}$";
    private String regexDate = "^\\d{2}-\\d{2}-\\d{4}$";
    private String regexAddress = "^[a-zA-Z0-9,\\s]+$";

    public static Validator getInstance() {
        return Validator.SingletonHolder.instance;
    }

    private static class SingletonHolder {
        private static final Validator instance = new Validator();
    }

    private Validator() {

    }

    public Predicate<String> validateNameOrSurname = parameter -> validate(parameter, regexNameOrSurname);

    public Predicate<String> validatePhone = parameter -> validate(parameter, regexPhone);

    public Predicate<String> validateAddress = parameter -> validate(parameter, regexAddress);

    public Predicate<String> validateDate = parameter -> validate(parameter, regexDate);

    private boolean validate(String parameter, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(parameter);
        return matcher.find();
    }
}
