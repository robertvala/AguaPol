package com.espol.aguapol.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.espol.aguapol.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;
import java.util.regex.MatchResult;

public class MapsFragment extends Fragment {
    private GoogleMap mMap;


    public MapsFragment(){
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap=googleMap;
            LatLng sydney = new LatLng(-2.147982, -79.966889);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Tramo 1"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,17));
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        }
    };

    public void changLatLon(LatLng newLocation,String marker){
        mMap.addMarker(new MarkerOptions().position(newLocation).title(marker));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newLocation,17));
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}