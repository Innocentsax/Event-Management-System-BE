package dev.Innocent.EventManagementSystem.api.getBank;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "slug",
        "code",
        "longcode",
        "gateway",
        "pay_with_bank",
        "active",
        "is_deleted",
        "country",
        "currency",
        "type",
        "id",
        "createdAt",
        "updatedAt"
})
public class Datum {

    @JsonProperty("name")
    private String name;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("code")
    private String code;
    @JsonProperty("longcode")
    private String longcode;
    @JsonProperty("gateway")
    private Object gateway;
    @JsonProperty("pay_with_bank")
    private Boolean payWithBank;
    @JsonProperty("active")
    private Boolean active;
    @JsonProperty("is_deleted")
    private Boolean isDeleted;
    @JsonProperty("country")
    private String country;
    @JsonProperty("currency")
    private String currency;
    @JsonProperty("type")
    private String type;
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("createdAt")
    private String createdAt;
    @JsonProperty("updatedAt")
    private String updatedAt;
}

