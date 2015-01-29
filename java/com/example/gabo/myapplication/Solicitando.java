package com.example.gabo.myapplication;


import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.MenuItem;


public class Solicitando extends FragmentActivity
{

    private FragmentTabHost tabHost;
    private long route_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solicitando);
        route_id = getIntent().getLongExtra(MainMapFragment.ROUTE_ID,0);
        tabHost= (FragmentTabHost) findViewById(android.R.id.tabhost);

        tabHost.setup(this,
                getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("Perfil "+route_id),
                RouteProfileFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab2").setIndicator("Mapa de Ruta"),
                MapaRuta.class, null);
        tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator("Comentar"),
                Comentar.class, null);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}