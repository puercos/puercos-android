package com.puercos.puercos.networking;

/**
 * Created by fernandoortiz on 10/10/16.
 */

public interface NetworkListener {
    /**
     * Method called when the network operation
     * was successful
     * */
    void onSuccess(String result);

    /**
     * Method called then the network
     * operation fails
     * */
    void onError(String errorReason);
}
