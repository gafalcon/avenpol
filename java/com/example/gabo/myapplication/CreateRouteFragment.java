package com.example.gabo.myapplication;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import databases.Avenpol_db;
import databases.PointDataSource;
import databases.Route;
import databases.RouteDataSource;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreateRouteFragment extends Fragment implements DinamicMapFragment.OnMapReadyListener{

    private DinamicMapFragment mMapFragment;
    private GoogleMap googleMap;
    PolylineOptions path;

    @InjectView(R.id.editTextAvailability)
    TextView view_availability;
    @InjectView(R.id.editTextCost) TextView view_cost;
    @InjectView(R.id.editTextDate) TextView view_date;
    @InjectView(R.id.editTextHour) TextView view_hour;

    private boolean firstMarkerSet = false;


    public CreateRouteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_route, container, false);
        mMapFragment = DinamicMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.create_route_map, mMapFragment).commit();
        return view;
    }


    @Override
    public void onMapReady() {
        googleMap = mMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-2.16, -79.9), 12));
        path = new PolylineOptions();
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                marker.hideInfoWindow();
            }

            @Override
            public void onMarkerDrag(Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                marker.setSnippet("Marker moved to: "+marker.getPosition().latitude+", "+marker.getPosition().longitude);
                marker.showInfoWindow();
            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                if(!firstMarkerSet) {
                    googleMap.addMarker(new MarkerOptions().position(latLng).title("Starting Point").draggable(false));
                    firstMarkerSet = true;
                }
                path.add(latLng).color(Color.BLUE);
                googleMap.addPolyline(path);

            }
        });
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!enabled)
            showDialog(googleMap);
        //getLocation(googleMap);

    }

    public void getLocation(GoogleMap map){
        LocationManager service = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        Criteria criteria = new Criteria();
        boolean enabled = service
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!enabled) {
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }
        // check if enabled and if not send user to the GSP settings
        // Better solution would be to display a dialog and suggesting to
        // go to the settings
        if (enabled) {
            String provider = service.getBestProvider(criteria, false);
            service.requestLocationUpdates(provider, 0, 0, locationListener);
            Location location = null;
            while (location == null)
                location = service.getLastKnownLocation(provider);
            map.addMarker(new MarkerOptions()
                                .position(new LatLng(location.getLatitude(), location.getLongitude()))
                                .title("Current Location"));

        }
    }
    public void showDialog(final GoogleMap googleMap){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("¿Desea activar el GPS?")
                .setTitle("El GPS no se encuentra habilitado");
        builder.setPositiveButton("Ok",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                //getLocation(googleMap);
            }
        });
        builder.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void saveRoute(View view){
        List<LatLng> latLngList = path.getPoints();
        if (latLngList.size() > 2) {
            ButterKnife.inject(getActivity());
            int availability = Integer.parseInt(view_availability.getText().toString());
            Double cost = Double.parseDouble(view_cost.getText().toString());
            String date = view_date.getText().toString();
            String hour = view_hour.getText().toString();
            Avenpol_db db = new Avenpol_db(getActivity());
            db.openDb();
            RouteDataSource routeDataSource = new RouteDataSource(db.getDatabase());
            Route route = routeDataSource.createRoute(availability, date + " " + hour, cost,1,1,1);
            int order = 1;
            PointDataSource pointDataSource = new PointDataSource(db.getDatabase());
            for (LatLng latLng : path.getPoints()) {
                pointDataSource.createPoint(latLng.latitude, latLng.longitude, order++, route.getId());
            }
            Toast.makeText(getActivity(), "Route was created", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getActivity(), "Tiene que definir un camino", Toast.LENGTH_SHORT).show();
        }
    }
}