package com.g44.kodeholik.exception;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.g44.kodeholik.config.MessageProperties;
import com.g44.kodeholik.model.dto.exception.ErrorResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalException {

        private final MessageProperties messageProperties;

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
        public ResponseEntity<ErrorResponse> handleMalformedJwtThrowException(MalformedJwtException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(io.jsonwebtoken.MalformedJwtException.class)
        @ResponseStatus(HttpStatus.UNAUTHORIZED)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleMalformedJwtException(MalformedJwtException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.UNAUTHORIZED);
        }

        // handle thieu request param
        @ExceptionHandler(MissingServletRequestParameterException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handlerMissingRequestParamException(
                        MissingServletRequestParameterException ex) {
                return new ResponseEntity(new ErrorResponse(messageProperties.getMessage("MSG02"), ex.getMessage()),
                                HttpStatus.BAD_REQUEST);
        }

        // handle thieu request part
        @ExceptionHandler(MissingServletRequestPartException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handlerMissingRequestPartException(MissingServletRequestPartException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.BAD_REQUEST);
        }

        // handle thieu request part
        @ExceptionHandler(MultipartException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handlerMultipartException(MultipartException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(HttpMessageNotReadableException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handlerMessageNotReadableException(HttpMessageNotReadableException ex) {
                log.info(ex.toString());
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.BAD_REQUEST);
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

        // handle cac loi forbidden
        @ExceptionHandler(S3Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleS3Exception(S3Exception ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(IOException.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleIOException(IOException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(Exception.class)
        @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleServerException(Exception ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.INTERNAL_SERVER_ERROR);
        }

        @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
        @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleMethodNotAllowedException(
                        HttpRequestMethodNotSupportedException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.METHOD_NOT_ALLOWED);
        }

        @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public Map<String, Object> handleMethodArgumentNotValidException(
                        org.springframework.web.bind.MethodArgumentNotValidException ex) {
                List<Map<String, String>> errorList = ex.getBindingResult().getFieldErrors().stream()
                                .map(error -> Map.of("field", error.getField(), "error",
                                                messageProperties.getMessage(error.getDefaultMessage())))
                                .collect(Collectors.toList());

                return Map.of("message", errorList);
        }

        @ExceptionHandler(TestCaseNotPassedException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<Map<String, Object>> handleTestCaseNotPassedException(
                        TestCaseNotPassedException ex) {
                Map<String, Object> errorResponse = new HashMap<>();
                errorResponse.put("message", ex.getMessage());
                errorResponse.put("details", ex.getDetails());

                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(AccessDeniedException.class)
        @ResponseStatus(HttpStatus.FORBIDDEN)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
        @ResponseStatus(HttpStatus.BAD_REQUEST)
        @ResponseBody
        public ResponseEntity<ErrorResponse> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
                return new ResponseEntity(new ErrorResponse(ex.getMessage(), ex.getMessage()),
                                HttpStatus.BAD_REQUEST);
        }
}
