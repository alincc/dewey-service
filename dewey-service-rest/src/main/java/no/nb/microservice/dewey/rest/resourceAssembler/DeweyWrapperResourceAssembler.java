package no.nb.microservice.dewey.rest.resourceAssembler;

import no.nb.microservice.dewey.rest.model.Dewey;
import no.nb.microservice.dewey.rest.model.DeweyWrapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by raymondk on 7/13/15.
 */
public class DeweyWrapperResourceAssembler implements ResourceAssembler<DeweyWrapper, DeweyWrapper> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver = new HateoasPageableHandlerMethodArgumentResolver();

    @Override
    public DeweyWrapper toResource(DeweyWrapper deweyWrapper) {
        UriTemplate self = new UriTemplate(ServletUriComponentsBuilder.fromCurrentRequest().build().toString());
        deweyWrapper.add(createLink(self, null, Link.REL_SELF));

        UriTemplate first = new UriTemplate(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString());
        deweyWrapper.add(createLink(first, null, Link.REL_FIRST));

        return deweyWrapper;
    }



    private Link createLink(UriTemplate base, Pageable pageable, String rel) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);

        return new Link(new UriTemplate(builder.build().toString()), rel);
    }
}