package com.puercos.puercos.networking;

import android.content.Context;

import com.puercos.puercos.model.SoundPassword;

/**
 * Created by fernandoortiz on 10/10/16.
 */

public class NetworkManager extends HTTPClient {

    // URL
    private final String BASE_URL = "http://192.168.0.50";

    private final String OPEN_DOOR_URL          = BASE_URL + "/?dooropen";
    private final String CLOSE_DOOR_URL         = BASE_URL + "/?doorclose";
    private final String CHANGE_PASSWORD_URL    = BASE_URL + "/?password";

    // Singleton
    private static NetworkManager instance = null;
    protected NetworkManager() {
        // Exists only to defeat instantiation.
    }
    public static NetworkManager getInstance(Context ctx) {
        if(instance == null) {
            instance = new NetworkManager();
        }
        instance.setCtx(ctx);
        return instance;
    }

    // Public methods
    public void openDoor(NetworkListener listener) {
        super.performRequest(OPEN_DOOR_URL, listener);
    }
    public void closeDoor(NetworkListener listener) {
        super.performRequest(CLOSE_DOOR_URL, listener);
    }
    public void changePassword(SoundPassword password, NetworkListener listener) {
        StringBuilder sb = new StringBuilder();
        sb.append(CHANGE_PASSWORD_URL);
        sb.append("=");
        sb.append(password.toString());
        String url = sb.toString();
        super.performRequest(url, listener);
    }

}
