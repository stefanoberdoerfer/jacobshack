package com.app.shopsnoffs.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonAnyGetter;
import org.codehaus.jackson.annotate.JsonAnySetter;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.annotate.JsonPropertyOrder;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonPropertyOrder({
        "discount",
        "exp_time",
        "location",
        "name",
        "orig_price",
        "store_name",
        "item_name",
        "url"
})

public class ShopsNoffsShop implements Serializable {


    @JsonProperty("discount")
    private Integer discount;
    @JsonProperty("location")
    private List<Double> location = new ArrayList<Double>();
    @JsonProperty("store_name")
    private String storeName;
    @JsonProperty("item_name")
    private String itemName;
    @JsonProperty("url")
    private String url;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    /**
     *
     * @return
     * The discount
     */
    @JsonProperty("discount")
    public Integer getDiscount() {
        return discount;
    }

    /**
     *
     * @param discount
     * The discount
     */
    @JsonProperty("discount")
    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    /**
     *
     * @return
     * The location
     */
    @JsonProperty("location")
    public List<Double> getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    @JsonProperty("location")
    public void setLocation(List<Double> location) {
        this.location = location;
    }


    /**
     *
     * @return
     * The storeName
     */
    @JsonProperty("store_name")
    public String getStoreName() {
        return storeName;
    }

    /**
     *
     * @param itemName
     * The item_name
     */
    @JsonProperty("item_name")
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     *
     * @return
     * The storeName
     */
    @JsonProperty("item_name")
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @param storeName
     * The store_name
     */
    @JsonProperty("store_name")
    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    /**
     *
     * @return
     * The url
     */
    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     * The url
     */
    @JsonProperty("url")
    public void setUrl(String url) {
        this.url = url;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }


}
