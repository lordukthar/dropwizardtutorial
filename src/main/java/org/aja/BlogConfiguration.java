package org.aja;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.setup.Environment;
import org.aja.client.RxUserClient;
import org.glassfish.jersey.client.rx.RxClient;
import org.glassfish.jersey.client.rx.java8.RxCompletionStageInvoker;
import org.glassfish.jersey.client.rx.rxjava.RxObservableInvoker;
import org.glassfish.jersey.logging.LoggingFeature;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;


public class BlogConfiguration extends Configuration {


    private RxUserClient invoiceClient;

    public String userUri;


    @Valid
    @NotNull
    @JsonProperty
    private final
    JerseyClientConfiguration jerseyClient = new JerseyClientConfiguration();



    public RxUserClient RxUserClient(Environment environment) {
        RxClient<RxObservableInvoker> jc = rxClient(environment, "accountClient");
        return new RxUserClient(jc, URI.create(userUri));
    }


    public RxClient<RxObservableInvoker> rxClient(Environment environment, String clientName) {

        return new JerseyClientBuilder(environment)
               // .withProperty(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY)
                //.withProperty(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_CLIENT, "INFO")
                .using(this.jerseyClient)
                .buildRx(clientName, RxObservableInvoker.class);
    }


}
