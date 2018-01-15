package ro.unitbv.cantina.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import ro.unitbv.cantina.R;
import ro.unitbv.cantina.activities.DishActivity;
import ro.unitbv.cantina.objects.DishCats;

/**
 * Created by iosif on 11/26/16.
 */
public class CategoriesAdapter  extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {
    private int v_type;
    private ArrayList<String> last_filter_categories;
    private ArrayList<DishCats> mDataset;
    private DishActivity activity;
    private Activity a;
    // Provide a reference to the views for each data item
// Complex data items may need more than one view per item, and
// you provide access to all the views for a data item in a view holder
public static class ViewHolder extends RecyclerView.ViewHolder {
    // each data item is just a string in this case
    private TextView mTextView;
    public ViewHolder(View v) {
        super(v);
        mTextView = (TextView) v;
    }
}
    public interface AdapterListener{
        void categories_updated(ArrayList<String> last_filter_categories);
    }
    private AdapterListener listener;

    public CategoriesAdapter(Activity a, AdapterListener listener, ArrayList<DishCats> myDataset, ArrayList<String> last_filter_categories) {
        this.a = a;
        this.listener = listener;
        mDataset = myDataset;
        this.last_filter_categories = last_filter_categories;
    }
    // Provide a suitable constructor (depends on the kind of dataset)
    public CategoriesAdapter(DishActivity dishActivity, ArrayList<DishCats> myDataset, int v_type) {
        activity = dishActivity;
        mDataset = myDataset;
        this.v_type = v_type;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public CategoriesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v;
        if(v_type==1)
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_element_wrap, parent, false); else
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_element, parent, false);

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        DishCats cat = mDataset.get(position);


        if(last_filter_categories!=null)
        if(!last_filter_categories.contains(cat.getId())){
            holder.mTextView.setBackgroundResource(R.drawable.rounded_border_categories);
            holder.mTextView.setTextColor(a.getResources().getColor(R.color.text_color_cats));
        }else{
            holder.mTextView.setBackgroundResource(R.drawable.rounded_border_categories_checked);
            holder.mTextView.setTextColor(a.getResources().getColor(R.color.text_color_cats_checked));
        }


        holder.mTextView.setText(cat.getName());
        holder.mTextView.setOnClickListener(click_listener(holder.mTextView,cat.getId()));
    }

    private View.OnClickListener click_listener(final TextView mTextView, final String id) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(last_filter_categories!=null)
                if(last_filter_categories.contains(id)){
                    last_filter_categories.remove(id);
                    mTextView.setBackgroundResource(R.drawable.rounded_border_categories);
                    mTextView.setTextColor(a.getResources().getColor(R.color.text_color_cats));
                }else{
                    last_filter_categories.add(id);
                    mTextView.setBackgroundResource(R.drawable.rounded_border_categories_checked);
                    mTextView.setTextColor(a.getResources().getColor(R.color.text_color_cats_checked));
                }

                if(activity!=null)
                    activity.click_category(id);
                else if(listener!=null)
                    listener.categories_updated(last_filter_categories);


            }
        };
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}



