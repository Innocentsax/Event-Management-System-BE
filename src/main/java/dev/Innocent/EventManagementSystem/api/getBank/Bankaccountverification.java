package dev.Innocent.EventManagementSystem.api.getBank;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "message",
        "data",
        "meta"
})
public class Bankaccountverification {

    @JsonProperty("status")
    private Boolean status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private List<Datum> data;
    @JsonProperty("meta")
    private Meta meta;
}

