package com.g44.kodeholik.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.g44.kodeholik.model.dto.exception.ErrorResponse;

@RestControllerAdvice

public class GlobalException {
    // handle not found
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getDetails()),
                HttpStatus.NOT_FOUND);
    }

    // handle request param bi sai dinh dang
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // handle jwt k dung dinh dang
    @ExceptionHandler(MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    // handle loi gui email
    @ExceptionHandler(EmailSendingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleEmailSendingException(EmailSendingException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // handle cac loi bad request
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    // handle cac loi unauthorized
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.UNAUTHORIZED);
    }

    // handle cac loi forbidden
    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleForbiddenException(ForbiddenException ex) {
        return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                HttpStatus.FORBIDDEN);
    }
}
