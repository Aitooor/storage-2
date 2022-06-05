package net.cosmogrp.storage.validate;

public class Validate {

    private Validate() {}

    public static void notNull(Object object,
                               String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void state(boolean condition,
                             String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

}
