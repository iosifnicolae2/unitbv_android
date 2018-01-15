package ro.unitbv.cantina;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;

import cz.msebera.android.httpclient.Header;
import ro.unitbv.cantina.database.DishDB;
import ro.unitbv.cantina.database.OrarRowDB;
import ro.unitbv.cantina.objects.Dish;
import ro.unitbv.cantina.objects.OrarRow;

/**
 * Created by iosif on 10/15/16.
 */

public class UnitbvApp extends Application {
    private ScheduleUpdatedCallback scheduleUpdatedCallback;
    private ArrayList<Dish> menuData;
    private ArrayList<MenuCallback> menu_callbacks = new ArrayList<>();
    private ArrayList<String> filter_category = null;

    public static final String API_DOMAIN = "https://unitbv.mailo.ml";

    @Override
    public void onCreate() {
        super.onCreate();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {

                clearCartToday();
                return null;
            }
        }.execute();

    }

    private void clearCartToday() {

        SharedPreferences sharedPref = getSharedPreferences(
                getString(R.string.cart_preference_file_key), Context.MODE_PRIVATE);

        Calendar c = Calendar.getInstance();
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);

        String today = day+month+year+"";
        String last_cleaning = sharedPref.getString("clear_cart","");
        if(!today.equals(last_cleaning)){
            //cand se schimba ziua golim cosul de cumparaturi
            DishDB db = new DishDB(getBaseContext());
            db.clearTable();
            sharedPref.edit().putString("clear_cart",today).apply();
        }
    }



    private void get_schedule() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://unitbv.mailo.ml/static/orar_csv/orar.csv", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                parseCsv(response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });

    }


    private ArrayList<OrarRow> populateOrarDay(String an, String spec, String grupa, int j, String[] interval_values, int day, boolean saptamana_para) {

        ArrayList<OrarRow> orarValues = new ArrayList<>();

        for(int i = j,l=interval_values.length;i<j+7&&i<l;i++){
            if((interval_values[i]).length()>0)
                orarValues.add(new OrarRow(0, an,spec,grupa,interval_values[3],i-j,day,interval_values[i],saptamana_para));
        }

        return orarValues;
    }



    private void parseCsv(byte[] response) {


      //  ArrayList<Interval> intervale_orare = new ArrayList<>();
        ArrayList<OrarRow> orar = new ArrayList<>();

        InputStream is = new ByteArrayInputStream(response);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String line;
            int line_nr = 0;

            String an = "",spec="",grupa="";

            while ((line = reader.readLine()) != null) {
                line_nr++;

                if(line_nr>3){
                    String[] interval_values = line.split("\\|");

                        if(interval_values.length<4)
                            break;

                    if((interval_values[0]).length()>0)
                        an = interval_values[0];

                    if((interval_values[1]).length()>0)
                        spec = interval_values[1];

                    if((interval_values[2]).length()>0)
                        grupa = interval_values[2];

                    boolean saptamana_para = line_nr%2==1;

                    orar.addAll(populateOrarDay(an,spec,grupa,4,interval_values,1,saptamana_para));
                    orar.addAll(populateOrarDay(an,spec,grupa,11,interval_values,2,saptamana_para));
                    orar.addAll(populateOrarDay(an,spec,grupa,18,interval_values,3,saptamana_para));
                    orar.addAll(populateOrarDay(an,spec,grupa,25,interval_values,4,saptamana_para));
                    orar.addAll(populateOrarDay(an,spec,grupa,32,interval_values,5,saptamana_para));


                }/*else
                if(line_nr==3){
                  String[] interval_orar = line.split("\\|");
                    for(int j=4;j<11;j++){
                        intervale_orare.add(new Interval(interval_orar[j]));
                    }
                }*/
                // do something with "data" and "value"
            }
        }
        catch (IOException ex) {
            // handle exception
            Log.w("IOException",ex);
            Toast.makeText(this,getString(R.string.cannot_get_schedule),Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                // handle exception
                Log.w("IOException",e);
            }
        }

        saveToDB(orar);

        if(scheduleUpdatedCallback!=null)
        scheduleUpdatedCallback.schedule_updated();
    }

    private void saveToDB(ArrayList<OrarRow> orar) {

        OrarRowDB db = new OrarRowDB(getApplicationContext());
        db.clearTable();
        db.insertMoreRows(orar);

      //  ArrayList<OrarRow> llll = db.getSchedule();
      //  Log.w("jkljl",llll.toString());


    }


    public void check_schedule() {

        OrarRowDB db = new OrarRowDB(getApplicationContext());
        if(!db.checkSchedule())
            get_schedule();

    }



    public void setScheduleUpdatedCallback(ScheduleUpdatedCallback scheduleUpdatedCallback) {
        this.scheduleUpdatedCallback = scheduleUpdatedCallback;
    }

    public void setMenuData(ArrayList<Dish> menuData) {
        this.menuData = menuData;
    }

    public ArrayList<Dish> getMenuData() {
        return menuData;
    }

    public Dish getMenuDish(int position) {
        if(position>=0&&menuData!=null&&menuData.size()>position)
            return menuData.get(position);
        return null;
    }

    public void removeMenuCallback(MenuCallback c){
        this.menu_callbacks.remove(c);
    }

    public void addMenuCallback(MenuCallback c){
        c.filter_category(filter_category);
       // this.menu_callbacks.add(c);
    }

    public void filter_category(ArrayList<String> ids) {
        this.filter_category = ids;
        for(int i=0;i<menu_callbacks.size();i++)
            menu_callbacks.get(i).filter_category(ids);
    }

    public interface ScheduleUpdatedCallback {
        void schedule_updated();
    }

    public interface MenuCallback{
        void filter_category(ArrayList<String> id);
    }
    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }
}
