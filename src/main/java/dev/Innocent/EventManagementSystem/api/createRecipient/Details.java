
package dev.Innocent.EventManagementSystem.api.createRecipient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "authorization_code",
    "account_number",
    "account_name",
    "bank_code",
    "bank_name"
})
public class Details {

    @JsonProperty("authorization_code")
    private Object authorizationCode;
    @JsonProperty("account_number")
    private String accountNumber;
    @JsonProperty("account_name")
    private String accountName;
    @JsonProperty("bank_code")
    private String bankCode;
    @JsonProperty("bank_name")
    private String bankName;
}
