package com.pam.abourassa.tp1.fragments;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.pam.abourassa.tp1.utils.UnitConversionHelper;

import java.util.Locale;

/**
 * Created by Anthony on 28/12/2016.
 */

/**
 * Fragment permettant de faire afficher les previsions meteo de la ville selectionnee. Les previsions
 * sont recuperees a l'aide d'une requete HTTP et sont affichees a l'ecran. La google map de la ville
 * est affichee avec un marqueur avec le nom de la ville et du pays. Une actionBar permet de voir le
 * nom de la ville et l'icone du temps en cours dans celle-ci.
 */
public class WeatherFragment extends Fragment {
    // Variable permettant de sauvegarder le id du pays selectionner ou enregistrer.
    public static final String ARG_CITY_ID = "com.pam.abourassa.tp1.fragments.ARG_CITY_ID";
    public static final String KEY_CITY_FORECAST = "com.pam.abourassa.tp1.fragments.KEY_CITY_FORECAST";
    public static final String KEY_TEMPERATURE_UNIT = "com.pam.abourassa.tp1.fragments.KEY_TEMPERATURE_UNIT";
    // Constantes permettant de setter les seekbars
    public static final float MAX_PRESSURE = 108.3f;
    public static final int MAX_HUMIDITY = 100;

    // Variables de weatherFragment
    private View view;
    private Context context;
    private GoogleMap gMap;
    private MapView mapView;
    private Forecast forecast;
    private TemperatureUnit temperatureUnit = TemperatureUnit.CELSIUS;
    private float celsiusTemperature;
    private TextView temperatureTextView;
    private TextView sunriseTextView;
    private TextView sunsetTexView;
    private SeekBar pressureSeekBar;
    private SeekBar humiditySeekBar;

    // Constructeur vide necessaire au fragment
    public WeatherFragment () {
    }

    /**
     * Methode permettant de recuperer le id de la ville selectionnee ou enregistree dans le
     * listview precedant.
     */
    public static WeatherFragment newInstance (int cityId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CITY_ID, cityId);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Methode utilisee lors de la creation du fragment. Lors de la rotation de l'appareil, elle
     * permet de recuperer l'objet forecast enregistrer ainsi que recuperer l'unite de temperature,
     * afin de savoir s'il faut afficher la temperature en celsius ou en fahrenheit.
     */
    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            forecast = savedInstanceState.getParcelable(KEY_CITY_FORECAST);
            temperatureUnit = TemperatureUnit.stringToTemperatureUnit(savedInstanceState.getString(KEY_TEMPERATURE_UNIT));
        }
    }

    /**
     * Methode permettant de creer la vue du fragment et de faire l'association entre les variables
     * du programmes et celles de la vue.
     */
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

    /**
     * Methode permettant de recuperer le id de la ville selectionnee. A l'aide d'une requete HTTP,
     * les prevision de la ville sont recuperees a l'aide d'un objet JSON. Ces informations sont
     * ensuite affichees a l'aide de la methode setCityForecast. Un custom layout est afficher dans
     * l'actionBar et la google map est recuperee et affichee a l'ecran.
     */
    @Override
    public void onActivityCreated (@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Contexte du fragment
        context = getActivity();
        int cityId;

        /*
         * Sert lors du premier lancement du weatherFragment afin de recuperer le id de la ville
         * selectionnee dans le fragment precedant. La methode getCityForecast permet d'aller recuperer
         * les previsions de la ville. La condition forecast == null sert lors de la rotation de
         * l'appareil, car il n'est pas necessaire de faire une requete HTTP car l'objet forecast
         * contient deja toutes ces informations.
         */
        if (getArguments() != null && forecast == null) {
            cityId = getArguments().getInt(ARG_CITY_ID);
            getCityForecast(cityId);
        } else if (getArguments() != null && forecast!= null){
            cityId = getArguments().getInt(ARG_CITY_ID);
            getCityForecast(cityId);
        }

        setWeatherActionBar();
        setCityForecast(view);
        getCityMap();
    }

    /**
     * Methode servant a affichee la temperature en celsius ou en fahrenheit lors du clic sur le
     * textview qui affiche la temperature.
     */
    private void changeTemperatureUnit () {
        if (temperatureUnit == TemperatureUnit.CELSIUS) {
            temperatureUnit = TemperatureUnit.FAHRENHEIT;
        } else if (temperatureUnit == TemperatureUnit.FAHRENHEIT) {
            temperatureUnit = TemperatureUnit.CELSIUS;
        }
    }

    /**
     * Methode permettant d'afficher la temperature dans un textview. Si l'unite de temperature est
     * le celsius, on recupere seulement l'information de l'objet forecast, puisque celui-ci founrnit
     * la temperature en celsius. Si la temperature est en fahrenheit, il faut alors fire un calcul
     * a l'aide de la variable qui stock la temperature en celsius.
     */
    private void getTemperature () {
        switch (temperatureUnit) {
            case CELSIUS:
                temperatureTextView.setText(String.format(Locale.CANADA_FRENCH, "%.1f °C", celsiusTemperature));
                break;
            case FAHRENHEIT:
                temperatureTextView.setText(String.format(Locale.CANADA_FRENCH, "%.1f °F",
                        UnitConversionHelper.celsisuToFahrenheit(celsiusTemperature)));
                break;
            default:
                temperatureTextView.setVisibility(View.GONE);
                break;
        }
    }

    /**
     * Methode permettant d'ecouter les clics sur le textview affichant la temperature. Lors d'un
     * clic, on change l'unite de temperature et on affiche ensuite la temperature dans la nouvelle
     * unite de temperature(celsius ou fahrenheit).
     */
    private View.OnClickListener temperatureOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick (View view) {
            changeTemperatureUnit();
            getTemperature();
        }
    };

    /**
     * Methode permettant d'empecher le clic sur les seekbars. Celles-ci affichent la pourcentage
     * de pression ou d'humidite, donc elles ne doivents pas etre cliquables.
     */
    private View.OnTouchListener seekBarOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch (View view, MotionEvent motionEvent) {
            return true;
        }
    };

    /**
     * Methode permettant de faire afficher un titre ainsi que l'icone de la meteo en cours de la
     * ville.
     */
    private void setWeatherActionBar () {
        String actionBarTitle = forecast.getCityName();
        CustomActionBar.setActionBar(getActivity(), actionBarTitle, ForecastProvider.getInstance()
                .getWeatherIcon(forecast.weather.get(0).getIcon()));
    }

    private String getApplicationLanguage() {
        SharedPreferences preferences = getActivity().getSharedPreferences(PreferencesFragment.PREFERENCES_SETTINGS,
                Context.MODE_PRIVATE);
        boolean appLanguage = preferences.getBoolean(PreferencesFragment.APP_LANGUAGE, false);
        String applicationLanguage;

        if (appLanguage) {
            applicationLanguage = Locale.FRENCH.getLanguage();
        }else {
            applicationLanguage = Locale.ENGLISH.getLanguage();
        }

        return applicationLanguage;
    }
    /**
     * Methode permettant de recuperer les previsions de la ville. La verification internet est
     * verifiee avant de faire la requete HTTP. S'il n'y a pas de connection, un message est afficher
     * a l'utilisateur, sinon on recupere les previsions de la ville.
     */
    private void getCityForecast (int cityId) {
        if (NetworkConnection.verifyNetworkConnection(context)) {
            forecast = ForecastProvider.getInstance().getJsonString(getActivity(), cityId, getApplicationLanguage());
        } else {
            Toast.makeText(context, R.string.warning_network_connection_google_map_toast, Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Methode permettant de faire afficher les previsions de la ville. On affiche notamment la
     * description du temps de la ville, la temperature, la pression (kPa), l'humidite (%) ainsi que
     * l'heure du lever et du coucher du soleil.
     */
    private void setCityForecast (View view) {
        if (forecast != null) {
            // Permet de recuperer la temperature du site web et de le faire afficher a l'ecran
            celsiusTemperature = Float.parseFloat(forecast.main.getTemperature());
            getTemperature();

            // Recuperation des informations provenant du site web
            float pressure = forecast.main.getPressure();
            float humidity = forecast.main.getHumidity();
            String description = forecast.weather.get(0).getDescription();
            // Mettre la premiere lettre en majuscule de la description
            String descriptionFirstLetterCapitalize = description.substring(0, 1).toUpperCase() + description.substring(1);

            // Permet d'afficher la decription du temps de la ville
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_description))
                    .setText(descriptionFirstLetterCapitalize);
            // Permet d'afficher la pression
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_pressure_unity))
                    .setText(String.format(Locale.CANADA_FRENCH, "%.1f kPa", UnitConversionHelper.hpaToKpa(pressure)));
            // Permet d'afficher l'humidite
            ((TextView) view.findViewById(R.id.fragment_weather_forecast_textview_humidity_unity))
                    .setText(String.format(Locale.CANADA_FRENCH, "%.0f %%", humidity));
            // Permet d'afficher le coucher et le lever du soleil
            sunriseTextView.append(" " + UnitConversionHelper.unixTimeToStandardTime(forecast.sys.getSunrise()));
            sunsetTexView.append("  " + UnitConversionHelper.unixTimeToStandardTime(forecast.sys.getSunset()));

            // Permet de changer la temperature en celsius ou en fahrenheit
            temperatureTextView.setOnClickListener(temperatureOnClickListener);
            // Permet de faire afficher la progression sur la seekbar de la pression
            pressureSeekBar.setMax((int) MAX_PRESSURE);
            // Permet de convertir la pression (hPa) en kPa
            pressureSeekBar.setProgress((int) UnitConversionHelper.hpaToKpa(pressure));
            // Permet de faire afficher la progression sur la seekbar de l'humidite
            humiditySeekBar.setMax(MAX_HUMIDITY);
            humiditySeekBar.setProgress((int) humidity);
            // Permet d'ecouter les clics sur les seekbars
            pressureSeekBar.setOnTouchListener(seekBarOnTouchListener);
            humiditySeekBar.setOnTouchListener(seekBarOnTouchListener);
        }
    }

    /**
     * Methode permettant de recuperer la google map de la ville. Un marqueur est afficher sur la
     * ville a l'aide de sa latitude et de sa longitude recuperees du site web.
     */
    private void getCityMap () {
        // Verification de la connection internet afin de recuperer la google map si elle est active
        if (NetworkConnection.verifyNetworkConnection(context)) {
            mapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady (GoogleMap googleMap) {
                    if (googleMap != null) {
                        gMap = googleMap;
                        LatLng currentCity = new LatLng(forecast.coord.getLatitude(), forecast.coord.getLongitude());
                        Country country = Provider.getInstance().findCountryByCountryCode(context, forecast.sys.getCountryCode());

                        // Permet d'afficher un titre, un message et le marqueur sur la ville.
                        gMap.addMarker(new MarkerOptions()
                                .title(String.format("%s, %s".toUpperCase(), forecast.getCityName(), country.getLocalName()))
                                .snippet(String.format("Welcome to %s, in %s !", forecast.getCityName().toUpperCase(),
                                        country.getName().toUpperCase()))
                                .position(currentCity)
                                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_marker)));
                        // Permet de faire un zoom pour mieux voir la ville
                        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentCity, 11));
                    }
                }
            });
        }
    }

    /**
     * Methode permettant de sauvegardee l'objet forecast, contenant les previsions de la ville,
     * ainsi que l'unite de temperature pour afficher la temperature de la ville dans la meme unite.
     * Cette methode sert lors de la rotation de l'appareil afin de conserver toutes les previsions
     * de l'objet forecast.
     */
    @Override
    public void onSaveInstanceState (Bundle outState) {
        if (outState != null) {
            outState.putParcelable(KEY_CITY_FORECAST, forecast);
            outState.putString(KEY_TEMPERATURE_UNIT, temperatureUnit.toString());
        }

        super.onSaveInstanceState(outState);
    }

    /**
     * Methodes permettant de gerer la google map selon le cycle de vie du fragment.
     */
    @Override
    public void onStart () {
        mapView.onStart();
        super.onStart();
    }

    @Override
    public void onResume () {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause () {
        mapView.onPause();
        super.onPause();
    }

    @Override
    public void onStop () {
        mapView.onStop();
        super.onStop();
    }

    @Override
    public void onDestroy () {
        mapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory () {
        mapView.onLowMemory();
        super.onLowMemory();
    }

}