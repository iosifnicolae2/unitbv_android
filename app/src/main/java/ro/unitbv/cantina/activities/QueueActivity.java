package ro.unitbv.cantina.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import ro.unitbv.cantina.R;
import ro.unitbv.cantina.UnitbvApp;

public class QueueActivity extends AppCompatActivity {
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waze_menu);

        if( getSupportActionBar()!=null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle(R.string.coada_cantina);

        coordinatorLayout = findViewById(R.id.coordinator_layout);

        // Setup click listeners for buttons.
        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(10);
            }
        });
        final Button button2 = (Button) findViewById(R.id.button12);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(20);
            }
        });
        final Button button3 = (Button) findViewById(R.id.button23);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(30);
            }
        });
        final Button button4 = (Button) findViewById(R.id.button34);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(40);
            }
        });
        final Button button5 = (Button) findViewById(R.id.button45);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(50);
            }
        });
        final Button button6 = (Button) findViewById(R.id.button56);
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                push_queue_estimation_to_server(60);
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private void push_queue_estimation_to_server(int estimate) {
        @SuppressLint("HardwareIds")
        String android_id = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("client_id", android_id);
            jsonObject.put("number_of_clients", estimate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.post(UnitbvApp.WAZE_POST_CLIENTS_API)
                .addJSONObjectBody(jsonObject)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                             Log.w("response", response.toString());
                        }catch(Exception e){
                            Log.w("error",e.toString());
                            Snackbar.make(coordinatorLayout, R.string.queue_problem, Snackbar.LENGTH_LONG).show();
                            }
                        Snackbar.make(coordinatorLayout, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(ANError anError) {

                        anError.printStackTrace();
                        Snackbar.make(coordinatorLayout, R.string.queue_problem, Snackbar.LENGTH_LONG).show();

                    }
                });
    }
}
