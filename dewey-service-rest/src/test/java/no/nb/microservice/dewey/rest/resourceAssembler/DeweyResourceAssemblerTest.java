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
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by raymondk on 7/14/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeweyResourceAssemblerTest {

    @Mock
    IDeweyService iDeweyService;
    DeweyController deweyController;

    private String baseUrl;
    private String deweyListPath = "/dewey-list-TEST.xml";

    @Before
    public void setup() {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        baseUrl = "http://localhost";

        iDeweyService = new DeweyServiceImpl(messageSource());
        deweyController = new DeweyController(iDeweyService);
        ReflectionTestUtils.setField(iDeweyService, "deweyListPath", deweyListPath);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void deweyShouldHaveSelfLinkWhenClassIsempty() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyShouldNotHavePrevLinkWhenClassIsEmpty() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertTrue("Should not have PREV link", dewey.getLink("prev") == null);
        }
    }

    @Test
    public void deweyShouldHaveSelfLink1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyShouldHavePrevLink1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertFalse("Should have PREV link", dewey.getLink("prev").getHref().isEmpty());
        }
    }

    @Test
    public void deweyShouldHaveSelfLink2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyShouldHavePrevLink2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertFalse("Should have PREV link", dewey.getLink("prev").getHref().isEmpty());
        }
    }

    @Test
    public void deweyPathShouldNotHaveSelfLink() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertTrue("Should not have SELF link", dewey.getLink("self") == null);
        }
    }

    @Test
    public void deweyPathShouldHaveSelfLink1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyPathShouldHaveSelfLink2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyPathShouldHaveSelfLink3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertFalse("Should have SELF link", dewey.getLink("self").getHref().isEmpty());
        }
    }

    @Test
    public void deweyPathSelfLinkShouldContainClassValueInUrl1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweyPathSelfLinkShouldContainClassValueInUrl2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweyPathSelfLinkShouldContainClassValueInUrl3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweyPathSelfLinkShouldContainClassValueInUrl4() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyPathList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweySelfLinkShouldContainClassValueInUrl1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey(null, "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweySelfLinkShouldContainClassValueInUrl2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweySelfLinkShouldContainClassValueInUrl3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweySelfLinkShouldContainClassValueInUrl4() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=" + dewey.getClassValue(), dewey.getLink("self").getHref());
        }
    }

    @Test
    public void deweyPrevLinkShouldContainCorrectValue1() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("0", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/", dewey.getLink("prev").getHref());
        }
    }

    @Test
    public void deweyPrevLinkShouldContainCorrectValue2() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("05", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=0", dewey.getLink("prev").getHref());
        }
    }

    @Test
    public void deweyPrevLinkShouldContainCorrectValue3() {
        ResponseEntity<DeweyWrapper> entity = deweyController.dewey("054", "no", null);
        DeweyWrapper deweyWrapper = entity.getBody();
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(baseUrl + "/?class=05", dewey.getLink("prev").getHref());
        }
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/dewey");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}