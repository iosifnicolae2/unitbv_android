package ro.unitbv.cantina.objects;

/**
 * Created by iosif on 10/17/16.
 */
public class OrarRow {

    private final boolean saptamana_para;
    private String id;

    private String an;
    private String spec;
    private String grupa;
    private String sgr;


    private int interval;
    private int day;
    private String value;





    public OrarRow(int id, String an, String spec, String grupa, String sgr, int interval, int day, String value, boolean saptamana_para) {
        this.id = String.valueOf(id);
        this.an = an;
        this.spec = spec;
        this.grupa = grupa;
        this.sgr = sgr;

        this.interval = interval;
        this.day = day;
        this.value = value;
        this.saptamana_para = saptamana_para;



    }

    /*
        public OrarRow(String an, String spec, String grupa, String[] interval_values) {
            this.an = an;
            this.spec = spec;
            this.grupa = grupa;
            this.sgr = interval_values[3];

            orarValues = new ArrayList<>();

            orarValues.addAll(populateOrarDay(4,interval_values,0));
            orarValues.addAll(populateOrarDay(11,interval_values,1));
            orarValues.addAll(populateOrarDay(18,interval_values,2));
            orarValues.addAll(populateOrarDay(25,interval_values,3));
            orarValues.addAll(populateOrarDay(32,interval_values,4));

        }
    */


    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getAn() {
        return an;
    }

    public void setAn(String an) {
        this.an = an;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public boolean isSaptamana_para() {
        return saptamana_para;
    }
}
