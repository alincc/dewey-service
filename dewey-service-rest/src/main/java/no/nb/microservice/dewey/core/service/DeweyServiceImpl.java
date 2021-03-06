package no.nb.microservice.dewey.core.service;

import no.nb.microservice.dewey.rest.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by raymondk on 6/29/15.
 */
@Service
public class DeweyServiceImpl implements IDeweyService {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDeweyService.class);
    private DeweyRoot deweyRoot;
    private DeweyWrapper deweyWrapper;
    private String classValue;
    private String language;
    private String level;
    private MessageSource messageSource;

    @Value("${spring.dewey.deweyListPath}")
    private String deweyListPath;

    @Autowired
    public DeweyServiceImpl(MessageSource messageSource) {
        super();
        this.messageSource = messageSource;
    }

    @Override
    public DeweyWrapper getDeweyWrapper(String classValue, String language) {
        populateDeweyRoot();

        if (deweyRoot != null) {
            this.classValue = classValue;
            this.language = setCorrectLanguage(language);
            this.deweyWrapper = new DeweyWrapper();
            List<Dewey> deweyList = new ArrayList<>();

            if (classValue == null || classValue.isEmpty()) {
                level = "1";
                for (Discipline discipline : deweyRoot.getDisciplines()) {
                    getRecords(deweyList, discipline);
                }
            } else {
                level = String.valueOf(classValue.length() + 1);
                int disciplineNumber = Integer.parseInt(classValue.substring(0, 1));
                Discipline discipline = deweyRoot.getDisciplines().get(disciplineNumber);
                getRecords(deweyList, discipline);
            }
            deweyWrapper.setDeweyList(deweyList);
        }
        return deweyWrapper;
    }

    private void getRecords(List<Dewey> deweyList, Discipline discipline) {
        for (Record record : discipline.getRecord()) {
            if (classValue == null) {
                if (record.getLevel().equalsIgnoreCase(level)) {
                    deweyList.add(new Dewey(Integer.parseInt(record.getLevel()), record.getDeweyClass(), messageSource.getMessage(record.getDeweyClass(), null, new Locale(language)), 0));
                }
            } else {
                if (record.getLevel().equalsIgnoreCase(level) && record.getDeweyClass().startsWith(classValue)) {
                    deweyList.add(new Dewey(Integer.parseInt(record.getLevel()), record.getDeweyClass(), messageSource.getMessage(record.getDeweyClass(), null, new Locale(language)), 0));
                }
            }
            getTrail(record);
        }
    }

    public void getTrail(Record record) {
        if (classValue != null) {
            for (int i = 1; i <= classValue.length(); i++) {
                if (record.getDeweyClass().equals(classValue.substring(0, i))) {
                    Dewey dewey = new Dewey(Integer.parseInt(record.getLevel()), record.getDeweyClass(), messageSource.getMessage(record.getDeweyClass(), null, new Locale(language)), 0);
                    deweyWrapper.getDeweyPathList().add(dewey);
                }
            }
        }
    }

    public Map<String, Integer> getCount() {

        return null;
    }

    public void populateDeweyRoot() {
        InputStream is = null;
        try {
            JAXBContext context = JAXBContext.newInstance(DeweyRoot.class, Discipline.class, Record.class);
            Unmarshaller u = context.createUnmarshaller();
            ClassPathResource cpr = new ClassPathResource(deweyListPath);
            is = cpr.getInputStream();
            this.deweyRoot = (DeweyRoot) u.unmarshal(is);
        } catch (Exception ex) {
            LOGGER.error("Error loading " + deweyListPath, ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    LOGGER.error("Error closing " + deweyListPath + " stream", ex);
                }
            }
        }
    }

    private String setCorrectLanguage(String lang) {
        if (lang != null && ("no".equalsIgnoreCase(lang) || "en".equalsIgnoreCase(lang))) {
            return lang;
        }
        return "no";
    }
}