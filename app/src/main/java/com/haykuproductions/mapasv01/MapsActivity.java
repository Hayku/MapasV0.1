package com.haykuproductions.mapasv01;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private int tipomapa=0;
    private GoogleMap mMap;
    private PolylineOptions popciones;
    private ArrayList<LatLng> rutas;
    private Polyline linea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        rutas = new ArrayList<>();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng posicion) {
                mMap.addMarker(new MarkerOptions().position(posicion).title("" + posicion));
                rutas.add(posicion);
                if (rutas.size() >= 3) {
                    creaRuta(rutas);
                }
            }
        });
    }
    public void bt_tipomapa(View v) {
        if(mMap != null){
            tipomapa = (tipomapa + 1) % 4;
            switch (tipomapa) {
                case 0:
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                case 1:
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case 2:
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case 3:
                    mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;

            }
        }

    }
    public void creaRuta(ArrayList<LatLng> marcadores) {

        popciones = new PolylineOptions();
        popciones.color(Color.RED);

        for(int i = 0 ; i < marcadores.size(); i++){
            popciones.add(marcadores.get(i));
        }

        linea = mMap.addPolyline(popciones);
    }
    public void limpiaRuta(View v) {
        //linea.remove();
        rutas.clear();
        mMap.clear();

    }

    public void streetView(View v) {
        if(rutas.size()!=0) {
            Intent streetView = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse("google.streetview:cbll=" + rutas.get(rutas.size() - 1).latitude + "," + rutas.get(rutas.size() - 1).longitude + "&cbp=1,99.56,,1,-5.27&mz=21"));
            startActivity(streetView);
        }
        else{
            Toast.makeText(this, "Inserte una dirección para ver street view", Toast.LENGTH_SHORT).show();
        }
    }
}
