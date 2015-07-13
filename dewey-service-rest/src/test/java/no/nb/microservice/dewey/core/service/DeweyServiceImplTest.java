package no.nb.microservice.dewey.core.service;

import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.internal.matchers.Null;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

/**
 * Created by raymondk on 6/29/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class DeweyServiceImplTest {
    @Mock
    DeweyServiceImpl deweyService;

    private String deweyListPath = "/dewey-list-TEST.xml";

    @Before
    public void setup() {
        deweyService = new DeweyServiceImpl(messageSource());
    }

    @Test
    public void shouldReturnExceptionWhenWrongXML() {
        assertEquals(null, deweyService.getDeweyWrapper("/bogus.xml", "05", null));
    }

    @Test
    public void deweyWrapperShouldReturnDeweyWrapperObject() {
        assertEquals(deweyService.getDeweyWrapper(deweyListPath, "05", "no") instanceof DeweyWrapper, true);
    }

    @Test
    public void deweyWrapperShouldContainListWithAtLeastOneRecord() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "no");
        assertFalse("DeweyList should not be empty", deweyWrapper.getDeweyList().isEmpty());
    }

    @Test
    public void shouldReturnAllLevelOneDewey() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, null, "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(1, dewey.getLevel());
        }
    }

    @Test
    public void shouldReturnAllLevelTwoDewey() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "0", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(2, dewey.getLevel());
        }
    }

    @Test
    public void shouldReturnAllLevelThreeDewey() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "00", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(3, dewey.getLevel());
        }
    }

    @Test
    public void deweyClassValue1() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, null, "no");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("4", dewey.getClassValue());
    }

    @Test
    public void deweyClassValue2() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "no");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("054", dewey.getClassValue());
    }

    @Test
    public void deweyHeading() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "no");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldGiveEnglishTranslationForHeading() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "en");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Serials in French, Occitan & Catalan", dewey.getHeading());
    }

    @Test
    public void shouldGiveNorwegianTranslationWhenNoLanguage() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", null);
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldGiveNorwegianTranslationWhenBogusLanguage() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "bogus");
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldReturnOnlyTrail() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "052", "no");
        assertEquals(0, deweyWrapper.getDeweyList().size());
        assertEquals(3, deweyWrapper.getDeweyPathList().size());
    }

    @Test
    public void shouldContainCorrectNumberOfTrails() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "no");
        assertEquals(2, deweyWrapper.getDeweyPathList().size());
    }

    @Test
    public void shouldContainCorrectTrail() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(deweyListPath, "05", "no");
        Dewey first = deweyWrapper.getDeweyPathList().get(0);
        Dewey second = deweyWrapper.getDeweyPathList().get(1);

        assertEquals("0", first.getClassValue());
        assertEquals("Informatikk, informasjonsvitenskap og generell verker", first.getHeading());
        assertEquals("05", second.getClassValue());
        assertEquals("Periodika", second.getHeading());
    }

    private MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasenames("classpath:i18n/dewey");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}