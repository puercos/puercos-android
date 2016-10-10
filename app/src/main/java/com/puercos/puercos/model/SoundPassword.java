package com.puercos.puercos.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by fernandoortiz on 10/10/16.
 */

public class SoundPassword {

    // Constants
    public static final int MIN_NUMBER_OF_PAUSES = 2;

    // Attributes
    private ArrayList<Integer> pauses;

    // Constructors
    public SoundPassword () {
        this.pauses = new ArrayList<Integer>();
    }
    public SoundPassword (String description) {
        this();
        String[] pausesAsString = description.split(",");

        for (String pauseAsString : pausesAsString) {
            int pause = Integer.parseInt(pauseAsString);
            pauses.add(pause);
        }
    }

    // Public methods
    public void addPauseInMilliseconds(int pause) {
        this.pauses.add(pause);
    }
    public ArrayList<Integer> getPauses() {
        return this.pauses;
    }

    // toString
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int index = 0 ; index < this.pauses.size() ; index++) {
            int pause = this.pauses.get(index);
            sb.append(String.valueOf(pause));
            if (index != this.pauses.size() - 1) {
                // If it is not the last
                sb.append(",");
            }
        }
        return sb.toString();
    }

}