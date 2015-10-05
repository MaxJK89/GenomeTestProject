package quant.cann.genometestproject.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.Global;
import quant.cann.genometestproject.R;

public class MapFragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    @Bind(R.id.my_location_fab)
    FloatingActionButton myLocationFAB;
    MapView mMapView;
    private GoogleMap map;
    private Global app;
    private GoogleMap googleMap;


    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = ((Global) getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);

        myLocationFAB.setOnClickListener(this);

        mMapView = (MapView) v.findViewById(R.id.mapView);
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
                new LatLng(Global.getLatitude(), Global.getLongitude()));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Global.getLatitude(), Global.getLongitude())).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    private void goToMyLocation() {
        MarkerOptions marker = new MarkerOptions().position(
                new LatLng(Global.getLatitude(), Global.getLongitude()));

        // Changing marker icon
        marker.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ROSE));

        // adding marker
        googleMap.addMarker(marker);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(Global.getLatitude(), Global.getLongitude())).zoom(12).build();
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
    }

    @Override
    public void onClick(View v) {
        if (v instanceof FloatingActionButton) {
            goToMyLocation();
        }
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
