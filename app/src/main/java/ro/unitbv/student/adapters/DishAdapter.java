package ro.unitbv.student.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ro.unitbv.student.R;
import ro.unitbv.student.activities.DishActivity;
import ro.unitbv.student.objects.Dish;

/**
 * Created by iosif on 10/15/16.
 */
public class DishAdapter extends RecyclerView.Adapter<DishAdapter.ViewHolder>{

    public static final String DISH_POSITION = "dish_position";
    public static final String DISH_ID_DB = "dish_id_db";
    private  ArrayList<Dish> mDataset;
    private final Picasso picasso;
    private Activity mActivity;
    public boolean fromDB = false;


        public DishAdapter(Activity activity,ArrayList<Dish> orar_arraylist) {
            mDataset = orar_arraylist;
            mActivity = activity;



            picasso = Picasso.with(activity);


        }

    public void update_data(ArrayList<Dish> menu_arraylist) {
        this.mDataset = menu_arraylist;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        try{
            return mDataset.get(position).getId().hashCode();

        }catch(Exception e){
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            TextView dish_name;
            TextView dish_description;
            TextView dish_price;
            ImageView dish_image;

            ViewHolder(View v) {
                super(v);

                dish_name = (TextView) v.findViewById(R.id.dish_name);
                dish_description = (TextView) v.findViewById(R.id.dish_description);
                dish_price = (TextView) v.findViewById(R.id.dish_price);
                dish_image = (ImageView) v.findViewById(R.id.dish_image);
            }
        }



        // Create new views (invoked by the layout manager)
        @Override
        public DishAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
        int viewType) {
            // create a new view
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.dish_row, parent, false);
            // set the view's size, margins, paddings and layout parameters

            return new DishAdapter.ViewHolder(v);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final DishAdapter.ViewHolder holder, final int position) {

            final Dish ev = mDataset.get(position);

            //   holder.event_color.setBackgroundColor(ev.getTypeColor());
            holder.dish_name.setText(ev.getName());
            if(holder.dish_description!=null)
            holder.dish_description.setText(ev.getShort_description());

            holder.dish_price.setText(ev.getPrice());

            if(ev.getPicture_url()!=null&&ev.getPicture_url().length()>0)
            picasso
                    .load(ev.getPicture_url())
                    .placeholder(R.color.dish_img_placeholder)
                    .error(android.R.color.transparent)
                    .fit().centerCrop()
                    .into(holder.dish_image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSubject(ev,position);
                }
            });
        }

    private void onClickSubject(Dish e,int position) {

        Intent i = new Intent(mActivity,DishActivity.class);
        if(fromDB){
            i.putExtra(DISH_ID_DB,e.getId());

        }else{

            i.putExtra(DISH_POSITION,position);
        }
        mActivity.startActivity(i);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


