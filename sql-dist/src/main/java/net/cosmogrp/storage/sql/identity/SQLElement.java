package net.cosmogrp.storage.sql.identity;

import java.util.List;

public interface SQLElement {

    boolean isPrimary();

    boolean isNullable();

    boolean isUnique();

    String toParameter();

    String toDeclaration();

    String getColumn();

    boolean hasReference();

    DataType getType();

    List<SQLConstraint> getConstraints();

}
