package com.puercos.puercos.networking;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by fernandoortiz on 10/10/16.
 */

public abstract class HTTPClient {

    // Attributes
    protected Context ctx;

    // Getters and setters
    public Context getCtx() {
        return ctx;
    }
    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    // Protected members
    protected RequestQueue buildRequestQueue() {
        RequestQueue queue = Volley.newRequestQueue(ctx);
        return queue;
    }
    protected String buildRequestDescription(String url) {
        return "Network request sent to " + url;
    }

    // Public members
    public void performRequest(String url, final NetworkListener listener) {
        // Builds the request queue
        RequestQueue queue = buildRequestQueue();
        Log.d("NETWORKING", buildRequestDescription(url));

        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError(error.toString());
                    }
                }
        );

        queue.add(request);
    }

}
















