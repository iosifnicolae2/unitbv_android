package ro.unitbv.cantina.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ro.unitbv.cantina.adapters.CategoriesAdapter;
import ro.unitbv.cantina.R;
import ro.unitbv.cantina.UnitbvApp;
import ro.unitbv.cantina.database.DishDB;
import ro.unitbv.cantina.objects.Dish;

import static ro.unitbv.cantina.adapters.DishAdapter.DISH_ID_DB;
import static ro.unitbv.cantina.adapters.DishAdapter.DISH_POSITION;

public class DishActivity extends AppCompatActivity {

    private Dish dish;
    private DishDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_dish);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = new DishDB(DishActivity.this);


        UnitbvApp app = (UnitbvApp) getApplication();
        int position = getIntent().getIntExtra(DISH_POSITION,-1);
        if(position>=0)
        dish = app.getMenuDish(position);
        else{
            String dish_db_ud = getIntent().getStringExtra(DISH_ID_DB);
            dish = db.getById(dish_db_ud);
        }

        if(dish==null){
            Toast.makeText(this,R.string.cannot_load_dish,Toast.LENGTH_SHORT).show();
            finish();
            return;
        }




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        TextView dish_price = (TextView) findViewById(R.id.dish_price);
        dish_price.setText(dish.getPrice());

        TextView dish_description = (TextView) findViewById(R.id.dish_description);
        dish_description.setText(dish.getDescription());

        TextView dish_grammage = (TextView) findViewById(R.id.dish_grammage);
        dish_grammage.setText(dish.getQuantity());

        ImageView imageview_dish = (ImageView) findViewById(R.id.imageview_dish);

        /*Picasso.Builder builder = new Picasso.Builder(this);
        builder.listener(new Picasso.Listener()
        {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception)
            {
                exception.printStackTrace();
            }
        });
        builder.build();*/
        Picasso picasso = Picasso.with(DishActivity.this);

        picasso
                .load(dish.getPicture_url())
                .placeholder(R.color.dish_img_placeholder)
                .error(android.R.color.white)
                .fit().centerCrop()
                .into(imageview_dish);



        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(dish.getName());



        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.add(dish);

                Snackbar.make(view, R.string.item_added, Snackbar.LENGTH_LONG)
                        .setAction(R.string.view_cart, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(DishActivity.this,CartActivity.class);
                                startActivity(i);
                            }
                        }).show();
            }
        });

        CategoriesAdapter adapter = new CategoriesAdapter(this,dish.getCategories(),1);
        RecyclerView dish_categories = (RecyclerView) findViewById(R.id.dish_categories);
        dish_categories.setAdapter(adapter);

    }


    public void click_category(String id) {
        ArrayList<String> r = new ArrayList<String>();
        r.add(id);
        ( (UnitbvApp)getApplication()).filter_category(r);
        finish();
    }
}
