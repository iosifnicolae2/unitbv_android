package ro.unitbv.cantina.activities;
import android.annotation.SuppressLint;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONArrayRequestListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ro.unitbv.cantina.R;

public class QueueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waze_menu);

        if (getSupportActionBar() != null) {

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle(R.string.coada_cantina);

        final Button button1 = (Button) findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer button1 = 10;

            }
        });
        final Button button2 = (Button) findViewById(R.id.button12);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
                Integer button12 = 20;
            }
        });
        final Button button3 = (Button) findViewById(R.id.button23);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
                Integer button23 = 30;
            }
        });
        final Button button4 = (Button) findViewById(R.id.button34);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
                Integer button23 = 30;
            }
        });
        final Button button5 = (Button) findViewById(R.id.button45);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
                push_queue_estimation_to_server(30);
            }
        });
        final Button button6 = (Button) findViewById(R.id.button56);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.queue_thanks, Snackbar.LENGTH_LONG).show();
            }
        });


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

//        AndroidNetworking.post("https://unitbv.mailo.ml/api/queue/waze_clients")
//                .addJSONObjectBody(jsonObject)
//                .setPriority(Priority.MEDIUM)
//                .build()
//                .getAsJSONArray(new JSONArrayRequestListener() {
//                    @Override
//                    public void onResponse(JSONArray response) {
//                        Log.w("response", response.toString());
//                    }
//                    @Override
//                    public void onError(ANError error) {
//                        // handle error
//                    }
//                });
    }
}
