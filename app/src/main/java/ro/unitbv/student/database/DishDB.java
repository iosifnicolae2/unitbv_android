package ro.unitbv.student.database;

/**
 * Created by iosif on 11/30/16.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;

import java.util.ArrayList;

import ro.unitbv.student.objects.Dish;



/**
 * Created by iosif on 7/23/16.
 */

public class DishDB extends DatabaseHelper {

    /*
        private String an;
        private String spec;
        private String grupa;
        private String sgr;
    
    
    
        private int interval;
        private int day;
        private String value;
    
     */
    private static final String TABLE_NAME = "dishs_table_v1";
    private static final String DISH_ID = "id";
    private static final String DISH_PRICE = "price";
    private static final String DISH_NAME = "name";
    private static final String DISH_SHORT_DESCRIPTION = "short_description";
    private static final String DISH_DESCRIPTION = "description";
    private static final String DISH_QUANTITY = "quantity";
    private static final String DISH_PICTURE_URL = "picture_url";
    private static final String DISH_CATEGORIES = "categories";

    private static final String[] FIELDS_NAME = new String[]{
            DISH_ID,
            DISH_PRICE,
            DISH_NAME,
            DISH_SHORT_DESCRIPTION,
            DISH_DESCRIPTION,
            DISH_QUANTITY,
            DISH_PICTURE_URL,
            DISH_CATEGORIES
    };
    private static final String[] FIELDS_TYPE = new String[]{
            "INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ",
            "TEXT",
            "TEXT",
            "TEXT",
            "TEXT",
            "TEXT",
            "TEXT",
            "TEXT",

    };
    private final Context context;

    public DishDB(Context context) {
        super(context, TABLE_NAME, FIELDS_NAME, FIELDS_TYPE);
        this.context = context;
    }

    public void add(Dish u) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        //   values.put(DISH_ID, u.getId());
        values.put(DISH_PRICE, u.getPrice());
        values.put(DISH_NAME, u.getName());
        values.put(DISH_SHORT_DESCRIPTION, u.getShort_description());
        values.put(DISH_DESCRIPTION, u.getDescription());


        values.put(DISH_QUANTITY,u.getQuantity());
        values.put(DISH_PICTURE_URL, u.getPicture_url());

        JSONArray json = new JSONArray();
        if(u.getCategories()!=null){
            for(int i=0;i<u.getCategories().size();i++){
                json.put(u.getCategories().get(i).getJson());
            }
        }

        String categories = json.toString();

        values.put(DISH_CATEGORIES, categories);

        try {
            db.insertOrThrow(TABLE_NAME, null, values);
        }catch (Exception e){
            Log.e("Insert error ",e.getLocalizedMessage());
        }
        db.close();
    }

    public Dish getById(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" WHERE "+DISH_ID+"="+id+"  LIMIT 1"
                ,null);

        if (cursor != null){

            int id_index = cursor.getColumnIndex(DISH_ID);
            int name_index = cursor.getColumnIndex(DISH_NAME);
            int price_index = cursor.getColumnIndex(DISH_PRICE);
            int short_desc_indx = cursor.getColumnIndex(DISH_SHORT_DESCRIPTION);
            int descr_indx = cursor.getColumnIndex(DISH_DESCRIPTION);
            int quantity_indx = cursor.getColumnIndex(DISH_QUANTITY);
            int picture_url_indx = cursor.getColumnIndex(DISH_PICTURE_URL);
            int categories_indx = cursor.getColumnIndex(DISH_CATEGORIES);


            if (cursor.moveToFirst()) {
                do {

                  return new Dish(cursor.getString(id_index),
                          cursor.getString(price_index),
                          cursor.getString(name_index),
                          cursor.getString(descr_indx),
                          cursor.getString(short_desc_indx),
                          cursor.getString(quantity_indx),
                          cursor.getString(picture_url_indx),
                          cursor.getString(categories_indx));


                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }

        db.close();

        return null;
    }


    public ArrayList<Dish> getCart(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {
                        DISH_ID,
                        DISH_PRICE,
                        DISH_NAME,
                        DISH_SHORT_DESCRIPTION,
                        DISH_DESCRIPTION,
                        DISH_QUANTITY,
                        DISH_PICTURE_URL,
                        DISH_CATEGORIES
                }, null,
                null, null, null, null, null);

//DISH_PRICE+","+ DISH_NAME+","+DISH_SHORT_DESCRIPTION+","+DISH_DESCRIPTION
        ArrayList<Dish> r = new ArrayList<>();



        if (cursor != null){

            int id_index = cursor.getColumnIndex(DISH_ID);
            int name_index = cursor.getColumnIndex(DISH_NAME);
            int price_index = cursor.getColumnIndex(DISH_PRICE);
            int short_desc_indx = cursor.getColumnIndex(DISH_SHORT_DESCRIPTION);
            int descr_indx = cursor.getColumnIndex(DISH_DESCRIPTION);
            int quantity_indx = cursor.getColumnIndex(DISH_QUANTITY);
            int picture_url_indx = cursor.getColumnIndex(DISH_PICTURE_URL);
            int categories_indx = cursor.getColumnIndex(DISH_CATEGORIES);


            if (cursor.moveToFirst()) {
                do {


                    r.add(new Dish(cursor.getString(id_index),
                            cursor.getString(price_index),
                            cursor.getString(name_index),
                            cursor.getString(descr_indx),
                            cursor.getString(short_desc_indx),
                            cursor.getString(quantity_indx),
                            cursor.getString(picture_url_indx),
                            cursor.getString(categories_indx)));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }


        db.close();

        return r;
    }


    public void clearTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

}

