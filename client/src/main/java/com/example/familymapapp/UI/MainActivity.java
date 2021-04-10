package com.example.familymapapp.UI;

import android.app.ActivityManager;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.familymapapp.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;


public class MainActivity extends FragmentActivity implements LoginFragment.LoginCompleteListener {
    private boolean logged_in = false;
    private LoginFragment loginFragment;
    private GoogleMapsFragment mapFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fm = this.getSupportFragmentManager();
        ActivityManager am;
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.fragment_container);
        mapFragment = (GoogleMapsFragment) fm.findFragmentById(R.id.fragment_container);
        if ((loginFragment == null) || (logged_in = false)) {
            loginFragment = new LoginFragment(this);
            fm.beginTransaction().add(R.id.fragment_container, loginFragment).commit();
            logged_in = true;
        }
        else {
            beginMap(fm);
        }

    }

    private void beginMap(FragmentManager fm) {
        mapFragment = new GoogleMapsFragment();
        mapFragment.setLoginPerson(loginFragment.getLoginPerson());
        mapFragment.setAssociatedPeopleAndEvents(loginFragment.getPeopleAndEvents());
        mapFragment.setAuthToken(loginFragment.getAuth_token());
        fm.beginTransaction().add(R.id.fragment_container, mapFragment).commit();
    }

    public void checkRegisterInfo(View view) {
        loginFragment.checkRegisterInfo(view);
    }

    @Override
    public void completedLogin(boolean success) {
        if (success) {
            FragmentManager fm = this.getSupportFragmentManager();
            fm.beginTransaction().remove(loginFragment).commit();
            beginMap(fm);
        }
    }
}
