package com.dsergio.androidevaluation;

import org.json.JSONObject;

public interface MyListener {
    void swapFragment(String fileName);
    void incrementCurrentFileNameIndex();
    JSONObject parseJson(String fileName);
    String getPackageName();
}
