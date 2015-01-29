package com.example.gabo.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;


public class DinamicMapFragment extends SupportMapFragment {
    private static final String LOG_TAG = "CustomMapFragment";

    public DinamicMapFragment() {
        super();

    }

    public static DinamicMapFragment newInstance() {
        DinamicMapFragment fragment = new DinamicMapFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater arg0, ViewGroup arg1, Bundle arg2) {
        View v = super.onCreateView(arg0, arg1, arg2);
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment instanceof OnMapReadyListener) {
            ((OnMapReadyListener) fragment).onMapReady();
        }
        return v;
    }



    /**
     * Listener interface to tell when the map is ready
     */
    public static interface OnMapReadyListener {

        void onMapReady();
    }
}
