package com.app.shopsnoffs.network;

import android.util.Log;

import com.app.shopsnoffs.model.Json;
import com.app.shopsnoffs.model.ShopsNoffsData;
import com.app.shopsnoffs.model.ShopsNoffsShop;
import com.app.shopsnoffs.model.ShopsNoffsShopData;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.apache.commons.io.IOUtils;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 17.10.15.
 */
public class Request extends SpringAndroidSpiceRequest<Json> {

    private RequestType resType;
    private String params;

    public Request(RequestType requestType, String urlParameters) {
        super(Json.class);
        this.resType = requestType;
        this.params = urlParameters;
    }

    @Override
    public Json loadDataFromNetwork() throws Exception {

        Log.i("Request", "resType: " + resType);
        switch (resType) {
            case getAllOffers:
                return initRest().getForObject("http://52.88.63.250/getoffers", ShopsNoffsData.class);
            case getOffersByLoc:
                Log.e("GET::","http://52.88.63.250/getoffersbydistance" + params);
                return initRest().getForObject("http://52.88.63.250/getoffersbydistance" + params, ShopsNoffsShopData.class);
            case getOffersByName:
                Log.e("GET::","http://52.88.63.250/getoffersbyName" + params);
                return initRest().getForObject("http://52.88.63.250/getoffersbyname" + params, ShopsNoffsShopData.class);
            case getOffersByCategory:
                Log.e("GET::","http://52.88.63.250/getoffersbycategory" + params);
                return initRest().getForObject("http://52.88.63.250/getoffersbycategory" + params, ShopsNoffsShopData.class);
            default:
                return null;
        }
    }

    public RestTemplate initRest() {
        MappingJacksonHttpMessageConverter mjhmc = new MappingJacksonHttpMessageConverter();
        List<MediaType> medList = new ArrayList<>();
        medList.add(MediaType.APPLICATION_JSON);
        medList.add(MediaType.TEXT_HTML);
        mjhmc.setSupportedMediaTypes(medList);

        FormHttpMessageConverter conv = new FormHttpMessageConverter();
        conv.setSupportedMediaTypes(medList);


        RestTemplate restTemplate = new RestTemplate();

        restTemplate.getMessageConverters().add(conv);
        restTemplate.getMessageConverters().add(mjhmc);


        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();

        interceptors.add(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

                ClientHttpResponse response;
                response = execution.execute(request, body);

                Log.i("Networkresponse", "start");
                try {
                    String rawString = IOUtils.toString(response.getBody(), "UTF-8");
                    Log.i("RESPONSE: RAWSTRING: ", rawString);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return response;
            }
        });

        //restTemplate.setInterceptors(interceptors);


        return restTemplate;
    }
}
