package ro.unitbv.student.adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ro.unitbv.student.R;
import ro.unitbv.student.activities.EventPage;
import ro.unitbv.student.objects.Eveniment;

/**
 * Created by iosif on 10/15/16.
 */
public class OrarAdapter extends  RecyclerView.Adapter<OrarAdapter.ViewHolder>{

    private ArrayList<Eveniment> mDataset = new ArrayList<>();
    private Activity mActivity;


    public OrarAdapter(Activity activity,ArrayList<Eveniment> orar_arraylist) {
        mDataset = orar_arraylist;

        if(mDataset==null) this.mDataset = new ArrayList<>();
        mActivity = activity;

    }

    public void update_data(ArrayList<Eveniment> orar_arraylist) {
        this.mDataset = orar_arraylist;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
         View event_color;
         TextView event_title;
         TextView event_subtitle;
         TextView event_time;
         ImageView event_info_img;
         View v;

         ViewHolder(View v) {
            super(v);
            event_color = v.findViewById(R.id.event_color);

             event_title = (TextView) v.findViewById(R.id.event_title);
             event_subtitle = (TextView) v.findViewById(R.id.event_subtitle);
             event_time = (TextView) v.findViewById(R.id.event_time);
             event_info_img = (ImageView) v.findViewById(R.id.event_info_img);
        }
    }



    // Create new views (invoked by the layout manager)
    @Override
    public OrarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

       final Eveniment ev = mDataset.get(position);

        holder.event_color.setBackgroundColor(mActivity.getResources().getColor(ev.getTypeColor()));
        holder.event_title.setText(ev.getTitle());
        holder.event_subtitle.setText(ev.getFormatedLocation());

        holder.event_time.setText(ev.getFormatedTime());
        holder.event_info_img.setColorFilter(ev.getTypeColorImg(), PorterDuff.Mode.MULTIPLY);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSubject(ev);
            }
        });

    }

    private void onClickSubject(Eveniment e) {

        Intent i = new Intent(mActivity,EventPage.class);
        i.putExtra("event_id",e.getId());
        mActivity.startActivity(i);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
