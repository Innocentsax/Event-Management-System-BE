package dev.Innocent.EventManagementSystem.api.verifyaccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "message",
        "data"
})
@lombok.Data
public class VerifyAccount {

    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Data data;
}

