package addfood;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import adapter.FoodListAdapter;
import helper.FoodListHelper;

import com.example.moumita.caloriecountergeb.R;

import java.util.ArrayList;
import java.util.List;

import categorydatabase.CategoryOperations;
import categorydatabase.FoodCategory;

public class FoodListActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String categoryName;
    private CategoryOperations categoryData;
    private ListView mListView;
    private String mealType;
    private TextView foodHeaderText;
    private ListView categoryListView;
    private FoodListAdapter foodListAdapter;
    private List<FoodListHelper> categoryList = new ArrayList<>();
    private Typeface mTfRegular, mTfLight, mtfBold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        categoryData = new CategoryOperations(this);
        Bundle bundle = getIntent().getExtras();
        mealType = bundle.getString("meal_type");
        categoryName = bundle.getString("categoryname");

        categoryListView = findViewById(R.id.food_list_view);
        foodHeaderText = findViewById(R.id.food_header);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getAssets(), "OpenSans-Light.ttf");
        mtfBold = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        foodHeaderText.setTypeface(mtfBold);

        categoryData.open();

        final List<FoodCategory> foodCategoryList = categoryData.getFoodCategoryByName(categoryName);

        for (FoodCategory a : foodCategoryList) {
            String categoryName = a.getFoodName();
            String categoryImage = a.getFoodImage();
            categoryList.add(new FoodListHelper(categoryName, ImageID(categoryImage)));
        }

        categoryData.close();


        foodListAdapter = new FoodListAdapter(categoryList, this);

        categoryListView.setAdapter(foodListAdapter);
        categoryListView.setClickable(true);

        categoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemNumber, long l) {
                Object obj = categoryListView.getAdapter().getItem(itemNumber);
                final String foodName = foodCategoryList.get(itemNumber).getFoodName();
                Intent intent = new Intent(FoodListActivity.this, AddFoodToDiaryActivity.class);
                intent.putExtra("foodname", foodName);
                intent.putExtra("meal_type", mealType);
                startActivity(intent);

            }
        });

    }

    public int ImageID(String image_name) {
        int resID = this.getResources().getIdentifier(image_name, "drawable", getPackageName());
        return resID;
    }
}
