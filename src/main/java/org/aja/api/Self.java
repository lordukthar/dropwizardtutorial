package org.aja.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;

public class Self {

    @JsonProperty("href")
    private final String href;

    @ConstructorProperties({"href"})
    public Self(String href) {
        this.href = href;
    }

    public String getHref() {
        return href;
    }
}
