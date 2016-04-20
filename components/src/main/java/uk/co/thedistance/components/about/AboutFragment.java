/*
 * Copyright (c) The Distance Agency Ltd 2016.
 */

package uk.co.thedistance.components.about;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.util.ArrayList;

import uk.co.thedistance.components.R;

/**
 * Use this to add an about fragment to your activity, or call the {@link #getAdapter(Context, String)} to use in your own ListView
 * Created by pharris on 17/02/16.
 */
public class AboutFragment extends android.support.v4.app.ListFragment {

    private static final String APP_NAME = "appName";

    public static AboutFragment newInstance(String appName){
        AboutFragment frag = new AboutFragment();

        Bundle args = new Bundle(1);
        args.putString(APP_NAME, appName);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        String appName = getArguments().getString(APP_NAME);

        setListAdapter(getAdapter(getActivity(), appName));

        return view;
    }

    /**
     * Get an adapter containing the libraries used in this app.
     * <br />
     * Use the R.array.libraries libraries string-array to list the libraries used in your app
     * @param context
     * @param appName
     * @return
     */
    public static ListAdapter getAdapter(Context context, String appName){
        ArrayList<String> aboutItems = new ArrayList<>();

        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            aboutItems.add(String.format("%s version %s", appName, pInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        String[] libraries = context.getResources().getStringArray(R.array.libraries);
        if(null != libraries && libraries.length > 0) {
            aboutItems.add("Third Party Libraries");
            for (String s : libraries) {
                aboutItems.add(" • " + s);
            }
        }

        return new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, aboutItems);
    }
}
