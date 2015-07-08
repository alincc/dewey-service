package no.nb.microservice.dewey.rest.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(value = "/", description = "Dewey API")
public class DeweyController {
    private final IDeweyService iDeweyService;

    @Autowired
    public DeweyController(IDeweyService iDeweyService) {
        super();
        this.iDeweyService = iDeweyService;
    }

    @ApiOperation(value = "Get Dewey", response = String.class, httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful response"),
            @ApiResponse(code = 400, message = "Not Found")
    })
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<DeweyWrapper> dewey(@RequestParam(value = "class", required = false) String classValue, @RequestParam(value = "language", required = false) String language) {
        if (classValue == null || StringUtils.isNumeric(classValue)) {
            DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper(classValue, language);
            if (deweyWrapper != null && (!deweyWrapper.getDeweyList().isEmpty() || !deweyWrapper.getDeweyPathList().isEmpty())) {
                return new ResponseEntity<>(deweyWrapper, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}