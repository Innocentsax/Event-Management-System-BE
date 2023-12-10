package dev.Innocent.EventManagementSystem.api.verifyaccount;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "account_number",
        "account_name"
})
@lombok.Data
public class Data {

    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_name")
    private String accountName;

}

