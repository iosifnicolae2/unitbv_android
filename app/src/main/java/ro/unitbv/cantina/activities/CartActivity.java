package ro.unitbv.cantina.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import ro.unitbv.cantina.adapters.CartAdapter;
import ro.unitbv.cantina.R;
import ro.unitbv.cantina.database.DishDB;
import ro.unitbv.cantina.objects.Dish;

public class CartActivity extends AppCompatActivity {

    private CartAdapter cart_adapter;
    private ArrayList<Dish> menu_arraylist;
    private SwipeRefreshLayout swipe_refresh;
    private DishDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

      //  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);

        if( getSupportActionBar()!=null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        setTitle(R.string.cart_title);

        db = new DishDB(CartActivity.this);
        menu_arraylist = db.getCart();
        if(menu_arraylist==null)
            menu_arraylist = new ArrayList<>();


        //UnitbvApp app = (UnitbvApp) getApplication();

        swipe_refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_menu);
        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                menu_arraylist = db.getCart();
                cart_adapter.update_data(menu_arraylist);
                swipe_refresh.setRefreshing(false);
            }
        });

        final FloatingActionButton shopping_fab = (FloatingActionButton) findViewById(R.id.delete_cart);
        shopping_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db.clearTable();
                menu_arraylist = new ArrayList<Dish>();
                cart_adapter.update_data(menu_arraylist);
                Snackbar.make(view, R.string.cart_deleted, Snackbar.LENGTH_LONG).show();

            }
        });

        RecyclerView menu_list = (RecyclerView) findViewById(R.id.cart_list);
        menu_list.setHasFixedSize(true);

        menu_list.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx,int dy){
                super.onScrolled(recyclerView, dx, dy);

                if (dy >0) {
                    // Scroll Down
                    if (shopping_fab.isShown()) {
                        shopping_fab.hide();
                    }
                }
                else if (dy <0) {
                    // Scroll Up
                    if (!shopping_fab.isShown()) {
                        shopping_fab.show();
                    }
                }
            }
        });


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        menu_list.setLayoutManager(mLayoutManager);



        cart_adapter = new CartAdapter(this,menu_arraylist);
        menu_list.setAdapter(cart_adapter);


    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
