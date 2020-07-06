package com.dsergio.androidevaluation;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public abstract class FragmentBase extends Fragment implements View.OnClickListener, IFragment {

    protected MainActivity activity;
    protected String currentFragmentFileName;
    protected String nextFragmentFileName;
    protected String mParam1;

    @Override
    public void onClick(View view) {
        activity.swapFragment(nextFragmentFileName);
        activity.incrementCurrentFileNameIndex();
    }

    @Override
    public void setNextFragmentFileName() {
        nextFragmentFileName = getNextFragmentFileName();
    }
    @Override
    public void setCurrentFragmentFileName() { currentFragmentFileName = getCurrentFragmentFileName(); }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            activity = (MainActivity) context;
        }
        setNextFragmentFileName();
        setCurrentFragmentFileName();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        JSONObject jsonObject = activity.parseJson(currentFragmentFileName);

        String fragmentLayoutId;
        try {
            fragmentLayoutId = jsonObject.getString("fragmentLayout");

            String packageName = activity.getPackageName();
            int resId = getResources().getIdentifier(fragmentLayoutId, "layout", packageName);

            View view = inflater.inflate(resId, container, false);

            Button button1 = view.findViewWithTag("button");
            button1.setOnClickListener(this);

            TextView textView = view.findViewWithTag("text");
            textView.setText(mParam1);

            return view;

        } catch (JSONException e) {
            e.printStackTrace();

            // default to R.layout.fragment_1
            return inflater.inflate(R.layout.fragment_1, container, false);
        }

    }
}
