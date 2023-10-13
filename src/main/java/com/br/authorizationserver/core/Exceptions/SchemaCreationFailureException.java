package com.br.authorizationserver.core.Exceptions;

import java.sql.SQLException;

public class SchemaCreationFailureException extends SQLException {

    public SchemaCreationFailureException(String reason, Throwable cause) {
        super(reason, cause);
    }
    
}
