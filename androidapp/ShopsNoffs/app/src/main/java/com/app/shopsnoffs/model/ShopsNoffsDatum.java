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
        "url"
})

public class ShopsNoffsDatum implements Serializable {

    @JsonProperty("discount")
    private Integer discount;
    @JsonProperty("exp_time")
    private Integer expTime;
    @JsonProperty("location")
    private List<Double> location = new ArrayList<Double>();
    @JsonProperty("name")
    private String name;
    @JsonProperty("orig_price")
    private Integer origPrice;
    @JsonProperty("store_name")
    private String storeName;
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
     * The expTime
     */
    @JsonProperty("exp_time")
    public Integer getExpTime() {
        return expTime;
    }

    /**
     *
     * @param expTime
     * The exp_time
     */
    @JsonProperty("exp_time")
    public void setExpTime(Integer expTime) {
        this.expTime = expTime;
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
     * The name
     */
    @JsonProperty("name")
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The origPrice
     */
    @JsonProperty("orig_price")
    public Integer getOrigPrice() {
        return origPrice;
    }

    /**
     *
     * @param origPrice
     * The orig_price
     */
    @JsonProperty("orig_price")
    public void setOrigPrice(Integer origPrice) {
        this.origPrice = origPrice;
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

    public int getDiscountedPrice(){
        float forg = getOrigPrice();
        float discount = ((float)getDiscount() / 100f);

        return (int) (forg - (forg * discount));
    }

}
