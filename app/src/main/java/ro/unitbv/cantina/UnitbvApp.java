package ro.unitbv.cantina;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.multidex.MultiDex;

import com.androidnetworking.AndroidNetworking;

import java.util.ArrayList;
import java.util.Calendar;

import ro.unitbv.cantina.database.DishDB;
import ro.unitbv.cantina.objects.Dish;

/**
 * Created by iosif on 10/15/16.
 */

public class UnitbvApp extends Application {
    public static final String API_DOMAIN = "https://unitbv.mailo.ml";
    public static final String SOCKET_ENDPOINT = "https://unitbv.mailo.ml";
    public static final String WAZE_POST_CLIENTS_API = "https://unitbv.mailo.ml/api/queue/waze_clients";
    public static final String FEEDBACK_EMAIL_ADDRESS = "cantina.memo.brasov@gmail.com";
    private ArrayList<Dish> menuData;
    private ArrayList<MenuCallback> menu_callbacks = new ArrayList<>();
    private ArrayList<String> filter_category = null;
    @Override
    public void onCreate() {
        super.onCreate();
        AndroidNetworking.initialize(getApplicationContext());
        clearCartToday();
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
    }

    public void filter_category(ArrayList<String> ids) {
        this.filter_category = ids;
        for(int i=0;i<menu_callbacks.size();i++)
            menu_callbacks.get(i).filter_category(ids);
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
