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
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.*;
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
    public void shouldReturnDeweyWrapper() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no");
        DeweyWrapper deweyWrapper = entity.getBody();
        assertNotNull(deweyWrapper);
    }

    @Test
    public void shouldReturnDeweyWrapperWhenNoClassIsProvided() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertNotNull(deweyWrapper);
    }

    @Test
    public void shouldReturnNullWhenWrongClassIsProvided() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("bogus", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertNull(deweyWrapper);
    }

    @Test
    public void shouldReturnDeweyWrapperContainingResources() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertNotNull(deweyWrapper.getLinks());
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/dewey");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}