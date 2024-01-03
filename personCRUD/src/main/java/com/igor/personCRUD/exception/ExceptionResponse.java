package com.igor.personCRUD.exception;

import java.util.Date;

public record ExceptionResponse(
        Date timeStamp,
        String message,
        String statusCode
) {
}
