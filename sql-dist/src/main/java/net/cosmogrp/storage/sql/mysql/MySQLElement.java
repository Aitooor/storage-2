package net.cosmogrp.storage.sql.mysql;

import net.cosmogrp.storage.sql.identity.DataType;
import net.cosmogrp.storage.sql.identity.SQLConstraint;
import net.cosmogrp.storage.sql.identity.SQLElement;

import java.util.Arrays;
import java.util.List;

public class MySQLElement implements SQLElement {

    private final String column;
    private final DataType type;

    private final List<SQLConstraint> constraints;

    private String declaration;
    private ForeignKey foreignKey;

    public MySQLElement(
            String column, DataType type,
            ForeignKey foreignKey,
            SQLConstraint... constraints
    ) {
        this.column = column;
        this.type = type;
        this.constraints = Arrays.asList(constraints);
        this.foreignKey = foreignKey;
    }

    public MySQLElement(String column, DataType type, SQLConstraint... constraints) {
        this.column = column;
        this.type = type;
        this.constraints = Arrays.asList(constraints);
    }

    @Override
    public boolean isPrimary() {
        return constraints.contains(SQLConstraint.PRIMARY);
    }

    @Override
    public boolean isNullable() {
        return !constraints.contains(SQLConstraint.NOT_NULL);
    }

    @Override
    public boolean isUnique() {
        return constraints.contains(SQLConstraint.UNIQUE);
    }

    @Override
    public String toParameter() {
        return ":" + column;
    }

    @Override
    public String toDeclaration() {
        if (declaration != null) {
            return declaration;
        }

        StringBuilder builder = new StringBuilder();

        builder
                .append(column)
                .append(" ")
                .append(type.getSql())
                .append(" ");

        constraints.forEach(constraint -> builder
                .append(constraint.toSql())
                .append(" "));

        if (hasReference()) {
            builder.append(foreignKey.toSql());
        }

        declaration = builder.toString();

        return declaration;
    }

    @Override
    public String getColumn() {
        return column;
    }

    @Override
    public boolean hasReference() {
        return foreignKey != null;
    }

    @Override
    public DataType getType() {
        return type;
    }

    @Override
    public List<SQLConstraint> getConstraints() {
        return constraints;
    }

    public static class ForeignKey {

        private static final String TEMPLATE = "FOREIGN KEY REFERENCES %s(%s) %s %s";

        private final String tableReference;
        private final String elementReference;

        private final ForeignTrigger trigger;
        private final ForeignAction action;

        public ForeignKey(
                String tableReference,
                String elementReference,
                ForeignTrigger trigger,
                ForeignAction action
        ) {
            this.tableReference = tableReference;
            this.elementReference = elementReference;
            this.trigger = trigger;
            this.action = action;
        }

        public String toSql() {
            return String.format(
                    TEMPLATE, tableReference, elementReference,
                    trigger.sql, action.sql
            );
        }
    }

    public enum ForeignTrigger {

        DELETE("ON DELETE"),
        UPDATE("ON UPDATE");

        private final String sql;

        ForeignTrigger(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }

    public enum ForeignAction {

        RESTRICT("RESTRICT"),
        CASCADE("CASCADE"),
        NULL("SET NULL"),
        NOTHING("NO ACTION"),
        DEFAULT("SET DEFAULT")

        ;

        private final String sql;

        ForeignAction(String sql) {
            this.sql = sql;
        }

        public String getSql() {
            return sql;
        }
    }

}
