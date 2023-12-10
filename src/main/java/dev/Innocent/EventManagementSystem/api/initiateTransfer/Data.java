
package dev.Innocent.EventManagementSystem.api.initiateTransfer;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "integration",
    "domain",
    "amount",
    "currency",
    "source",
    "reason",
    "recipient",
    "status",
    "transfer_code",
    "id",
    "createdAt",
    "updatedAt"
})
public class Data {

    @JsonProperty("integration")
    private Integer integration;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("amount")
    private Integer amount;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("source")
    private String source;
    @JsonProperty("reason")
    private String reason;
    @JsonProperty("recipient")
    private Integer recipient;
    @JsonProperty("status")
    private String status;
    @JsonProperty("transfer_code")
    private String transferCode;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;

}
