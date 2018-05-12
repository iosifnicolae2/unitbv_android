package ro.unitbv.cantina.activities;

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

import java.util.ArrayList;

import ro.unitbv.cantina.R;
import ro.unitbv.cantina.UnitbvApp;
import ro.unitbv.cantina.fragments.MenuFragment;

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

        fragment_manager = getSupportFragmentManager();

        setMenuView();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(id  == R.id.nav_menu) {
            setMenuView();
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

    @Override
    public void filter_category(ArrayList<String> ids) {
        if(ids!=null){
            if(menu_fragment!=null&&menu_fragment.isVisible())
                menu_fragment.filterByCat(ids);
        }

    }
}
