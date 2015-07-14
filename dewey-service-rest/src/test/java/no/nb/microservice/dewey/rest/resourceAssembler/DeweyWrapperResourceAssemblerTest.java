package no.nb.microservice.dewey.rest.resourceAssembler;

import no.nb.microservice.dewey.core.service.DeweyServiceImpl;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.controller.DeweyController;
import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by raymondk on 7/14/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeweyWrapperResourceAssemblerTest {

    @Mock
    IDeweyService iDeweyService;
    DeweyController deweyController;

    private String deweyListPath = "/dewey-list-TEST.xml";

    @Before
    public void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);

        iDeweyService = new DeweyServiceImpl(messageSource());
        deweyController = new DeweyController(iDeweyService);
        ReflectionTestUtils.setField(iDeweyService, "deweyListPath", deweyListPath);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void deweyWrapperShouldHaveSelfLink1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have SELF link", deweyWrapper.getLink("self").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveFirstLink1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have FIRST link", deweyWrapper.getLink("first").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveSelfLink2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have SELF link", deweyWrapper.getLink("self").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveFirstLink2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have FIRST link", deweyWrapper.getLink("first").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveSelfLink3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have SELF link", deweyWrapper.getLink("self").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveFirstLink3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have FIRST link", deweyWrapper.getLink("first").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveSelfLink4() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have SELF link", deweyWrapper.getLink("self").getHref().isEmpty());
    }

    @Test
    public void deweyWrapperShouldHaveFirstLink4() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        assertFalse("Should have FIRST link", deweyWrapper.getLink("first").getHref().isEmpty());
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/dewey");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}