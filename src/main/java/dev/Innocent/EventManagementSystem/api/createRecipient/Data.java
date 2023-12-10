
package dev.Innocent.EventManagementSystem.api.createRecipient;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "active",
    "createdAt",
    "currency",
    "domain",
    "id",
    "integration",
    "name",
    "recipient_code",
    "type",
    "updatedAt",
    "is_deleted",
    "details"
})
@lombok.Data
public class Data {

    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("domain")
    private String domain;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("integration")
    private Integer integration;
    @JsonProperty("name")
    private String name;
    @JsonProperty("recipient_code")
    private String recipientCode;
    @JsonProperty("type")
    private String type;
    @JsonProperty("updatedAt")
    private String updatedAt;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @JsonProperty("details")
    private Details details;
}
