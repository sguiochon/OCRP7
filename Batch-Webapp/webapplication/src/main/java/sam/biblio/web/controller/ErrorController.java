package sam.biblio.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.ModelAndView;
import sam.biblio.web.error.ExceptionDescription;
import sam.biblio.web.error.LendingCreationException;
import sam.biblio.web.error.LendingExtensionException;

import java.net.URISyntaxException;

@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(URISyntaxException.class)
    public ModelAndView handleMyException(URISyntaxException mex) {
        ModelAndView model = new ModelAndView();
        model.addObject("errMessage", mex.getMessage());
        model.addObject("errReason", mex.getReason());
        model.setViewName("error/api_error");
        return model;
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public String handleException(HttpClientErrorException ex) {
        return "error/api_error";
    }


    @ExceptionHandler(LendingCreationException.class)
    protected ResponseEntity<Object> handleLendingPeriodExtensionError(LendingCreationException exception) {
        ExceptionDescription desc = new ExceptionDescription(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), "");
        return new ResponseEntity<Object>(desc, desc.getStatus());
    }

    @ExceptionHandler(LendingExtensionException.class)
    protected ResponseEntity<Object> handleLendingPeriodExtensionError(LendingExtensionException exception) {
        ExceptionDescription desc = new ExceptionDescription(HttpStatus.NOT_ACCEPTABLE, exception.getMessage(), "");
        return new ResponseEntity<Object>(desc, desc.getStatus());
    }
}
