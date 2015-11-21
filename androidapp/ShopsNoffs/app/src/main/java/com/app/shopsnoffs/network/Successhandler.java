package com.app.shopsnoffs.network;

import com.app.shopsnoffs.model.Json;

/**
 * Created by stefan on 17.10.15.
 */
public interface Successhandler {

    public void onSuccess(Json json);

    public void onFailure();

}
