package org.aja;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.aja.client.UserClient;
import org.aja.mapper.WebApplicationExceptionMapper;
import org.aja.resources.UserResource;


public class BlogApplication extends Application<BlogConfiguration> {

    public static void main(final String[] args) throws Exception {
        new BlogApplication().run(args);
    }

    @Override
    public String getName() {
        return "Blog";
    }

    @Override
    public void initialize(final Bootstrap<BlogConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final BlogConfiguration configuration,
                    final Environment environment) {
        // TODO: implement application

        environment.jersey().register(new UserResource(new UserClient()));
        environment.jersey().register(new WebApplicationExceptionMapper());
    }

}
