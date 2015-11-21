package com.app.shopsnoffs.network;

import com.app.shopsnoffs.model.Json;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

/**
 * Created by stefan on 17.10.15.
 */
public class CustomRequestListener implements
        RequestListener<Json> {

    Successhandler c;

    public CustomRequestListener(Successhandler c){
        super();
        this.c = c;
    }

    @Override
    public void onRequestFailure(SpiceException spiceException) {
        c.onFailure();
    }

    @Override
    public void onRequestSuccess(Json json) {
        c.onSuccess(json);
    }
}
