package net.cosmogrp.storage;

public interface Validate {

    static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    static void state(boolean state, String message) {
        if (!state) {
            throw new IllegalArgumentException(message);
        }
    }

}
