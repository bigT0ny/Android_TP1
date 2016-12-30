package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.model.Objects.City;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.Provider;
import com.pam.abourassa.tp1.networkConnection.NetworkConnection;

/**
 * Created by Anthony on 28/12/2016.
 */
public class WeatherFragment extends Fragment {
    // Variable permettant de sauvegarder le id du pays selectionner ou enregistrer.
    public static final String ARG_CITY_ID = "ca.cs.equipe3.forecast.fragments.ARG_CITY_ID";

    private GoogleMap gMap;
    private MapView mapView;
    private Context context;
    private City city;

    public WeatherFragment () {
    }

    /**
     * @param cityId Parameter 1 : selected city ID.
     * @return an instance of WeatherFragment
     */
    public static WeatherFragment newInstance (int cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();

        if (savedInstanceState != null) {

        }

        if (getArguments() != null) {
            int cityId = getArguments().getInt(ARG_CITY_ID);
            city = Provider.getInstance().findCityById(context, cityId);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        mapView = (MapView) view.findViewById(R.id.fragment_weather_map_mapview_city_map);
        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (NetworkConnection.verifyNetworkConnection(context)) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady (GoogleMap googleMap) {
                    if (googleMap != null) {
                        gMap = googleMap;
                        LatLng currentCity = new LatLng(city.getLatitude(), city.getLongitude());
                        Country country = Provider.getInstance().findCountryByCountryCode(context, city.getCountryCode());

                        gMap.addMarker(new MarkerOptions()
                                .title(String.format("%s, %s".toUpperCase(), city.getName(), country.getLocalName()))
                                .snippet(String.format("Welcome to %s, in %s !", city.getName().toUpperCase(),
                                        country.getName().toUpperCase()))
                                .position(currentCity)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));

                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCity, 13));
                    }
                }
            });
        }else {
            Toast.makeText(context, R.string.warning_network_connection_google_map_toast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStart() {
        mapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

}
