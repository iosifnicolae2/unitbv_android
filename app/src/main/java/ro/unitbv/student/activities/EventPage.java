package ro.unitbv.student.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import ro.unitbv.student.R;
import ro.unitbv.student.database.OrarRowDB;
import ro.unitbv.student.objects.Eveniment;

/**
 * Created by iosif on 10/15/16.
 */
public class EventPage extends AppCompatActivity {
    private Eveniment event;
    private String maiParticipa;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_page);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



        Intent i = getIntent();
        String id = "-1";
        if(i!=null){
            id = i.getStringExtra("event_id");
        }

        OrarRowDB db = new OrarRowDB(this);
        event = db.getById(id);

        maiParticipa = db.maiParticipa(event);

        if(event == null){
            Toast.makeText(this,"Error fetch event informations.",Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, event.getTypeColorImg()));
        }


        TextView type_event = (TextView) findViewById(R.id.type_event);
        TextView location_event = (TextView) findViewById(R.id.location_event);
        TextView event_schedule = (TextView) findViewById(R.id.event_schedule);
        TextView event_participants = (TextView) findViewById(R.id.event_participants);
        TextView event_teacher = (TextView) findViewById(R.id.event_teacher);

        setTitle(event.getTitle());
        type_event.setText(event.getTypeFormated());
        location_event.setText(event.getFormatedLocation());
        event_schedule.setText(event.getformatedSchedule());
        event_participants.setText(maiParticipa);
        event_teacher.setText(event.getTeacher());



        toolbar.setBackgroundResource(event.getTypeColorImg());

    }

    public void go_event(View view) {

        Uri gmmIntentUri = Uri.parse("geo:0,0?q=Corpul "+event.getBuilding()+" Brasov");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

}
