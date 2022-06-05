package net.cosmogrp.storage;

import net.cosmogrp.storage.model.Model;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public interface CachedModelService<T extends Model>
        extends ModelService<T> {

    @Nullable T getSync(String id);

    List<T> getSync(String field, String value);

    @Nullable T getOrFindSync(String id);

    List<T> getOrFindSync(String field, String value);

    List<T> getAllSync();

    /**
     * Uploads the model to the server
     *
     * @param model The model to be uploaded.
     */
    void uploadSync(T model);

    /**
     * Upload all the files in the current directory
     * to the remote server
     *
     * @param preUploadAction a function that takes a single
     *                        parameter, which is the file to be uploaded.
     */
    void uploadAllSync(Consumer<T> preUploadAction);

}
