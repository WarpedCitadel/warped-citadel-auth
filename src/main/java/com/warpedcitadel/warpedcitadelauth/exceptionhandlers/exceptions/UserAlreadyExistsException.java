package com.warpedcitadel.warpedcitadelauth.exceptionhandlers.exceptions;

import java.sql.SQLException;


// Todo | Exception goes through put may need to specify desired response
public class UserAlreadyExistsException extends SQLException {
    public UserAlreadyExistsException(String message, Throwable cause){
        super(message, cause);
    }
}
