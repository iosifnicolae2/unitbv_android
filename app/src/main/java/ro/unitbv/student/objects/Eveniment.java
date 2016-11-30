package ro.unitbv.student.objects;

import android.content.Context;
import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ro.unitbv.student.R;

/**
 * Created by iosif on 10/15/16.
 */
public class Eveniment {

    private final String[] start_hours = new String[]{
            "8:00","10:00","12:00","14:00","16:00","18:00","20:00"
    };
    private final String[] end_hours = new String[]{
            "9:50","11:50","13:50","15:50","17:50","19:50","21:50"
    };
    public final static int[] days = new int[]{
            R.string.luni,R.string.marti,R.string.miercuri,R.string.joi,R.string.vineri
    };
    private  int int_day;
    private String value_orar;
    private boolean saptamana_para;
    private int interval;
    private String id;
    private String title;

    // Identification data
    private String an;
    private String spec;
    private String grupa;
    private String sgr;

    //Timestamp data
    private String start_h;
    private String end_h;
    private String day;

    //Curs(C), Seminar(S), Laborator(L)
    private String type;

    //Location data
    private String building;
    private String floor;
    private String room;


    private String teacher;

    public Eveniment(Context c,  OrarRow orar){

        try {
            id = orar.getId();


            String[] values = orar.getValue().split(",");

            title = values[0];
            an = orar.getAn();
            spec = orar.getSpec();
            grupa = orar.getGrupa();
            sgr = orar.getSgr();

            interval = orar.getInterval();
            saptamana_para = orar.isSaptamana_para();
             value_orar = orar.getValue();

            start_h = start_hours[orar.getInterval()];
            end_h = end_hours[orar.getInterval()];
            day = c.getString(days[orar.getDay()-1]);

            int_day = orar.getDay();

            type = values[1];

            String location = values[2].replaceAll(" ", "");
            building = location.substring(0, 1);

            Pattern p = Pattern.compile("\\d+");
            Matcher m = p.matcher(location);

            if (m.find()) {
                room = m.group(0);
            }

            int region_tart = m.toMatchResult().start();
            floor = location.substring(1, region_tart);

            teacher = values[3];
        }catch(Exception e){
            Log.w("parse Event error: ",e);
        }
    }


    public Eveniment() {
        id="id";
        title="ALGAD";
        an="1";
        spec="AIA";
        grupa="4162";
        sgr="B";

        start_h = "12:00";
        end_h = "14:50";
        day = "Luni";

        type = "S";

        building = "V";
        floor = "IV";
        room = "8";

        teacher = "Popescu_G";

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getGrupa() {
        return grupa;
    }

    public void setGrupa(String grupa) {
        this.grupa = grupa;
    }

    public String getSgr() {
        return sgr;
    }

    public void setSgr(String sgr) {
        this.sgr = sgr;
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

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getTypeFormated() {
        if(type!=null){

            if(type.contains("C"))
                return R.string.course;
            if(type.contains("S"))
                return R.string.seminar;
            if(type.contains("L"))
                return R.string.laboratory;
        }

        return R.string.undefined_type;
    }

    public int getTypeColor() {
        if(type!=null){

            if(type.contains("C"))
                return R.color.course_img;
            if(type.contains("S"))
                return R.color.seminar_img;
            if(type.contains("L"))
                return R.color.laboratory_img;
        }

        return android.R.color.darker_gray;
    }

    public String getFormatedTime() {
        return start_h+" - "+end_h;
    }

    public int getTypeColorImg() {

        if(type!=null){

            if(type.contains("C"))
                return R.color.course_img;
            if(type.contains("S"))
                return R.color.seminar_img;
            if(type.contains("L"))
                return R.color.laboratory_img;
        }

        return android.R.color.darker_gray;
    }

    public String getFormatedLocation() {
        return "Corp "+building+" "+floor+" "+room;
    }

    public String getformatedSchedule() {
        return capitalize(getDay())+" "+getStart_h()+" - "+getEnd_h();
    }

    private String capitalize(String day) {
        if(day==null) return "";
        return day.substring(0,1).toUpperCase() + day.substring(1).toLowerCase();
    }


    public int getInterval() {
        return interval;
    }

    public boolean isSaptamana_para() {
        return saptamana_para;
    }

    public String getValue_orar() {
        return value_orar;
    }

    public int getInt_day() {
        return int_day;
    }
}
