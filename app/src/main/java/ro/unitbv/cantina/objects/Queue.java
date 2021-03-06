package ro.unitbv.cantina.objects;

import org.json.JSONException;
import org.json.JSONObject;


public class Queue {
    private String id;
    private String name;
    private JSONObject json;

    public Queue(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public Queue(JSONObject o) throws JSONException {
        json = o;
        this.id = !o.has("_id")?"":o.getString("_id");
        this.name = !o.has("name")?"":o.getString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getJson() {
        if(json!=null)
            return json;
        JSONObject j =  new JSONObject();
        try {
            j.put("_id",id);
            j.put("name",name);
            return j;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }
}
