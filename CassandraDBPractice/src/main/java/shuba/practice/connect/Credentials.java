package shuba.practice.connect;


import com.fasterxml.jackson.annotation.JsonProperty;

public class Credentials {

    //@JsonIgnore
    @JsonProperty("clientId")
    private String id;

    private String secret;
    private String token;

    // рефлексия, он без сеттеров делает как?

    public String getClientId() {
        return id;
    }

    public String getSecret() {
        return secret;
    }

    public String getToken() {
        return token;
    }
}
