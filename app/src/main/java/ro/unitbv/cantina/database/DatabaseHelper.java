package ro.unitbv.cantina.database;

/**
 * Created by iosif on 6/11/16.
 */

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {


    // am modificat acest template: https://github.com/khetiyachintan/SQLite-Database-Example/blob/master/src/chintan/khetiya/sqlite/cursor/DatabaseHandler.java


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Unitbv";


    // QR_CODE table name
    private String TABLE_NAME = "test_db" ;


    private final String[] fields_name;
    private final  String[] fields_type;

    public DatabaseHelper(Context context,String table_name, String[] fields_name, String[] fields_type) {
        super(context, DATABASE_NAME+table_name, null, DATABASE_VERSION);
        this.TABLE_NAME = table_name;
        this.fields_name = fields_name;
        this.fields_type = fields_type;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "(";

        int f_size = fields_name.length-1;

        for(int i =0;i<f_size;i++){
            CREATE_TABLE+=fields_name[i]+" "+fields_type[i]+", ";
        }
        if(f_size>0)
        CREATE_TABLE+=fields_name[f_size]+" "+fields_type[f_size];

        CREATE_TABLE+=")";

        db.execSQL(CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //todo trebuie verificat ce se intampla aici..

        // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }



    /* Delete commands */
    // Deleting single contact

    // Deleting single contact
    public void deleteByField(String field_name,String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, field_name + " = ?",
                new String[] { value });
        db.close();
    }




    // Getting QR_CODE Count
    public int getCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

}
