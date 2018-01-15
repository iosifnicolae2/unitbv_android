package ro.unitbv.cantina.objects;

import android.util.Log;

/**
 * Created by iosif on 10/17/16.
 */
public class Interval {
    private String start_h;
    private String end_h;

    public Interval(String start_h, String end_h) {
        this.start_h = start_h;
        this.end_h = end_h;
    }

    public Interval(String s) {
        String[] interval_orar = s.split("-");
        if(interval_orar.length==2){
            start_h = interval_orar[0];
            end_h = interval_orar[1];
        }else
            Log.w("error parsing Interval","array length is less than 2");
    }

    public String getStart_h() {
        return start_h;
    }

    public void setStart_h(String start_h) {
        this.start_h = start_h;
    }

    public String getEnd_h() {
        return end_h;
    }

    public void setEnd_h(String end_h) {
        this.end_h = end_h;
    }
}
