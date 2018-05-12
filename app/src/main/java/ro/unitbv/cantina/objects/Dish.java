package ro.unitbv.cantina.objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by iosif on 11/1/16.
 */

public class Dish {

    /*
    [
      {
        _id:"123",
        price:12.5,
        name:"Nume"
        description:"descriere",
        quantity:"125.4g",
        picture_url:"http://domain.com/img.jpg"

      }
    ]
     */

    private String id;
    private String price;
    private String name;
    private String short_description;
    private String description;
    private String quantity;
    private String picture_url;
    private ArrayList<DishCats> categories;

    public Dish() {
        this.id = "id";
        this.price = "price";
        this.name = "name";
        this.description = "description";
        this.quantity = "quantity";
        this.picture_url = "https://0x0800.github.io/2048-CUPCAKES/style/img/1024.jpg";
    }

    public Dish(String id, String price, String name, String description,String short_description, String quantity, String picture_url,ArrayList<DishCats> categories) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.short_description = short_description;
        this.description = description;
        this.quantity = quantity;
        this.picture_url = picture_url;
        this.categories = categories;
    }

    public Dish(String id, String price, String name, String description,String short_description, String quantity, String picture_url,String categories) {
        this.id = id;
        this.price = price;
        this.name = name;
        this.short_description = short_description;
        this.description = description;
        this.quantity = quantity;
        this.picture_url = picture_url;
        try {
            JSONArray cats =  new JSONArray(categories);
            this.categories = convertCatergories(cats);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<DishCats> convertCatergories(JSONArray cats) {

        if(cats==null)return null;

        ArrayList<DishCats> r = new ArrayList<>();
        for(int i=0;i<cats.length();i++){
            JSONObject obj;
            try {
                obj = cats.getJSONObject(i);
                r.add(new DishCats(obj));
                Log.w("insert cat",obj.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // r.add(new DishCats());
        }
        //todo convert to ArrayList
        return r;
    }

    public Dish(JSONObject dish) throws JSONException {
        this.id = !dish.has("_id")?"":dish.getString("_id");
        this.price = !dish.has("price")?"":dish.getString("price");
        this.name = !dish.has("name")?"":dish.getString("name");
        this.short_description = !dish.has("short_description")?"":dish.getString("short_description");
        this.description = !dish.has("description")?"":dish.getString("description");
        this.quantity = !dish.has("quantity")?"":dish.getString("quantity");
        this.picture_url = !dish.has("cdn_logo")?"":dish.getString("cdn_logo");
        if(dish.has("categories")){
            JSONArray cats = dish.getJSONArray("categories");
            this.categories = new ArrayList<>();
            for(int i=0;i<cats.length();i++)
             this.categories.add(new DishCats(cats.getJSONObject(i)));
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPicture_url() {
        return picture_url;
    }

    public void setPicture_url(String picture_url) {
        this.picture_url = picture_url;
    }

    public String getShort_description() {
        return short_description;
    }

    public ArrayList<DishCats> getCategories(){
        return categories;
    }
}
