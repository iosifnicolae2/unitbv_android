package ro.unitbv.student.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ro.unitbv.student.R;
import ro.unitbv.student.objects.Dish;

/**
 * Created by iosif on 11/2/16.
 */
public class CartAdapter extends DishAdapter {


    public CartAdapter(Activity activity, ArrayList<Dish> dish) {
        super(activity, dish);
        this.fromDB = true;
    }

    @Override
    public DishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_row_cart, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new DishAdapter.ViewHolder(v);
    }
}
