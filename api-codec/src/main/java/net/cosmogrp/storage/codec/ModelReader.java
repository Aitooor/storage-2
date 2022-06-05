package net.cosmogrp.storage.codec;

import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;

public interface ModelReader<R> {

    /**
     * Reads a String from the current row
     * and converts it to a UUID
     *
     * @param field The name of the field to read.
     * @return Nothing.
     */
    default UUID readUuid(String field) {
        return UUID.fromString(readString(field));
    }

    /**
     * It reads a list of objects from the
     * document and returns them as a set
     *
     * @param field The field name in the document.
     * @param clazz The class of the type of the
     *              elements of the set.
     * @return A Set of the given class.
     */
    default <T> Set<T> readSet(String field, Class<T> clazz) {
        return new HashSet<>(readList(field, clazz));
    }

    /**
     * Reads a date from the document
     *
     * @param field The field name to read.
     * @return Nothing.
     */
    default Date readDate(String field) {
        return new Date(readLong(field));
    }

    default <T extends Enum<T>> T readEnum(String field, Class<T> clazz) {
        return Enum.valueOf(clazz, readString(field));
    }

    /**
     * Reads a string from the document
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    String readString(String field);

    /**
     * Reads a double from the document
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    double readDouble(String field);

    /**
     * Reads a field from the document and
     * returns it as a float
     *
     * @param field The field to read.
     * @return The value of the field.
     */
    default float readFloat(String field) {
        return (float) readDouble(field);
    }

    /**
     * Reads a long from the document
     *
     * @param field The field to read from.
     * @return The long value of the field.
     */
    long readLong(String field);

    /**
     * Reads the value of the field from the
     * document and returns it as an integer
     *
     * @param field The field to read.
     * @return The integer value of the field.
     */
    int readInt(String field);

    /**
     * Reads a boolean value from the document
     *
     * @param field The field to read.
     * @return The boolean value of the field.
     */
    boolean readBoolean(String field);

    /**
     * Read a list of objects from a field in the document
     *
     * @param field The field name to read from the document.
     * @param clazz The class of the type you want to read.
     * @return A List of the specified type.
     */
    <T> List<T> readList(String field, Class<T> clazz);

    /**
     * If the document has a field with the given name,
     * parse it using the given parser
     *
     * @param field  The name of the field to read.
     * @param parser A function that takes a DocumentReader
     *               and returns a DocumentCodec.
     * @return The result of applying the parser to the child document.
     */
    <T extends ModelCodec<R>> @Nullable T readChild(
            String field,
            Function<ModelReader<R>, T> parser
    );

    /**
     * It reads a list of documents from the given field, and
     * parses each document into a key-value pair using the given
     * key and value parsers
     *
     * @param field       The name of the field to read.
     * @param keyParser   A function that takes a DocumentCodec
     *                    and returns a key.
     * @param valueParser A function that takes a DocumentReader
     *                    and returns a value of type V.
     * @return A map of key-value pairs.
     */
    <K, V extends ModelCodec<R>> Map<K, V> readMap(
            String field,
            Function<V, K> keyParser,
            Function<ModelReader<R>, V> valueParser
    );

    /**
     * Reads a list of documents from a field in the current document
     *
     * @param field  The name of the field in the document
     *               that contains the list of children.
     * @param parser A function that takes a DocumentReader
     *               and returns a DocumentCodec.
     * @return A set of DocumentCodec objects.
     */
    <T extends ModelCodec<R>> Set<T> readChildren(
            String field,
            Function<ModelReader<R>, T> parser
    );

}
