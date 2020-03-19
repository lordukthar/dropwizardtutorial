package org.aja.api;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.beans.ConstructorProperties;
import java.util.Arrays;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    @JsonProperty("userId")
    private final int userId;

    @JsonProperty("body")
    private String body;

    @JsonProperty("title")
    private String title;

    @ConstructorProperties({"userId", "body", "title"})
    public Post(int userId, String body, String title) {
        this.userId = userId;
        this.title = title;
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }
}
