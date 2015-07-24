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
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        ReflectionTestUtils.setField(deweyService, "deweyListPath", deweyListPath);
    }

    @Test
    public void shouldReturnNullWhenWrongXML() {
        ReflectionTestUtils.setField(deweyService, "deweyListPath", "bogus.xml");
        assertEquals(null, deweyService.getDeweyWrapper("05", null));
    }

    @Test
    public void deweyWrapperShouldReturnDeweyWrapperObject() {
        assertEquals(deweyService.getDeweyWrapper("05", "no") instanceof DeweyWrapper, true);
    }

    @Test
    public void deweyWrapperShouldContainListWithAtLeastOneRecord() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("05", "no");
        assertFalse("DeweyList should not be empty", deweyWrapper.getDeweyList().isEmpty());
    }

    @Test
    public void deweyLevelOnevalues() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper(null, "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(1, dewey.getLevel());
        }
        Dewey dewey0 = deweyWrapper.getDeweyList().get(0);
        assertEquals("0", dewey0.getClassValue());
        assertEquals("Informatikk, informasjonsvitenskap og generell verker", dewey0.getHeading());

        Dewey dewey4 = deweyWrapper.getDeweyList().get(4);
        assertEquals("4", dewey4.getClassValue());
        assertEquals("Språk", dewey4.getHeading());

        assertTrue("Should not contain any trails", deweyWrapper.getDeweyPathList().size() == 0);
    }

    @Test
    public void deweyLevelTwovalues() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("3", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(2, dewey.getLevel());
        }
        Dewey dewey0 = deweyWrapper.getDeweyList().get(0);
        assertEquals("30", dewey0.getClassValue());
        assertEquals("Samfunnsvitenskap, sosiologi og antropologi", dewey0.getHeading());

        Dewey dewey4 = deweyWrapper.getDeweyList().get(4);
        assertEquals("34", dewey4.getClassValue());
        assertEquals("Rettsvitenskap", dewey4.getHeading());

        assertTrue("Should contain 1 trail", deweyWrapper.getDeweyPathList().size() == 1);
        Dewey lastTrail = deweyWrapper.getDeweyPathList().get(deweyWrapper.getDeweyPathList().size()-1);
        assertEquals("3", lastTrail.getClassValue());
        assertEquals("Samfunnsvitenskap", lastTrail.getHeading());

    }

    @Test
    public void deweyLevelThreevalues() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("36", "no");
        for (Dewey dewey : deweyWrapper.getDeweyList()) {
            assertEquals(3, dewey.getLevel());
        }
        Dewey dewey0 = deweyWrapper.getDeweyList().get(0);
        assertEquals("360", dewey0.getClassValue());
        assertEquals("Sosiale problemer og tjenester; foreninger", dewey0.getHeading());

        Dewey dewey4 = deweyWrapper.getDeweyList().get(4);
        assertEquals("364", dewey4.getClassValue());
        assertEquals("Kriminologi", dewey4.getHeading());

        assertTrue("Should contain 2 trails", deweyWrapper.getDeweyPathList().size() == 2);
        Dewey lastTrail = deweyWrapper.getDeweyPathList().get(deweyWrapper.getDeweyPathList().size()-1);
        assertEquals("36", lastTrail.getClassValue());
        assertEquals("Sosiale problemer og sosiale tjenester", lastTrail.getHeading());
    }

    @Test
    public void shouldReturnOnlyTrail() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("052", "no");
        assertEquals(0, deweyWrapper.getDeweyList().size());
        assertEquals(3, deweyWrapper.getDeweyPathList().size());

        Dewey lastTrail = deweyWrapper.getDeweyPathList().get(deweyWrapper.getDeweyPathList().size() - 1);
        assertEquals("052", lastTrail.getClassValue());
        assertEquals("Generelle periodika på engelsk", lastTrail.getHeading());
    }

    @Test
    public void shouldGiveEnglishTranslationForHeading() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("05", "en");

        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Serials in French, Occitan & Catalan", dewey.getHeading());

        Dewey trail = deweyWrapper.getDeweyPathList().get(deweyWrapper.getDeweyPathList().size()-1);
        assertEquals("Magazines, journals & serials", trail.getHeading());
    }

    @Test
    public void shouldGiveNorwegianTranslationWhenNoLanguage() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("05", null);
        Dewey dewey = deweyWrapper.getDeweyList().get(4);
        assertEquals("Generelle periodika på fransk, oksitansk, katalansk", dewey.getHeading());
    }

    @Test
    public void shouldGiveNorwegianTranslationWhenBogusLanguage() {
        DeweyWrapper deweyWrapper = deweyService.getDeweyWrapper("05", "bogus");
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