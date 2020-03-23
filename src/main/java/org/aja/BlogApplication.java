package org.aja;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.aja.client.RxUserClient;
import org.aja.client.UserClient;
import org.aja.filter.HeaderLoggingFilter;
import org.aja.jwt.JwtAuthFilter;
import org.aja.mapper.WebApplicationExceptionMapper;
import org.aja.resources.UserRectorResource;
import org.aja.resources.UserResource;
import org.aja.service.SlowService;
import org.aja.service.UserService;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.container.DynamicFeature;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;


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

        ExecutorService executorService = environment.lifecycle()
                .executorService("my-pool-%d")
                 .minThreads(10)
                .maxThreads(200)
                .build();

        //Example for loop
        for (int i = 0; i < 10; i++) {
            executorService.execute(() -> {
                try {
                    System.out.println("T" + Thread.currentThread().getName());
                } catch (Exception e) {
                   e.printStackTrace();
                }
            });
        }


        RxUserClient rxUserClient = configuration.RxUserClient(environment);
        final UserClient userClient = new UserClient();
        environment.jersey().register(new WebApplicationExceptionMapper());
        environment.jersey().register(new UserResource(executorService, new UserService(userClient, rxUserClient)));
        environment.jersey().register(new UserRectorResource(new SlowService(userClient)));
        environment.jersey().register(new HeaderLoggingFilter());

        environment.jersey().register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, LoggingFeature.DEFAULT_MAX_ENTITY_SIZE));


     JwtAuthFilter jwtAuthFilter = new JwtAuthFilter();

        environment.jersey().register((DynamicFeature) (resourceInfo, context) -> {
            if (UserResource.class.equals(resourceInfo.getResourceClass())) {
                context.register(jwtAuthFilter);
            }

            if (UserRectorResource  .class.equals(resourceInfo.getResourceClass())) {
                context.register(jwtAuthFilter);
            }
        });
    }

}
