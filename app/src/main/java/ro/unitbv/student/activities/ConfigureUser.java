package ro.unitbv.student.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import ro.unitbv.student.R;
import ro.unitbv.student.database.OrarRowDB;

/**
 * Created by iosif on 10/22/16.
 */
public class ConfigureUser extends AppCompatActivity{

    public static final String PREFERENCE_USER_SETTINGS = "prefs_user";
    private String year = "";
    private String spec = "";
    private String grupa = "";
    private String sgr = "";
    private Spinner grupa_spinner;
    private OrarRowDB db;
    private SharedPreferences sharedPref;
    private ArrayList<String> grupe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_user_activity);
        setTitle("");
        if(getActionBar()!=null){
           // getActionBar().setHomeButtonEnabled(true);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }

        sharedPref = getSharedPreferences(PREFERENCE_USER_SETTINGS, Context.MODE_PRIVATE);

        year = sharedPref.getString("year","");
        spec = sharedPref.getString("spec","");
        grupa = sharedPref.getString("grupa","");
        sgr = sharedPref.getString("sgr","");



        db = new OrarRowDB(this);
        final ArrayList<String> years = db.getOrarYears();
        final ArrayList<String> specs = db.getOrarSpecs();
        final ArrayList<String> sgrs = db.getOrarSGR();

        grupe = db.getOrarGrupaTotal();


        Spinner year_spinner = (Spinner) findViewById(R.id.year_spinner);
        Spinner spec_spinner = (Spinner) findViewById(R.id.spec_spinner);
        grupa_spinner = (Spinner) findViewById(R.id.grupa_spinner);
        Spinner sgr_spinner = (Spinner) findViewById(R.id.sgr_spinner);

        year_spinner.setAdapter(new ArrayAdapter<>(ConfigureUser.this, android.R.layout.simple_list_item_1, years));
        spec_spinner.setAdapter(new ArrayAdapter<>(ConfigureUser.this, android.R.layout.simple_list_item_1, specs));
        grupa_spinner.setAdapter(new ArrayAdapter<>(ConfigureUser.this, android.R.layout.simple_list_item_1, grupe));
        sgr_spinner.setAdapter(new ArrayAdapter<>(ConfigureUser.this, android.R.layout.simple_list_item_1, sgrs));


        year_spinner.setSelection(search_position(years,year));
        spec_spinner.setSelection(search_position(specs,spec));
        grupa_spinner.setSelection(search_position(grupe,grupa));
        sgr_spinner.setSelection(search_position(sgrs,sgr));


        year_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                year = years.get(position);
                update_grupa_adapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spec_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                spec = specs.get(position);
                update_grupa_adapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        grupa_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                grupa = grupe.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        sgr_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                sgr = sgrs.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void save_user_settings() {
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("year",year);
        editor.putString("spec",spec);
        editor.putString("grupa",grupa);
        editor.putString("sgr",sgr);

        editor.apply();
    }


    private int search_position(ArrayList<String> search,String year) {

        for(int i=0,l=search.size();i<l;i++)
            if(search.get(i).equals(year)) return i;

        return 0;
    }

    private void update_grupa_adapter() {

        if(spec.length()==0 || year.length()==0) return;

        grupe = db.getOrarGrupa(year,spec);

        grupa_spinner.setAdapter(new ArrayAdapter<String>(ConfigureUser.this, android.R.layout.simple_list_item_1, grupe));
        grupa_spinner.setSelection(search_position(grupe,grupa));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_grupa, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            save_user_settings();
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
