package com.example.gabo.myapplication;


import android.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.MapFragment;

public class Solicitando extends Fragment
{

    private FragmentTabHost tabHost;

   /* @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitando);
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Perfil"),
                Perfil.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mapa de Ruta"),
                MapaRuta.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Comentar"),
                Comentar.class, null);
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_solicitando, container, false);
        tabHost= (FragmentTabHost) getActivity().findViewById(android.R.id.tabhost);

        /*tabHost.setup(getActivity(),
                getFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Perfil"),
                Perfil.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mapa de Ruta"),
                MapaRuta.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Comentar"),
                Comentar.class, null);
        getActivity().setTitle("Perfil de la Ruta");/*/
        return rootView;
    }


}