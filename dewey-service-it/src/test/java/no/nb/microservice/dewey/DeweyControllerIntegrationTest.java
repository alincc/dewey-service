package no.nb.microservice.dewey;

import no.nb.commons.web.util.UserUtils;
import no.nb.microservice.dewey.core.service.IDeweyService;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by raymondk on 6/30/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
@WebIntegrationTest("server.port: 0")
@ActiveProfiles(profiles = "test")
public class DeweyControllerIntegrationTest {

    @Value("${local.server.port}")
    int port;

    @Value("${spring.dewey.deweyListPath}")
    private String deweyListPath;

    @Autowired
    IDeweyService iDeweyService;
    RestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers;
    String baseURL;

    @Before
    public void setup() {
        headers = new HttpHeaders();
        headers.add(UserUtils.SSO_HEADER, "token");
        headers.add(UserUtils.REAL_IP_HEADER, "123.45.100.1");
        baseURL = "http://localhost:" + port + "/dewey";

        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/");
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @After
    public void cleanUp() {
        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void shouldReadCorrectXML() {
        assertEquals("/dewey-list-TEST.xml", deweyListPath);
    }

    @Test
    public void shouldReturnDeweyFound() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?class=05&language=no", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldReturnDeweyFoundWhenClassIsNull() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?language=no", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldReturnDeweyFoundWhenLanguageIsNull() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?class=05", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldReturnDeweyFoundWhenClassAndLanguageIsNull() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
    }

    @Test
    public void shouldReturnDeweyFoundButOnlyWithTrail() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?class=05555&language=no", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200 ", entity.getStatusCode().is2xxSuccessful());
        assertTrue("DeweyList should be empty", entity.getBody().getDeweyList().isEmpty());
        assertFalse("DeweyPathList should not be empty", entity.getBody().getDeweyPathList().isEmpty());
    }

    @Test
    public void shouldReturnDeweyNotFoundIfWrongParameterValues() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?class=wrongClassValue&language=wrongLanguageValue", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 400 ", entity.getStatusCode().is4xxClientError());
    }

    @Test
    public void shouldReturnDeweyFoundIfWrongParameterNames() {
        ResponseEntity<DeweyWrapper> entity = restTemplate.exchange(baseURL + "/?wrongParameterName1=01&wrongParameterName2=no", HttpMethod.GET, new HttpEntity<Void>(headers), DeweyWrapper.class);
        assertTrue("Status code should be 200", entity.getStatusCode().is2xxSuccessful());
    }
}