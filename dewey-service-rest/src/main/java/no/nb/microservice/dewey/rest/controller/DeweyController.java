package no.nb.microservice.dewey.rest.controller;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import no.nb.microservice.dewey.rest.resourceassembler.DeweyPathResourceAssembler;
import no.nb.microservice.dewey.rest.resourceassembler.DeweyResourceAssembler;
import no.nb.microservice.dewey.rest.resourceassembler.DeweyWrapperResourceAssembler;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<DeweyWrapper> dewey(
            @RequestParam(value = "class", required = false) String classValue,
            @RequestParam(value = "language", required = false, defaultValue = "no") String language) {
        if (classValue == null || StringUtils.isNumeric(classValue)) {
            DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper(classValue, language);
            if (deweyWrapper != null && (!deweyWrapper.getDeweyList().isEmpty() || !deweyWrapper.getDeweyPathList().isEmpty())) {
                return new ResponseEntity<>(addResources(deweyWrapper), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    private DeweyWrapper addResources(DeweyWrapper deweyWrapper) {
        new DeweyWrapperResourceAssembler().toResource(deweyWrapper);

        if (!deweyWrapper.getDeweyList().isEmpty()) {
            for (Dewey dewey : deweyWrapper.getDeweyList()) {
                new DeweyResourceAssembler().toResource(dewey);
            }
        }
        if (!deweyWrapper.getDeweyPathList().isEmpty()) {
            for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
                new DeweyPathResourceAssembler().toResource(dewey);
            }

        }
        return deweyWrapper;
    }
}