package com.mygdx.utils;

import org.hibernate.boot.model.TypeContributions;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupport;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.SqlTypes;
import org.hibernate.type.descriptor.sql.internal.DdlTypeImpl;

public class CodeSQLiteDialect extends Dialect {

    public CodeSQLiteDialect() {
        super();
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl() {
            @Override
            public boolean supportsIdentityColumns() {
                return true;
            }

            @Override
            public String getIdentityColumnString(int type) {
                return "integer";
            }

            @Override
            public String getIdentityInsertString() {
                return "null";
            }

            @Override
            public boolean hasDataTypeInIdentityColumn() {
                return false;
            }
        };
    }

    @Override
    public String getAddForeignKeyConstraintString(
        String constraintName,
        String[] foreignKey,
        String referencedTable,
        String[] primaryKey,
        boolean referencesPrimaryKey) {
        return "";
    }

    @Override
    public void contributeTypes(TypeContributions typeContributions, ServiceRegistry serviceRegistry) {
        super.contributeTypes(typeContributions, serviceRegistry);

        var ddlTypeRegistry = typeContributions.getTypeConfiguration().getDdlTypeRegistry();
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.BOOLEAN, "boolean", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.INTEGER, "integer", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.BIGINT, "integer", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.VARCHAR, "varchar", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.FLOAT, "real", this));
        ddlTypeRegistry.addDescriptor(new DdlTypeImpl(SqlTypes.DOUBLE, "real", this));
    }
}
