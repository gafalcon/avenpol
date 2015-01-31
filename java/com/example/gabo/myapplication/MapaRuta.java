package com.example.gabo.myapplication;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import databases.Avenpol_db;
import databases.PointDataSource;
import databases.Route;
import databases.RouteDataSource;


public class MapaRuta extends Fragment implements DinamicMapFragment.OnMapReadyListener{

    private DinamicMapFragment mMapFragment;
    private long route_id;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.actividad_mapa_ruta);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle args = getArguments();
        route_id = args.getLong(RouteInfoTabbedActivity.ROUTE_ID);
        View view =  inflater.inflate(R.layout.activity_mapa_ruta, container, false);
        mMapFragment = DinamicMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.single_route_map, mMapFragment).commit();
        return view;
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

    @Override
    public void onMapReady() {
        GoogleMap googleMap = mMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        Avenpol_db db = new Avenpol_db(getActivity());
        db.openDb();
        RouteDataSource routeDataSource = new RouteDataSource(db.getDatabase());
        Route route = routeDataSource.getRouteById(route_id);
        PointDataSource pointDataSource = new PointDataSource(db.getDatabase());
        List<LatLng> points = pointDataSource.getAllPointsByRoute(route.getId());
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(points.get(0), 12));
        String title;
        float marker_color;
        if(route.getType() == 1){
            title = "Punto de Salida";
            marker_color = BitmapDescriptorFactory.HUE_AZURE;
        }else{
            title = "Punto de Llegada";
            marker_color = BitmapDescriptorFactory.HUE_GREEN;
        }
        googleMap.addMarker(new MarkerOptions()
                .position(points.get(0))
                .title(title)
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(marker_color)));
        googleMap.addPolyline(new PolylineOptions().addAll(points).color(Color.YELLOW));
    }
}