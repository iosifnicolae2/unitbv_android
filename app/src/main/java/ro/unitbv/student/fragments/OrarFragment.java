package ro.unitbv.student.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;

import ro.unitbv.student.R;
import ro.unitbv.student.UnitbvApp;
import ro.unitbv.student.activities.MainActivity;
import ro.unitbv.student.adapters.OrarAdapter;
import ro.unitbv.student.database.OrarRowDB;
import ro.unitbv.student.objects.Eveniment;

import static ro.unitbv.student.R.string.orar;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrarFragment extends Fragment {

    private final int current_day;

    public OrarFragment() {
        // Required empty public constructor

        Calendar today = Calendar.getInstance();
        current_day = today.get(Calendar.DAY_OF_WEEK)-2;//Monday equal 1
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setTitle(orar);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orar, container, false);


        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        viewPager.setAdapter(new OrarFragmentAdapter(getActivity()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
       // tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setupWithViewPager(viewPager);


        TabLayout.Tab tab = tabLayout.getTabAt(current_day);
        if (tab != null) {
            tab.select();
        }


        // Inflate the layout for this fragment
        return v;
    }

    private RecyclerView orar_list;
    private OrarAdapter orar_adapter;
    private ArrayList<Eveniment> orar_arraylist;
    private int mDay = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        UnitbvApp app = (UnitbvApp) getActivity().getApplication();
        app.setScheduleUpdatedCallback(new UnitbvApp.ScheduleUpdatedCallback() {
            @Override
            public void schedule_updated() {
                orar_arraylist = get_orar_schedule(mDay);

                orar_adapter = new OrarAdapter(getActivity(),orar_arraylist);

                orar_list.setAdapter(orar_adapter);

            }
        });

    }
    private ArrayList<Eveniment> get_orar_schedule( int mDay) {

        OrarRowDB db = new OrarRowDB(getActivity());
        orar_arraylist =  db.getTodaySchedule(mDay);

        return orar_arraylist;
    }

    private class OrarFragmentAdapter extends PagerAdapter {

        final int PAGE_COUNT = 5;
        private final LayoutInflater inflater;


        public OrarFragmentAdapter(Context context) {
            super();
            orar_adapter = new OrarAdapter(getActivity(),orar_arraylist);

            inflater = LayoutInflater.from(context);


        }

        //view inflating..
        @Override
        public Object instantiateItem(ViewGroup collection, int position) {


            orar_list = (RecyclerView) inflater.inflate(R.layout.fragment_orar_day,null, false);
            orar_list.setHasFixedSize(true);

            orar_arraylist = get_orar_schedule(position);
            orar_adapter = new OrarAdapter(getActivity(),orar_arraylist);


            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            orar_list.setLayoutManager(mLayoutManager);

            orar_list.setAdapter(orar_adapter);


            collection.addView(orar_list);
            return orar_list;
        }



        @Override
        public void destroyItem(ViewGroup collection, int position, Object view) {
            collection.removeView((View) view);
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }



        @Override
        public CharSequence getPageTitle(int position) {
            // Generate title based on item position
            return getString(Eveniment.days[position]);
        }
    }
}
