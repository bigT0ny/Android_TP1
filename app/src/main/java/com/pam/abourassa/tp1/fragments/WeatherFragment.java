package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.pam.abourassa.tp1.R;
import com.pam.abourassa.tp1.actionBars.CustomActionBar;
import com.pam.abourassa.tp1.enums.TemperatureUnit;
import com.pam.abourassa.tp1.model.Objects.Country;
import com.pam.abourassa.tp1.model.Objects.Forecast;
import com.pam.abourassa.tp1.model.providers.ForecastProvider;
import com.pam.abourassa.tp1.model.providers.Provider;
import com.pam.abourassa.tp1.networkConnection.NetworkConnection;
import com.pam.abourassa.tp1.utils.ConversionHelper;

import java.util.Locale;

/**
 * Created by Anthony on 28/12/2016.
 */
public class WeatherFragment extends Fragment {
    // Variable permettant de sauvegarder le id du pays selectionner ou enregistrer.
    public static final String ARG_CITY_ID = "com.pam.abourassa.tp1.fragments.ARG_CITY_ID";
    public static final String KEY_CITY_FORECAST = "com.pam.abourassa.tp1.fragments.KEY_CITY_FORECAST";
    public static final String KEY_CELSIUS_TEMPERATURE = "com.pam.abourassa.tp1.fragments.KEY_CELSIUS_TEMPERATURE";
    private final float MAX_PRESSURE = 108.3f;
    private final int MAX_HUMIDITY = 100;

    private View view;
    private Context context;
    private GoogleMap gMap;
    private MapView mapView;
    private Forecast forecast;
    private TemperatureUnit temperatureUnit;
    private float currentTemperature =  1000;
    private TextView temperatureTextView;
    private TextView sunriseTextView;
    private TextView sunsetTexView;
    private SeekBar pressureSeekBar;
    private SeekBar humiditySeekBar;

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

        if (savedInstanceState != null) {
            forecast = savedInstanceState.getParcelable(KEY_CITY_FORECAST);
            currentTemperature = savedInstanceState.getFloat(KEY_CELSIUS_TEMPERATURE);
        }
    }

    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_weather, container, false);
        temperatureTextView = (TextView) view.findViewById(R.id.fragment_weather_forecast_textview_temperature);
        sunriseTextView = (TextView) view.findViewById(R.id.fragment_weather_forecast_textview_sunrise);
        sunsetTexView = (TextView) view.findViewById(R.id.fragment_weather_forecast_textview_sunset);
        pressureSeekBar = (SeekBar) view.findViewById(R.id.fragment_weather_forecast_seekbar_pressure);
        humiditySeekBar = (SeekBar) view.findViewById(R.id.fragment_weather_forecast_seekbar_humidity);
        mapView = (MapView) view.findViewById(R.id.fragment_weather_map_mapview_city_map);
        mapView.onCreate(savedInstanceState);

        return view;
    }

    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity();
        setHasOptionsMenu(true);
        temperatureUnit = TemperatureUnit.CELSIUS;

        if (getArguments() != null && forecast == null) {
            int cityId = getArguments().getInt(ARG_CITY_ID);
            getCityForecast(cityId);
        }

        setCityForecast(view);
        setWeatherActionBar();
        getCityMap();
    }

    private View.OnClickListener temperatureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            changeTemperatureUnit();

            switch(temperatureUnit) {
                case CELSIUS:
                    temperatureTextView.setText(String.format(Locale.CANADA_FRENCH, "%.1f °C", currentTemperature));
                    break;
                case FAHRENHEIT:
                    currentTemperature = ConversionHelper.celsisuToFahrenheit(currentTemperature);
                    temperatureTextView.setText(String.format(Locale.CANADA_FRENCH, "%.1f °F", currentTemperature));
                    break;
                default:
                    temperatureTextView.setText(temperatureTextView.toString());
                    break;
            }
        }
    };

    private View.OnTouchListener seekBarOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View view, MotionEvent motionEvent) {
            return true;
        }
    };

    private void setWeatherActionBar() {
        String actionBarTitle = forecast.getCityName();
        CustomActionBar.setActionBar(getActivity(), actionBarTitle, ForecastProvider.getInstance()
                .getWeatherIcon(forecast.weather.get(0).getIcon()));
    }

    private void changeTemperatureUnit() {
        if (temperatureUnit == TemperatureUnit.CELSIUS) {
            temperatureUnit = TemperatureUnit.FAHRENHEIT;
        }else {
            temperatureUnit = TemperatureUnit.CELSIUS;
        }
    }

    private void getCityForecast(int cityId) {
        if (NetworkConnection.verifyNetworkConnection(context)) {
            forecast = ForecastProvider.getInstance().getJsonString(cityId, getActivity());
        }else {
            Toast.makeText(context, R.string.warning_network_connection_google_map_toast, Toast.LENGTH_LONG).show();
        }
    }

    private void setCityForecast(View view) {
        if (forecast != null) {
            if (currentTemperature == 1000) {
                currentTemperature = Float.parseFloat(forecast.main.getTemperature());
            }
            float pressure = forecast.main.getPressure();
            float humidity = forecast.main.getHumidity();
            String description = forecast.weather.get(0).getDescription();
            String descriptionFirstLetterCapitalize = description.substring(0, 1).toUpperCase() + description.substring(1);

            temperatureTextView.setText(String.format(Locale.CANADA_FRENCH, "%.1f °C", currentTemperature));
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_description))
                    .setText(descriptionFirstLetterCapitalize);
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_pressure_unity))
                    .setText(String.format(Locale.CANADA_FRENCH, "%.1f kPa", ConversionHelper.hpaToKpa(pressure)));
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_humidity_unity))
                    .setText(String.format(Locale.CANADA_FRENCH, "%.0f %%", humidity));
            sunriseTextView.append(" " + ConversionHelper.unixTimeToStandardTime(forecast.sys.getSunrise()));
            sunsetTexView.append("  " + ConversionHelper.unixTimeToStandardTime(forecast.sys.getSunset()));

            temperatureTextView.setOnClickListener(temperatureOnClickListener);
            pressureSeekBar.setMax((int) MAX_PRESSURE);
            pressureSeekBar.setProgress((int) ConversionHelper.hpaToKpa(pressure));
            humiditySeekBar.setMax(MAX_HUMIDITY);
            humiditySeekBar.setProgress((int) humidity);
            pressureSeekBar.setOnTouchListener(seekBarOnTouchListener);
            humiditySeekBar.setOnTouchListener(seekBarOnTouchListener);
        }
    }

    private void getCityMap() {
        if (NetworkConnection.verifyNetworkConnection(context)) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady (GoogleMap googleMap) {
                    if (googleMap != null) {
                        gMap = googleMap;
                        LatLng currentCity = new LatLng(forecast.coord.getLatitude(), forecast.coord.getLongitude());
                        Country country = Provider.getInstance().findCountryByCountryCode(context, forecast.sys.getCountryCode());

                        gMap.addMarker(new MarkerOptions()
                                .title(String.format("%s, %s".toUpperCase(), forecast.getCityName(), country.getLocalName()))
                                .snippet(String.format("Welcome to %s, in %s !", forecast.getCityName().toUpperCase(),
                                        country.getName().toUpperCase()))
                                .position(currentCity)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCity, 11));
                    }
                }
            });
        }
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        if (outState != null) {
            outState.putParcelable(KEY_CITY_FORECAST, forecast);
            outState.putFloat(KEY_CELSIUS_TEMPERATURE, currentTemperature);
        }

        super.onSaveInstanceState(outState);
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
    public void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        mapView.onLowMemory();
        super.onLowMemory();
    }

}
