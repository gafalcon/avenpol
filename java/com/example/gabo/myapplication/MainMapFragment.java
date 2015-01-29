package com.example.gabo.myapplication;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;
import java.util.Random;

import databases.Avenpol_db;
import databases.PointDataSource;
import databases.Route;
import databases.RouteDataSource;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainMapFragment extends Fragment implements DinamicMapFragment.OnMapReadyListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DinamicMapFragment mMapFragment;
    private GoogleMap googleMap;


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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-2.16,-79.9), 12));
        Avenpol_db db = new Avenpol_db(getActivity());
        db.openDb();
        RouteDataSource routeDataSource = new RouteDataSource(db.getDatabase());
        List<Route> routes = routeDataSource.getAllRoutesByUser(1);
        PointDataSource pointDataSource = new PointDataSource(db.getDatabase());
        int[] colors = { Color.CYAN, Color.GREEN, Color.MAGENTA, Color.GRAY, Color.BLUE};
        int color = 0;
        for(Route route : routes){
            List<LatLng> points = pointDataSource.getAllPointsByRoute(route.getId());
            String title;
            float marker_color;
            if(route.getType() == 1){
                title = "Starting Point";
                marker_color = BitmapDescriptorFactory.HUE_CYAN;
            }else{
                title = "Destination";
                marker_color = BitmapDescriptorFactory.HUE_AZURE;
            }

            googleMap.addMarker(new MarkerOptions()
                    .position(points.get(0))
                    .title(title)
                    .draggable(false)
                    .icon(BitmapDescriptorFactory.defaultMarker(marker_color)));
            PolylineOptions path = new PolylineOptions();
            path.addAll(points);
            path.color(colors[color]);
            color = (color + 1) % 5;
            googleMap.addPolyline(path);
        }
    }
}
