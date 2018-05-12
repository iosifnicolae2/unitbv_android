package ro.unitbv.cantina.fragments;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.engineio.client.Transport;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;
import com.google.firebase.crash.FirebaseCrash;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.MySSLSocketFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.scheme.PlainSocketFactory;
import cz.msebera.android.httpclient.conn.scheme.Scheme;
import cz.msebera.android.httpclient.conn.scheme.SchemeRegistry;
import cz.msebera.android.httpclient.entity.StringEntity;
import ro.unitbv.cantina.R;
import ro.unitbv.cantina.UnitbvApp;
import ro.unitbv.cantina.activities.CartActivity;
import ro.unitbv.cantina.activities.QueueActivity;
import ro.unitbv.cantina.adapters.CategoriesAdapter;
import ro.unitbv.cantina.adapters.DishAdapter;
import ro.unitbv.cantina.objects.Dish;
import ro.unitbv.cantina.objects.DishCats;

import static ro.unitbv.cantina.UnitbvApp.API_DOMAIN;

/**
 * Created by iosif on 10/15/16.
 */
public class MenuFragment extends Fragment{


    private DishAdapter menu_adapter;
    private ArrayList<Dish> menu_arraylist;
    private SwipeRefreshLayout swipe_refresh;
    private UnitbvApp app;
    private ArrayList<String> last_filter_categories = new ArrayList<>();
    private ArrayList<String> filter_categories = new ArrayList<>();
    private CategoriesAdapter.AdapterListener category_click = new CategoriesAdapter.AdapterListener() {

        @Override
        public void categories_updated(ArrayList<String> last_filter_categories) {
            filter_categories = last_filter_categories;
        }
    };
    private JsonHttpResponseHandler response_handler = new JsonHttpResponseHandler() {

        @Override
        public void onStart() {
            swipe_refresh.setRefreshing(true);
        }


        @Override
        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            swipe_refresh.setRefreshing(false);
            if(response.has("error")){

                Toast.makeText(getContext(), R.string.error_get_menu,Toast.LENGTH_SHORT).show();
            }
            try {

                if(response.has("data")){
                    JSONArray menu = response.getJSONArray("data");
                    menu_arraylist.clear();
                    for(int i=0;i<menu.length();i++){
                        JSONObject dish = menu.getJSONObject(i);
                        menu_arraylist.add(new Dish(dish));
                    }
                    update_data();

                }
            } catch (JSONException e) {
                e.printStackTrace();
                FirebaseCrash.report(e);
                Toast.makeText(getContext(),R.string.error_get_menu,Toast.LENGTH_SHORT).show();
            }
        }


        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);

            Toast.makeText(getContext(),R.string.error_get_menu,Toast.LENGTH_SHORT).show();
            swipe_refresh.setRefreshing(false);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            super.onFailure(statusCode, headers, responseString, throwable);
            Toast.makeText(getContext(),R.string.error_get_menu,Toast.LENGTH_SHORT).show();
            swipe_refresh.setRefreshing(false);

        }

        @Override
        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
            super.onFailure(statusCode, headers, throwable, errorResponse);
            Toast.makeText(getContext(),R.string.error_get_menu,Toast.LENGTH_SHORT).show();
            swipe_refresh.setRefreshing(false);

        }

        @Override
        public void onRetry(int retryNo) {
            // called when request is retried
        }
    };
    private ArrayList<DishCats> categories = new ArrayList<>();

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket(UnitbvApp.SOCKET_ENDPOINT);
            mSocket.io().on(Manager.EVENT_TRANSPORT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    Transport transport = (Transport) args[0];
                    transport.on(Transport.EVENT_ERROR, new Emitter.Listener() {
                        @Override
                        public void call(Object... args) {
                            Exception e = (Exception) args[0];
                            Log.e("Socket.io", "Transport error " + e);
                            e.printStackTrace();
                            e.getCause().printStackTrace();
                        }
                    });
                }
            });
        } catch (URISyntaxException e) {
            Log.d("socket.io error", e.getMessage());
        }
    }
    private Emitter.Listener updateMenuSocketListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), R.string.update_menu_socket_io, Toast.LENGTH_SHORT).show();
                        update_filters();
                    }
                });
            }
        }
    };

    public MenuFragment() {
        menu_arraylist = new ArrayList<>();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        populate_categories();
    }

    private void populate_categories() {
        AsyncHttpClient client = getAsyncHttpClient();
        client.get(API_DOMAIN+"/api/categories", new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                swipe_refresh.setRefreshing(false);
                try {

                    if(response.has("categories")){
                        JSONArray cats = response.getJSONArray("categories");
                        categories.clear();
                        for(int i=0;i<cats.length();i++){
                            JSONObject cat = cats.getJSONObject(i);
                            categories.add(new DishCats(cat));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    FirebaseCrash.report(e);
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_menu_fragment, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.filter_menu:
                showFilterDialog();
                break;
            case R.id.feedback_menu:
                send_feedback();
                break;
        }
        return true;

    }

    private void send_feedback() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, UnitbvApp.FEEDBACK_EMAIL_ADDRESS);
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.send_feedback_email_subject));
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.send_feedback_intent_title)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), R.string.feedback_error_no_email_client, Toast.LENGTH_SHORT).show();
        }
    }

    private void showFilterDialog() {
        if(categories.size()==0){
            Toast.makeText(getContext(),R.string.error_filter_categories,Toast.LENGTH_SHORT).show();
            return;
        }
        filter_categories = new ArrayList<>();
        if(getActivity() == null){
            Toast.makeText(getContext(), "Activity should't be null!.", Toast.LENGTH_SHORT).show();
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.select_categories));

        LayoutInflater inflater = getActivity().getLayoutInflater();
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.categories_activity,null);

        RecyclerView v = (RecyclerView) layout.findViewById(R.id.categories_adapter);


        v.setLayoutManager(new GridLayoutManager(getContext(),2, StaggeredGridLayoutManager.VERTICAL,false));

        CategoriesAdapter adapter = new CategoriesAdapter(getActivity(),category_click,categories,last_filter_categories);
        v.setAdapter(adapter);

        builder.setView(layout);

        String positiveText = getString(R.string.select_cats_save);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        last_filter_categories = filter_categories;
                        app.filter_category(filter_categories);
                        update_filters();
                    }
                });
        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.cancel();
                    }
                });



        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    private void update_filters() {

        if(last_filter_categories==null||last_filter_categories.size()==0){
            get_menu_today("");
            return;
        }
        AsyncHttpClient client = getAsyncHttpClient();

        try {
        JSONArray jsonParams = new JSONArray(Collections.singletonList(last_filter_categories));
        StringEntity entity;
            entity = new StringEntity(jsonParams.toString());
            client.post(getContext(), API_DOMAIN+"/api/todayMenu/filter", entity, "application/json",
                    response_handler);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getActivity() == null){
            Toast.makeText(getContext(), "Activity should't be null!.", Toast.LENGTH_SHORT).show();
            return;
        }

        getActivity().setTitle(R.string.menu_canteen_large);
        app = (UnitbvApp) getActivity().getApplication();
        get_menu_today("");

        // set Socket listener
        mSocket.on("update_menu", updateMenuSocketListener);
        mSocket.connect();
    }
    private AsyncHttpClient getAsyncHttpClient() {
        // TODO(iosif): this is an insecure method to prevent https errors
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", MySSLSocketFactory.getFixedSocketFactory(), 443));
        return new AsyncHttpClient(schemeRegistry);
    }

    private void get_menu_today(String query) {
        AsyncHttpClient client = getAsyncHttpClient();
        client.get(API_DOMAIN+"/api/todayMenu"+query, response_handler);
    }

    private void update_data() {
        app.setMenuData(menu_arraylist);
        menu_adapter.update_data(menu_arraylist);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        swipe_refresh = (SwipeRefreshLayout) v.findViewById(R.id.refresh_menu);

        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                update_filters();
            }
        });
        final FloatingActionButton shopping_fab = (FloatingActionButton) v.findViewById(R.id.shopping_fab);
        shopping_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),CartActivity.class);
                startActivity(i);
            }
        });
        final FloatingActionButton queue_button = (FloatingActionButton) v.findViewById(R.id.queue_button);
        queue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent o = new Intent(getActivity(),QueueActivity.class);
                startActivity(o);
            }
        });
        RecyclerView menu_list = (RecyclerView) v.findViewById(R.id.menu_list);
        menu_list.setHasFixedSize(true);

        menu_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (shopping_fab.isShown()&&queue_button.isShown()) {
                        shopping_fab.hide();
                        queue_button.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!shopping_fab.isShown()&&!queue_button.isShown()) {
                        shopping_fab.show();
                        queue_button.show();
                    }
                }
            }
        });


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        menu_list.setLayoutManager(mLayoutManager);


        menu_adapter = new DishAdapter(getActivity(),menu_arraylist);
        menu_adapter.setHasStableIds(true);

        menu_list.setAdapter(menu_adapter);

        // Inflate the layout for this fragment
        return v;
    }

    public void filterByCat(ArrayList<String> ids) {
        last_filter_categories = ids;
        //get_menu_today("/filter/by_category/"+id);
        update_filters();

    }
}
