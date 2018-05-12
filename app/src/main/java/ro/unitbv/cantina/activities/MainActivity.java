package ro.unitbv.cantina.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import ro.unitbv.cantina.R;
import ro.unitbv.cantina.UnitbvApp;
import ro.unitbv.cantina.fragments.MenuFragment;
import ro.unitbv.cantina.fragments.OrarFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, UnitbvApp.MenuCallback {

    private FragmentManager fragment_manager;
    private Toolbar toolbar_a;
    private MenuFragment menu_fragment;
    private UnitbvApp app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (UnitbvApp) getApplication();

        // Disable Schedule sync
        // app.check_schedule();

        setContentView(R.layout.activity_main);
        toolbar_a = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar_a);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar_a, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        fragment_manager = getSupportFragmentManager();
        // Disable Orar View
        // setOrarView();
        setMenuView();

//        updateWeather();
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.addMenuCallback(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        app.removeMenuCallback(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_orar) {
            setOrarView();
        }else if(id  == R.id.nav_menu){
            setMenuView();
        }else if(id== R.id.nav_settings){
            Intent i = new Intent(this,SettingsActivity.class);
            startActivity(i);
            return true;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void setMenuView() {

        menu_fragment = new MenuFragment();
        FragmentTransaction ft = fragment_manager.beginTransaction();
        ft.replace(R.id.home_fragment_container, menu_fragment);
        ft.commit();

    }

    private void updateWeather(){

        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.wunderground.com/api/0b4553f4ca1dd2ff/forecast/q/RO/Brasov.json", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    JSONObject forecast = response.getJSONObject("forecast");
                    JSONObject wh = forecast.getJSONObject("simpleforecast").getJSONArray("forecastday").getJSONObject(0);

                    String temp  = wh.getJSONObject("low").getInt("celsius")+" - "+wh.getJSONObject("high").getInt("celsius")+" °C";

                    //TextView temperature = (TextView) findViewById(R.id.weather_celsius);
                    //temperature.setText(temp);

                   /* ImageView photo = (ImageView) findViewById(R.id.weather_photo);

                    JSONObject txt_wf = forecast.getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0);

                    String photo_url  = txt_wf.getString("icon_url");

                    Picasso.with(MainActivity.this)
                            .load(photo_url)
                            .placeholder(android.R.color.white)
                            .error(android.R.color.white)
                            .into(photo);
*/
                    JSONObject txt_wf = forecast.getJSONObject("txt_forecast").getJSONArray("forecastday").getJSONObject(0);

                   // TextView desc = (TextView) findViewById(R.id.weather_description);
                   // desc.setText(txt_wf.getString("fcttext_metric"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setOrarView() {

        OrarFragment fragment = new OrarFragment();
        FragmentTransaction ft = fragment_manager.beginTransaction();
        ft.replace(R.id.home_fragment_container, fragment);
        ft.commit();

    }

    public void setNoShadowToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar_a.setElevation(0);
        }
    }

    public void setShadowToolbar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar_a.setElevation(1);
        }
    }

    @Override
    public void filter_category(ArrayList<String> ids) {
        if(ids!=null){
            if(menu_fragment!=null&&menu_fragment.isVisible())
                menu_fragment.filterByCat(ids);
        }

    }
}
