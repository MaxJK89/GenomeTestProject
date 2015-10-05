package quant.cann.genometestproject.fragments;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import quant.cann.genometestproject.R;

public class DetailPlaceMapFragment extends DialogFragment implements OnMapReadyCallback {
    private static final String LAT = "param1";
    private static final String LNG = "param2";

    private static double lati;
    private static double longi;
    private static String placeName;
    MapView mMapView;
    private GoogleMap map;
    private GoogleMap googleMap;


    public DetailPlaceMapFragment() {
        // Required empty public constructor
    }

    public static DetailPlaceMapFragment newInstance(double lat, double lng, String placeName) {
        DetailPlaceMapFragment fragment = new DetailPlaceMapFragment();
        Bundle args = new Bundle();
        args.putDouble(LAT, lat);
        args.putDouble(LNG, lng);
        args.putString("PLACENAME", placeName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            lati = getArguments().getDouble(LAT);
            longi = getArguments().getDouble(LNG);
            placeName = getArguments().getString("PLACENAME");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_detail_place_map, container, false);
        getDialog().setTitle(placeName);

        mMapView = (MapView) v.findViewById(R.id.detailMapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        googleMap = mMapView.getMap();
        mMapView.getMapAsync(this);

        return v;
    }

    @Override
    public void onMapReady(final GoogleMap readyMap) {
        this.map = readyMap;
        map.getUiSettings().setZoomControlsEnabled(false);

        // Here is your "functional My Location button"
        // thnx Google
//        map.setMyLocationEnabled(true);
        // but we will use our own FAB to do this to make it look cooler


        // create marker
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(lati, longi));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lati, longi)).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }


    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
