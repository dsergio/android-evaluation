package com.dsergio.androidevaluation;

import org.json.JSONObject;

/**
 *
 * MainActivity must implement this interface. This is used so that the Fragment class can be decoupled from the Activity class.
 *
 */
public interface MyListener {
    void swapFragment(String fileName);
    void incrementCurrentFileNameIndex();
    void decrementCurrentFileNameIndex();
    JSONObject parseJson(String fileName);
    String getPackageName();
}
