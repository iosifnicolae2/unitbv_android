package ro.unitbv.cantina.adapters;

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

import ro.unitbv.cantina.R;
import ro.unitbv.cantina.activities.DishActivity;
import ro.unitbv.cantina.objects.Dish;

/**
 * Created by iosif on 10/15/16.
 */
public class DishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final String DISH_POSITION = "dish_position";
    public static final String DISH_ID_DB = "dish_id_db";
    private static final int TYPE_HEADER = 0;
    static final int TYPE_ITEM = 1;
    private final Picasso picasso;
    public boolean fromDB = false;
    protected ArrayList<Dish> mDataset;
    private Activity mActivity;
    private View header;
    private boolean with_header = false;


    public DishAdapter(View header, Activity activity, ArrayList<Dish> orar_arraylist) {
        this.header = header;
        this.with_header = (header != null);
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
        try {
            return mDataset.get(position).getId().hashCode();

        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position) && with_header)
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_header, parent, false);
            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_row, parent, false);
            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder view_holder, int p) {
        if (view_holder instanceof VHHeader) {
            VHHeader VHheader = (VHHeader) view_holder;
            VHheader.view = this.header;
        } else if (view_holder instanceof VHItem) {
            VHItem holder = (VHItem) view_holder;
            final int position = holder.getAdapterPosition() - (with_header ? 1 : 0);
            final Dish ev = mDataset.get(position);

            //   holder.event_color.setBackgroundColor(ev.getTypeColor());
            holder.dish_name.setText(ev.getName());
            if (holder.dish_description != null)
                holder.dish_description.setText(ev.getShort_description());

            holder.dish_price.setText(ev.getPrice());

            if (ev.getPicture_url() != null && ev.getPicture_url().length() > 0)
                picasso
                        .load(ev.getPicture_url())
                        .placeholder(R.color.dish_img_placeholder)
                        .error(android.R.color.transparent)
                        .fit().centerCrop()
                        .into(holder.dish_image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onClickSubject(ev, position);
                }
            });
        }
    }

    private void onClickSubject(Dish e, int position) {

        Intent i = new Intent(mActivity, DishActivity.class);
        if (fromDB) {
            i.putExtra(DISH_ID_DB, e.getId());

        } else {

            i.putExtra(DISH_POSITION, position);
        }
        mActivity.startActivity(i);
    }

    @Override
    public int getItemCount() {
        // we include the header
        return mDataset.size() + (with_header ? 1 : 0);
    }


    static class VHHeader extends RecyclerView.ViewHolder {
        View view;

        VHHeader(View view) {
            super(view);
            this.view = view;
        }
    }

    static class VHItem extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        TextView dish_name;
        TextView dish_description;
        TextView dish_price;
        ImageView dish_image;

        VHItem(View v) {
            super(v);
            dish_name = v.findViewById(R.id.dish_name);
            dish_description = v.findViewById(R.id.dish_description);
            dish_price = v.findViewById(R.id.dish_price);
            dish_image = v.findViewById(R.id.dish_image);
        }
    }
}


