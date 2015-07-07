package no.nb.microservice.dewey.rest.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

@RestController
@Api(value = "/", description = "Dewey API")
public class DeweyController {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeweyController.class);

    @Autowired
    private IDeweyService iDeweyService;

    @ApiOperation(value = "Get Dewey", response = String.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response"),
            @ApiResponse(code = 400, message = "Not Found")
    })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<DeweyWrapper> dewey(@RequestParam(value = "class", required = false) String classValue, @RequestParam(value = "language", required = false) String language) {
        String lang = language;
        if (classValue != null && !StringUtils.isNumeric(classValue)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (lang == null || "".equalsIgnoreCase(lang)) {
            lang = "no";
        }

        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper(classValue, lang);
        if (deweyWrapper != null && (!deweyWrapper.getDeweyList().isEmpty() || !deweyWrapper.getDeweyPathList().isEmpty())) {
            return new ResponseEntity<>(deweyWrapper, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "It looks like we have a internal error in our application. The error have been logged and will be looked at by our development team.")
    public void defaultHandler(HttpServletRequest req, Exception e) {

        // Build Header string
        StringBuilder headers = new StringBuilder();
        for (String headerKey : Collections.list(req.getHeaderNames())) {
            String headerValue = req.getHeader(headerKey);
            headers.append(headerKey + ": " + headerValue + ", ");
        }

        LOGGER.error("" +
                "Got an unexcepted exception.\n" +
                "Context Path: " + req.getContextPath() + "\n" +
                "Request URI: " + req.getRequestURI() + "\n" +
                "Query String: " + req.getQueryString() + "\n" +
                "Method: " + req.getMethod() + "\n" +
                "Headers: " + headers + "\n" +
                "Auth Type: " + req.getAuthType() + "\n" +
                "Remote User: " + req.getRemoteUser() + "\n" +
                "Username: " + ((req.getUserPrincipal()  != null) ? req.getUserPrincipal().getName() : "Anonymous") + "\n"
                , e);
    }
}
