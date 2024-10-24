package prashant.thakur.weathermicroservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class UnAuthorizedException extends Exception{
    public UnAuthorizedException(String message){super(message);}
}
