package com.br.authorizationserver.core.liquibase;

import java.util.stream.Stream;

import com.br.authorizationserver.core.Exceptions.DriverNotCompatibleException;

public enum EDataBaseType {
    POSTGRES, ORACLE, H2;

    public static EDataBaseType of(final String driverBd) throws DriverNotCompatibleException  {
        return Stream.of(EDataBaseType.values())
                .filter(typeDb -> driverBd.toUpperCase().contains(typeDb.name()))
                .findFirst().orElseThrow(() -> new DriverNotCompatibleException(
                        String.format("%s not compatible or not identify, expected: %s, %s or %s", driverBd,
                                POSTGRES.name(), ORACLE.name(), H2.name())));

    }

    public boolean isH2() {
        return this.equals(H2);
    }

    public boolean isPostgres() {
        return this.equals(POSTGRES);
    }

    public boolean isOracle() {
        return this.equals(ORACLE);
    }
}
