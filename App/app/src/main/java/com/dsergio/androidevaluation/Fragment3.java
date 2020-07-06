package com.dsergio.androidevaluation;

import android.os.Bundle;

@SuppressWarnings("unused")
public class Fragment3 extends FragmentBase {

    private static Fragment3 uniqueInstance;

    private static final String ARG_PARAM1 = "param1";

    public Fragment3() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return instance of fragment Fragment3.
     */
    public static synchronized Fragment3 newInstance(String param1) {
        Fragment3 fragment;
        if (uniqueInstance == null) {
            fragment = new Fragment3();
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
        return "fragment_3.json";
    }
    @Override
    public String getNextFragmentFileName() {
        return "fragment_1.json";
    }
}