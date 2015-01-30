package com.example.gabo.myapplication;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import databases.Avenpol_db;
import databases.PointDataSource;
import databases.Route;
import databases.RouteDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMapFragment extends Fragment implements DinamicMapFragment.OnMapReadyListener, GoogleMap.InfoWindowAdapter {
    public final static String ROUTE_ID = "com.example.gabo.myapplication.ROUTE_ID";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DinamicMapFragment mMapFragment;
    private GoogleMap googleMap;
    private Hashtable<String, String> markers_date;
    private Hashtable<String, Double> markers_price;
    private Hashtable<String, Long> markers_id;

    @InjectView(R.id.info_window_date) TextView iw_date;
    @InjectView(R.id.info_window_price) TextView iw_price;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainMapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainMapFragment newInstance(String param1, String param2) {
        MainMapFragment fragment = new MainMapFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainMapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_map, container, false);
        mMapFragment = DinamicMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().replace(R.id.main_map_container, mMapFragment).commit();
        return view;
    }


    @Override
    public void onMapReady() {
        googleMap = mMapFragment.getMap();
        googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-2.16, -79.9), 12));
        googleMap.setInfoWindowAdapter(this);
        googleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                startActivity(new Intent(getActivity(),RouteInfoTabbedActivity.class)
                        .putExtra(ROUTE_ID,markers_id.get(marker.getId())));
            }
        });

        Avenpol_db db = new Avenpol_db(getActivity());
        db.openDb();
        RouteDataSource routeDataSource = new RouteDataSource(db.getDatabase());
        List<Route> routes = routeDataSource.getAllRoutesByUser(1);
        PointDataSource pointDataSource = new PointDataSource(db.getDatabase());
        int[] colors = { Color.CYAN, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.BLUE};
        int color = 0;
        markers_date = new Hashtable<>();
        markers_price = new Hashtable<>();
        markers_id = new Hashtable<>();
        for(Route route : routes){
            List<LatLng> points = pointDataSource.getAllPointsByRoute(route.getId());
            String title;
            float marker_color;
            if(route.getType() == 1){
                title = "Punto de Salida";
                marker_color = BitmapDescriptorFactory.HUE_AZURE;
            }else{
                title = "Punto de Llegada";
                marker_color = BitmapDescriptorFactory.HUE_GREEN;
            }

            final Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(points.get(0))
                    .title(title)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(marker_color)));
            markers_date.put(marker.getId(),route.getDate());
            markers_price.put(marker.getId(),route.getCost());
            markers_id.put(marker.getId(), route.getId());
            PolylineOptions path = new PolylineOptions();
            path.addAll(points);
            path.color(colors[color]);
            color = (color + 1) % 5;
            googleMap.addPolyline(path);
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
        // Getting view from the layout file info_window_layout
        View v = getActivity().getLayoutInflater().inflate(R.layout.info_window_layout, null);
        ButterKnife.inject(this,v);

        //TextView tvLat = (TextView) v.findViewById(R.id.tv_lat);

        // Getting reference to the TextView to set longitude
        //TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
        String date;
        Double price;
        // Setting the latitude
        if (marker.getId() != null && markers_date != null && markers_date.size() > 0) {
            date = markers_date.get(marker.getId());
            price = markers_price.get(marker.getId());
            iw_date.setText("Fecha:" + date);

            // Setting the longitude
            iw_price.setText("Precio:"+ price);
        }


        // Returning the view containing InfoWindow contents
        return v;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;

    }
}
