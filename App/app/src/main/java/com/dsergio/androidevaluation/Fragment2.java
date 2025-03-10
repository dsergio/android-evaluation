package com.dsergio.androidevaluation;

import android.os.Bundle;

/**
 * Fragment class
 */
@SuppressWarnings("unused")
public class Fragment2 extends FragmentBase {

    private static Fragment2 uniqueInstance;
    private static final String ARG_PARAM1 = "param1";


    public Fragment2() {
        // Required empty public constructor
    }

    /**
     * Singleton factory method
     *
     * @param param1 Parameter 1.
     * @return instance of fragment Fragment2.
     */
    public static synchronized Fragment2 newInstance(String param1) {
        Fragment2 fragment;
        if (uniqueInstance == null) {
            fragment = new Fragment2();
        } else {
            fragment = uniqueInstance;
        }

        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
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
        return "fragment_2.json";
    }
    @Override
    public String getNextFragmentFileName() {
        return "fragment_3.json";
    }
}