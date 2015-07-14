package no.nb.microservice.dewey.rest.resourceassembler;

import no.nb.microservice.dewey.rest.model.Dewey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by raymondk on 7/14/15.
 */
public class DeweyPathResourceAssembler implements ResourceAssembler<Dewey, Dewey> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver = new HateoasPageableHandlerMethodArgumentResolver();

    @Override
    public Dewey toResource(Dewey dewey) {
        UriTemplate self = new UriTemplate(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString() + "/?class=" + dewey.getClassValue());
        dewey.add(createLink(self, null, Link.REL_SELF));

        return dewey;
    }

    private Link createLink(UriTemplate base, Pageable pageable, String rel) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);

        return new Link(new UriTemplate(builder.build().toString()), rel);
    }
}
