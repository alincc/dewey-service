package no.nb.microservice.dewey.rest.controller;

import no.nb.microservice.dewey.Application;
import no.nb.microservice.dewey.core.service.DeweyServiceImpl;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static reactor.core.composable.spec.Promises.when;

@RunWith(MockitoJUnitRunner.class)
public class DeweyControllerTest {

    @Mock
    private IDeweyService iDeweyService;
    private DeweyController deweyController;
    private String deweyListPath = "/dewey-list-TEST.xml";


    @Before
    public void setup() {
        iDeweyService = new DeweyServiceImpl(messageSource());
        deweyController = new DeweyController(iDeweyService);
        ReflectionTestUtils.setField(iDeweyService, "deweyListPath", deweyListPath);

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void shouldReturnDeweyWrapperWithNonEmptyDeweyList() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertTrue("List of dewey should contain more than one item", !deweyWrapper.getDeweyList().isEmpty());
    }

    @Test
    public void shouldReturnDeweyWrapperWithNonEmptyDeweyPathList() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertTrue("List of dewey should contain more than one item", !deweyWrapper.getDeweyPathList().isEmpty());
    }

    @Test
    public void shouldReturnOnlyDeweyPathList() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("456123", "no");
        DeweyWrapper deweyWrapper = entity.getBody();
        assertTrue("DeweyList should be empty", deweyWrapper.getDeweyList().isEmpty());
        assertTrue("DeweyPathList should not be empty", !deweyWrapper.getDeweyPathList().isEmpty());
    }

    @Test
    public void shouldReturnNotFound() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("abc", "no");
        DeweyWrapper deweyWrapper = entity.getBody();
        assertTrue("Status code should be 400", entity.getStatusCode().is4xxClientError());
        assertTrue("Should be null", deweyWrapper == null);
    }

    @Test
    public void shouldReturnDeweyWithNorwegianLanguageWhenNoLanguageParameterIsSent() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldReturnDeweyWithNorwegianLanguageWhenBogusLanguageParameterIsSent() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "bogus");
        DeweyWrapper deweyWrapper = entity.getBody();
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/dewey");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}