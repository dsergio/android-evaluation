package com.dsergio.androidevaluation;

import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment1#newInstance} factory method to
 * create an instance of this fragment.
 */
@SuppressWarnings("unused")
public class Fragment1 extends FragmentBase {

    private static Fragment1 uniqueInstance;

    private static final String ARG_PARAM1 = "param1";

    public Fragment1() {
        // Required empty public constructor
    }

    /**
     *
     *
     * @param param1 Parameter 1.
     * @return instance of fragment Fragment1.
     */
    public static synchronized Fragment1 newInstance(String param1) {
        Fragment1 fragment;
        if (uniqueInstance == null) {
            fragment = new Fragment1();
        } else {
            fragment = uniqueInstance;
        }

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        Log.d("Fragment1", "param1: " + param1);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public String getCurrentFragmentFileName() {
        return "fragment_1.json";
    }
    @Override
    public String getNextFragmentFileName() {
        return "fragment_2.json";
    }

}