package org.aja.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Priority;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.MultivaluedMap;
import java.util.stream.Collectors;

@Priority(100)
public class HeaderLoggingFilter implements ContainerRequestFilter {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public void filter(ContainerRequestContext requestContext) {

        if (log.isDebugEnabled()) {
            MultivaluedMap<String, String> headers = requestContext.getHeaders();
            log.debug("Headers: {}", headers.keySet().stream()
                    .map(k -> "[" + k + "=" + headers.get(k).stream().map(String::toString).collect(Collectors.joining(", ")) + "]")
                    .collect(Collectors.joining(", "))
            );
        }
    }

}
