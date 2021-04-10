package com.example.familymapapp.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.familymapapp.Model.AssociatedPeopleAndEvents;
import com.example.familymapapp.Model.EventsList;
import com.example.familymapapp.Model.Person;
import com.example.familymapapp.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GoogleMapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private AssociatedPeopleAndEvents peopleAndEvents;
    private Person person;

    private Person loginPerson;
    private String auth_token;


    public void setLoginPerson(Person person) {
        loginPerson = person;
    }

    public void setAuthToken(String authentication) {
        auth_token = authentication;
    }

    public void setAssociatedPeopleAndEvents (AssociatedPeopleAndEvents givenPeopleAndEvents) {
        peopleAndEvents = givenPeopleAndEvents;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_maps, container, false);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
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

        EventsList eventsList = this.peopleAndEvents.getEvents();
        for (int i = 0; i < eventsList.getData().length; ++i) {
            LatLng city = new LatLng(eventsList.getData()[i].getLatitude(), eventsList.getData()[i].getLongitude());
            mMap.addMarker(new MarkerOptions().position(city));
        }
        mMap.getCameraPosition();
        return;
    }
}
