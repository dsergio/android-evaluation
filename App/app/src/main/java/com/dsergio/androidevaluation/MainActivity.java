package com.dsergio.androidevaluation;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * MainActivity Class
 *
 */
public class MainActivity extends AppCompatActivity implements MyListener {

    public final String appName = "Android Evaluation";
    private int currentFragmentFileNameIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            swapFragment("fragment_1.json");
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("currentFragmentFileNameIndex", currentFragmentFileNameIndex);
        ArrayList<String> fragmentNames = new ArrayList<>();
        FragmentManager fm = getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
            FragmentManager.BackStackEntry b = fm.getBackStackEntryAt(i);
            String tag = b.getName();
            fragmentNames.add(tag);
        }
        outState.putStringArrayList("fragmentNames", fragmentNames);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentFragmentFileNameIndex = savedInstanceState.getInt("currentFragmentFileNameIndex");
        ArrayList<String> fragmentNames = savedInstanceState.getStringArrayList("fragmentNames");

        if (fragmentNames != null) {
            for (String s : fragmentNames) {
                createFragment(s, null, null);
            }
        }
    }

    @Override
    public void incrementCurrentFileNameIndex() {
        if (currentFragmentFileNameIndex < 3) {
            currentFragmentFileNameIndex++;
        } else {
            currentFragmentFileNameIndex = 1;
        }
        Log.d(appName, "currentFragmentFileNameIndex: " + currentFragmentFileNameIndex);
    }

    @Override
    public void decrementCurrentFileNameIndex() {
        if (currentFragmentFileNameIndex > 1) {
            currentFragmentFileNameIndex--;
        } else {
            currentFragmentFileNameIndex = 3;
        }
        Log.d(appName, "currentFragmentFileNameIndex: " + currentFragmentFileNameIndex);
    }


    @Override
    public void onBackPressed() {

        Log.d(appName, "Back pressed...  currentFragmentFileNameIndex: " + currentFragmentFileNameIndex);

        if (currentFragmentFileNameIndex == 1) {

            FragmentManager fm = getSupportFragmentManager();
            for(int i = 0; i < fm.getBackStackEntryCount(); i++) {
                fm.popBackStack();
            }
            super.onBackPressed();

        } else {
            getSupportFragmentManager().popBackStack();
            decrementCurrentFileNameIndex();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // This adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        int id = item.getItemId();

        if (id == R.id.action_about) {
            Toast.makeText(this,
                    appName,
                    Toast.LENGTH_SHORT)
                    .show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * Use this method to read the JSON config files to swap fragments
     *
     * @param filename the filename of the json fragment configuration file
     * @return the JSONObject parsed from the file
     */
    @Override
    public JSONObject parseJson(@NonNull String filename) {
        AssetManager assetManager = getResources().getAssets();

        String jsonStr;

        try {
            InputStream is = assetManager.open(filename);

            int size = is.available();
            byte[] buffer = new byte[size];
            int read = is.read(buffer);
            is.close();
            if (read != -1) {
                jsonStr = new String(buffer, StandardCharsets.UTF_8);
            } else {
                jsonStr = "";
            }

            is.close();
        } catch (IOException e) {
            Log.e(appName, "IOException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        JSONObject jsonObj;
        try {
            jsonObj = new JSONObject(jsonStr);
        } catch (final JSONException e) {
            Log.e(appName, "JSONException: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        return jsonObj;
    }


    /**
     *
     * Parse the JSON config file and create the fragment with animations
     *
     * @param fileName the json file with the fragment information
     */
    @Override
    public void swapFragment(@NonNull String fileName) {

        JSONObject jsonObject = parseJson(fileName);
        String fragmentAnimationEnter = null;
        String fragmentAnimationExit = null;
        String fragmentClass = null;
        try {
            fragmentClass = jsonObject.getString("fragmentClass");
            fragmentAnimationEnter = jsonObject.getString("fragmentAnimationEnter");
            fragmentAnimationExit = jsonObject.getString("fragmentAnimationExit");
        } catch (JSONException e) {
            Log.e(appName, "JSONException: " + e.getMessage());
            e.printStackTrace();
        }

        if (fragmentClass != null) {
            createFragment(fragmentClass, fragmentAnimationEnter, fragmentAnimationExit);
        }

    }

    /**
     *
     * Create fragments with given fragment class using Java Reflection and replace the FrameLayout with the Fragment
     *
     * @param fragmentClass the name of the fragment class
     * @param fragmentAnimationEnter the name of the enter animator. Can be null.
     * @param fragmentAnimationExit the name of the exit animator. Can be null
     */
    private void createFragment(@NonNull String fragmentClass, @Nullable String fragmentAnimationEnter, @Nullable String fragmentAnimationExit) {

        Fragment fragment;
        fragment = null;
        Class<?> c = null;
        try {
            c = Class.forName(fragmentClass);
            Log.d(appName, "CLASS NAME: " + c.getName());

            Class<?>[] cArg = new Class[1];
            cArg[0] = String.class;

            Method method = c.getMethod("newInstance", cArg);

            StringBuilder paramStringBuilder = new StringBuilder();
            FragmentManager fm = getSupportFragmentManager();
            int j = 0;
            for(int i = fm.getBackStackEntryCount() - 1; i >= 0 && j < 3; i--) {
                paramStringBuilder.append(fm.getBackStackEntryAt(i).getName()).append(" ");
                j++;
            }

            fragment = (Fragment) method.invoke(null, paramStringBuilder.toString());

        }  catch (ClassNotFoundException e) {
            Log.e(appName, "ClassNotFoundException: " + e.getMessage());
            e.printStackTrace();
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        String packageName = getPackageName();

        if (fragmentAnimationEnter != null && fragmentAnimationExit != null) {
            int fragmentAnimationEnterId = getResources().getIdentifier(fragmentAnimationEnter, "anim", packageName);
            int fragmentAnimationExitId = getResources().getIdentifier(fragmentAnimationExit, "anim", packageName);

            transaction.setCustomAnimations(fragmentAnimationEnterId, fragmentAnimationExitId);
        }

        if (fragment != null) {
            transaction.replace(R.id.fragment_display, fragment, c.getSimpleName());
        }
        if (c != null) {
            transaction.addToBackStack(c.getSimpleName());
        }

        transaction.commit();
    }
}