package no.nb.microservice.dewey.core.service;

import no.nb.microservice.dewey.rest.model.DeweyWrapper;

/**
 * Created by raymondk on 6/29/15.
 */
public interface IDeweyService {

    DeweyWrapper getDeweyWrapper(String classValue, String language);
}
