package ro.unitbv.cantina.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ro.unitbv.cantina.R;
import ro.unitbv.cantina.objects.Dish;

/**
 * Created by iosif on 11/2/16.

 */
public class CartAdapter extends DishAdapter {


    public CartAdapter(Activity activity, ArrayList<Dish> dish) {
        super(null, activity, dish);
        this.fromDB = true;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dish_row_cart, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new VHItem(v);
    }
}
