package no.nb.microservice.dewey.rest.resourceAssembler;

import no.nb.microservice.dewey.rest.model.Dewey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.HateoasPageableHandlerMethodArgumentResolver;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.UriTemplate;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Created by raymondk on 7/13/15.
 */
public class DeweyResourceAssembler implements ResourceAssembler<Dewey, Dewey> {

    private final HateoasPageableHandlerMethodArgumentResolver pageableResolver = new HateoasPageableHandlerMethodArgumentResolver();

    @Override
    public Dewey toResource(Dewey dewey) {
        UriTemplate self = new UriTemplate(ServletUriComponentsBuilder.fromCurrentContextPath().build().toString() + "/?class=" + dewey.getClassValue());
        dewey.add(createLink(self, null, Link.REL_SELF));

        //Next Link
        if (dewey.getClassValue().length() > 1) {
            String prevClass = null;
            if (dewey.getClassValue().length() > 2) {
                prevClass = dewey.getClassValue().substring(0, dewey.getClassValue().length()-(dewey.getClassValue().length()-1));
            }

            String uri = ServletUriComponentsBuilder.fromCurrentContextPath().build().toString();
            if (prevClass == null || prevClass.isEmpty()) {
                uri += "/";
            } else {
                uri += "/?class=" + prevClass;
            }
            dewey.add(createLink(new UriTemplate(uri), null, Link.REL_PREVIOUS));
        }

        return dewey;
    }

    private Link createLink(UriTemplate base, Pageable pageable, String rel) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromUri(base.expand());
        pageableResolver.enhance(builder, null, pageable);

        return new Link(new UriTemplate(builder.build().toString()), rel);
    }
}
