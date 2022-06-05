package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ModelService<T extends Model> {

    String ID_FIELD = "id";

    /**
     * Find a sync by id
     *
     * @param id The id of the object to find.
     * @return The method returns a nullable object of type T.
     */
    @Nullable T findSync(String id);

    List<T> findSync(String field, String value);

    /**
     * Return a list of all the elements in the collection
     *
     * @return Nothing
     */
    List<T> findAllSync();

    /**
     * Save the model to the database and cache
     * if it's not already there.
     *
     * @param model The model that will be saved.
     */
    void saveSync(T model);

    /**
     * DeleteSync deletes the model from the database
     *
     * @param model The model to be deleted.
     */
    void deleteSync(T model);

    /**
     * DeleteSync deletes the object with the given id
     *
     * @param id The id of the sync to delete.
     * @return The return type is void.
     */
    T deleteSync(String id);

}
