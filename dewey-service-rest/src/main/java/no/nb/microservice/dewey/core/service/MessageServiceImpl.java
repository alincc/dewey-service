package no.nb.microservice.dewey.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

/**
 * Created by raymondk on 7/1/15.
 */
@Service
public class MessageServiceImpl implements IMessageService {

    @Autowired
    MessageSource messageSource;

    @Override
    public String getLanguageTranslation(String id, String locale) {
        return messageSource.getMessage(id, null, new Locale(locale));
    }
}
