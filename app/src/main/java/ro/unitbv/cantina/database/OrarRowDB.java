package ro.unitbv.cantina.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

import ro.unitbv.cantina.objects.Eveniment;
import ro.unitbv.cantina.objects.OrarRow;

import static ro.unitbv.cantina.activities.ConfigureUser.PREFERENCE_USER_SETTINGS;

/**
 * Created by iosif on 7/23/16.
 */

/**
 * Created by iosif on 7/23/16.
 */

public class OrarRowDB extends DatabaseHelper {

/*
    private String an;
    private String spec;
    private String grupa;
    private String sgr;



    private int interval;
    private int day;
    private String value;

 */
    private static final String TABLE_NAME = "orar_rows_v1";
    private static final String ORAR_ID = "id";
    private static final String ORAR_AN = "an";
    private static final String ORAR_SPEC = "spec";
    private static final String ORAR_GR = "grupa";
    private static final String ORAR_SGR = "sgr";
    private static final String ORAR_INTERVAL = "interval";
    private static final String ORAR_DAY = "day";
    private static final String ORAR_VALUE = "value";

    private static final String ORAR_SAPTAMANA_PARA = "sapt_para";
    private static final String[] FIELDS_NAME = new String[]{
            ORAR_ID,
            ORAR_AN,
            ORAR_SPEC,
            ORAR_GR,
            ORAR_SGR,
            ORAR_INTERVAL,
            ORAR_DAY,
            ORAR_VALUE,
            ORAR_SAPTAMANA_PARA
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
            "INTEGER",

    };
    private final Context context;

    public OrarRowDB(Context context) {
        super(context, TABLE_NAME, FIELDS_NAME, FIELDS_TYPE);
        this.context = context;
    }

    public void add(OrarRow u) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
     //   values.put(ORAR_ID, u.getId());
        values.put(ORAR_AN, u.getAn());
        values.put(ORAR_SPEC, u.getSpec());
        values.put(ORAR_GR, u.getGrupa());
        values.put(ORAR_SGR, u.getSgr());


        values.put(ORAR_INTERVAL,u.getInterval());
        values.put(ORAR_DAY, u.getDay());
        values.put(ORAR_VALUE, u.getValue());
        values.put(ORAR_SAPTAMANA_PARA, u.isSaptamana_para());

        try {
            db.insertOrThrow(TABLE_NAME, null, values);
        }catch (Exception e){
            Log.e("Insert error ",e.getLocalizedMessage());
        }
        db.close();
    }

    public Eveniment getById(String id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor  cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" WHERE "+ORAR_ID+"="+id+"  LIMIT 1"
                ,null);

        if (cursor != null){

            int id_index = cursor.getColumnIndex(ORAR_ID);
            int spec_index = cursor.getColumnIndex(ORAR_SPEC);
            int gr_indx = cursor.getColumnIndex(ORAR_GR);
            int sgr_indx = cursor.getColumnIndex(ORAR_SGR);
            int intrv_indx = cursor.getColumnIndex(ORAR_INTERVAL);
            int day_index = cursor.getColumnIndex(ORAR_DAY);
            int val_ind = cursor.getColumnIndex(ORAR_VALUE);
            int saptamana_para = cursor.getColumnIndex(ORAR_SAPTAMANA_PARA);


            if (cursor.moveToFirst()) {
                do {


                   return new Eveniment(context,new OrarRow(cursor.getInt(id_index),cursor.getString(cursor.getColumnIndex(ORAR_AN)),
                            cursor.getString(spec_index),
                            cursor.getString(gr_indx),
                            cursor.getString(sgr_indx),
                            cursor.getInt(intrv_indx),
                            cursor.getInt(day_index),
                            cursor.getString(val_ind), cursor.getInt(saptamana_para)>0));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }

        db.close();

        return null;
    }


    public String maiParticipa(Eveniment e){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor  cursor = db.rawQuery("SELECT "+ORAR_SPEC+","+ORAR_SGR+","+ORAR_GR+" from "+TABLE_NAME+" WHERE "+ORAR_DAY+"="+e.getInt_day()+" AND "+ORAR_INTERVAL+"="+e.getInterval()+" AND "+ORAR_SAPTAMANA_PARA+"="+(e.isSaptamana_para()?1:0)+
                " AND "+ORAR_VALUE+"='"+e.getValue_orar()+"';"
                ,null);

        if (cursor != null){

            int gr_index = cursor.getColumnIndex(ORAR_GR);
            int spec_index = cursor.getColumnIndex(ORAR_SPEC);
            int sgr_index = cursor.getColumnIndex(ORAR_SGR);

            String s = "";
            if (cursor.moveToFirst()) {
                do {

                    s+=cursor.getString(spec_index);
                    String gr = cursor.getString(gr_index);
                    s+=" "+gr.substring(gr.length() - 1);
                    s+=cursor.getString(sgr_index);


                    if(cursor.moveToNext())
                        s+=", ";
                    else return s;
                } while (true);
            }


            // return contact
            cursor.close();
        }

        db.close();

        return "";
    }

    public ArrayList<OrarRow> getSchedule(){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] {
                        ORAR_ID,
                        ORAR_AN,
                        ORAR_SPEC,
                        ORAR_GR,
                        ORAR_SGR,
                        ORAR_INTERVAL,
                        ORAR_DAY,
                        ORAR_VALUE,
                        ORAR_SAPTAMANA_PARA
                }, null,
                null, null, null, null, String.valueOf(20));

//ORAR_AN+","+ ORAR_SPEC+","+ORAR_GR+","+ORAR_SGR
        ArrayList<OrarRow> r = new ArrayList<>();



        if (cursor != null){

            int id_index = cursor.getColumnIndex(ORAR_ID);
            int spec_index = cursor.getColumnIndex(ORAR_SPEC);
            int gr_indx = cursor.getColumnIndex(ORAR_GR);
            int sgr_indx = cursor.getColumnIndex(ORAR_SGR);
            int intrv_indx = cursor.getColumnIndex(ORAR_INTERVAL);
            int day_index = cursor.getColumnIndex(ORAR_DAY);
            int val_ind = cursor.getColumnIndex(ORAR_VALUE);
            int saptamana_para = cursor.getColumnIndex(ORAR_SAPTAMANA_PARA);


            if (cursor.moveToFirst()) {
                do {


                    r.add(new OrarRow(cursor.getInt(id_index), cursor.getString(cursor.getColumnIndex(ORAR_AN)),
                            cursor.getString(spec_index),
                            cursor.getString(gr_indx),
                            cursor.getString(sgr_indx),
                            cursor.getInt(intrv_indx),
                            cursor.getInt(day_index),
                            cursor.getString(val_ind), cursor.getInt(saptamana_para)>0));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }


        db.close();

        return r;
    }

    public boolean checkSchedule() {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{"COUNT(*)"}, null,
                null, null, null, null, null);

//ORAR_AN+","+ ORAR_SPEC+","+ORAR_GR+","+ORAR_SGR
        ArrayList<OrarRow> r = new ArrayList<>();



        if (cursor != null){

            if (cursor.moveToFirst()) {
                return cursor.getInt(0)>0;
            }


            // return contact
            cursor.close();
        }


        db.close();
        return false;
    }

    public void clearTable() {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME,null,null);
    }

    public ArrayList<Eveniment> getTodaySchedule(int mDay) {

        SharedPreferences sharedPref = context.getSharedPreferences(PREFERENCE_USER_SETTINGS, Context.MODE_PRIVATE);

        String year_s = sharedPref.getString("year", "");
        //String spec_s = sharedPref.getString("spec", "");
        String grupa_s = sharedPref.getString("grupa", "");
        String sgr_s = sharedPref.getString("sgr", "");

        Calendar today = Calendar.getInstance();
        int s_para = today.get(Calendar.WEEK_OF_YEAR)%2==0?1:0;
       // int day = today.get(Calendar.DAY_OF_WEEK)-1;//Monday equal 1
      //  int hour = today.get(Calendar.HOUR_OF_DAY);
      //  int minimum_interval = getMinimumInterval(hour);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        if(year_s.length()==0){
           /* cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" WHERE "+ORAR_DAY+"=? AND "+ORAR_INTERVAL+">=? ORDER BY "+ORAR_INTERVAL +" LIMIT 20",
                    new String[]{
                            String.valueOf(day), String.valueOf(minimum_interval)
                    });*/
            return new ArrayList<>();
        }else{
            //cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" WHERE "+ORAR_DAY+"="+day+"  AND "+ORAR_GR+"="+grupa_s+"  AND "+ORAR_INTERVAL+">="+minimum_interval+" AND "+ORAR_SGR+"='"+sgr_s+"' AND "+ORAR_SAPTAMANA_PARA+"="+s_para+" ORDER BY "+ORAR_INTERVAL +" LIMIT 20",
                //    null);
        }

        cursor = db.rawQuery("SELECT * from "+TABLE_NAME+" WHERE "+ORAR_DAY+"="+(mDay+1)+"  AND "+ORAR_GR+"=4462  AND "+ORAR_INTERVAL+">=0 AND "+ORAR_SGR+"='B' AND "+ORAR_SAPTAMANA_PARA+"="+s_para+" ORDER BY "+ORAR_INTERVAL +" LIMIT 20"
                    ,null);

       /* query(TABLE_NAME, new String[] {
                        ORAR_AN,
                        ORAR_SPEC,
                        ORAR_GR,
                        ORAR_SGR,
                        ORAR_INTERVAL,
                        ORAR_DAY,
                        ORAR_VALUE
                }, null,
                null, null, ORAR_DAY+"=?", ORAR_DAY+","+ORAR_INTERVAL, null);*/

        ArrayList<Eveniment> orar_schedule = new ArrayList<>();


        if (cursor != null){

            int id_index = cursor.getColumnIndex(ORAR_ID);
            int spec_index = cursor.getColumnIndex(ORAR_SPEC);
            int gr_indx = cursor.getColumnIndex(ORAR_GR);
            int sgr_indx = cursor.getColumnIndex(ORAR_SGR);
            int intrv_indx = cursor.getColumnIndex(ORAR_INTERVAL);
            int day_index = cursor.getColumnIndex(ORAR_DAY);
            int val_ind = cursor.getColumnIndex(ORAR_VALUE);
            int saptamana_para = cursor.getColumnIndex(ORAR_SAPTAMANA_PARA);


            if (cursor.moveToFirst()) {
                do {


                    orar_schedule.add(new Eveniment(context,new OrarRow(cursor.getInt(id_index),cursor.getString(cursor.getColumnIndex(ORAR_AN)),
                            cursor.getString(spec_index),
                            cursor.getString(gr_indx),
                            cursor.getString(sgr_indx),
                            cursor.getInt(intrv_indx),
                            cursor.getInt(day_index),
                            cursor.getString(val_ind), cursor.getInt(saptamana_para)>0)));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }


        return orar_schedule;
    }

    private int getMinimumInterval(int hour) {

        if(hour>22) return 10;
        if(hour>20) return 6;
        if(hour>18) return 5;
        if(hour>16) return 4;
        if(hour>14) return 3;
        if(hour>12) return 2;
        if(hour>10) return 1;
        if(hour>8) return 0;

        return 0;//show all
    }

    public ArrayList<String> getOrarYears() {

        return getOrarFields(ORAR_AN);
    }

    public ArrayList<String> getOrarSpecs() {

        return getOrarFields(ORAR_SPEC);
    }

    public ArrayList<String> getOrarSGR() {

        return getOrarFields(ORAR_SGR);
    }

    public ArrayList<String> getOrarGrupaTotal() {
        return getOrarFields(ORAR_GR);
    }

    public ArrayList<String> getOrarGrupa(String year, String spec) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ORAR_GR+" from "+TABLE_NAME+" WHERE "+ORAR_AN+"=? AND "+ORAR_SPEC+"=? "+" GROUP BY "+ORAR_GR, new String[]{
                year,spec
        });

        ArrayList<String> result = new ArrayList<>();

        if (cursor != null){


            if (cursor.moveToFirst()) {
                do {


                    result.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }


        return result;

    }

    private ArrayList<String> getOrarFields(String field) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+field+" from "+TABLE_NAME+" GROUP BY "+field, null);

        ArrayList<String> result = new ArrayList<String>();

        if (cursor != null){


            if (cursor.moveToFirst()) {
                do {


                    result.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }


            // return contact
            cursor.close();
        }


        return result;
    }

    public void insertMoreRows(ArrayList<OrarRow> orar) {
        //
           // db.add(orar.get(i));

        SQLiteDatabase db = this.getWritableDatabase();

        //String insert_query = "INSERT INTO orar_rows_v1 (an,spec,grupa,sgr,interval,day,value,sapt_para) VALUES ('1','ET','4161','A','0','1','Engleza, S, NI11, Sasu_E_L','1'),('1','ET','4161','A','2','1','PCLP I, C, NI1, Firastrau_I','1')";
        String insert_query = "INSERT INTO "+TABLE_NAME+" ("+ORAR_AN+","+ORAR_SPEC+","+ORAR_GR+","+ORAR_SGR+","+ORAR_INTERVAL+","+ORAR_DAY+","+ORAR_VALUE+","+ORAR_SAPTAMANA_PARA+") VALUES ";

        for(int i=0,l=orar.size();i<l-1;i++){
            insert_query+="('"+orar.get(i).getAn()+"','"+orar.get(i).getSpec()+"','"+orar.get(i).getGrupa()+"','"+orar.get(i).getSgr()+"','"+orar.get(i).getInterval()+"','"+orar.get(i).getDay()+"','"+orar.get(i).getValue()+"','"+(orar.get(i).isSaptamana_para()?1:0)+"'),";

        }

        int i=orar.size()-1;
        insert_query+="('"+orar.get(i).getAn()+"','"+orar.get(i).getSpec()+"','"+orar.get(i).getGrupa()+"','"+orar.get(i).getSgr()+"','"+orar.get(i).getInterval()+"','"+orar.get(i).getDay()+"','"+orar.get(i).getValue()+"','"+(orar.get(i).isSaptamana_para()?1:0)+"');";

        try {
            db.execSQL(insert_query);
        }catch (Exception e){
            Log.e("Insert error ",e.getLocalizedMessage());
        }
        db.close();
    }
}

