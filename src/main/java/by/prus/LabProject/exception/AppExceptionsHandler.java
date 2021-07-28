package by.prus.LabProject.exception;

import by.prus.LabProject.model.response.ErrorMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class AppExceptionsHandler {

    @ExceptionHandler(value = {CertificateServiceException.class})
    public ResponseEntity<Object> handleUserServiceException(CertificateServiceException ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // этот метод будет обрабатывать все остальные Exception не относящиеся к UserServiceException (выше)
    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<Object> handleUserServiceException(Exception ex, WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage(new Date(), ex.getMessage());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
