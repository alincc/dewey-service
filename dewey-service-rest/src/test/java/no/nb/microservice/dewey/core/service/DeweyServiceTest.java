package no.nb.microservice.dewey.core.service;

import no.nb.microservice.dewey.Application;
import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by raymondk on 6/29/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DeweyServiceTest {

    @Autowired
    private IDeweyService iDeweyService;

    @Test
    public void deweyWrapperShouldReturnDeweyWrapperObject() {
        assertEquals(iDeweyService.getDeweyWrapper("001", "no") instanceof DeweyWrapper, true);
    }

    @Test
    public void deweyWrapperShouldContainListWithAtLeastOneRecord() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        assertTrue("DeweyList should contain one or more records", deweyWrapper.getDeweyList().size() >= 1);
    }

    @Test
    public void shouldReturnAllLevelOneDewey() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper(null, "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(1, dewey.getLevel());
        }
    }

    @Test
    public void shouldReturnAllLevelTwoDewey() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("0", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(2, dewey.getLevel());
        }
    }

    @Test
    public void shouldReturnAllLevelThreeDewey() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("00", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(3, dewey.getLevel());
        }
    }

    @Test
    public void shouldReturnOnlyTrail() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("052", "no");
        assertEquals(0, deweyWrapper.getDeweyList().size());
        assertEquals(3, deweyWrapper.getDeweyPathList().size());
    }

    @Test
    public void deweyLevel() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        Dewey dewey = deweyWrapper.getDeweyList().get(0);
        assertEquals(3, dewey.getLevel());
    }

    @Test
    public void deweyClassValue() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        Dewey firstDewey = deweyWrapper.getDeweyList().get(0);
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("050", firstDewey.getClassValue());
        assertEquals("054", dewey.getClassValue());
    }

    @Test
    public void deweyHeading() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldGiveEnglishTranslationForHeading() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "en");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Serials in French, Occitan & Catalan", dewey.getHeading());
    }

    @Test
    public void shouldGiveNorwegianTranslationWhenBogusLanguage() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "bogus");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldContainCorrectNumberOfTrails() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        assertEquals(2, deweyWrapper.getDeweyPathList().size());
    }

    @Test
    public void shouldContainCorrectTrail() {
        DeweyWrapper deweyWrapper = iDeweyService.getDeweyWrapper("05", "no");
        Dewey first = deweyWrapper.getDeweyPathList().get(0);
        Dewey second = deweyWrapper.getDeweyPathList().get(1);

        assertEquals("0", first.getClassValue());
        assertEquals("Informatikk, informasjonsvitenskap og generell verker", first.getHeading());
        assertEquals("05", second.getClassValue());
        assertEquals("Periodika", second.getHeading());
    }
}