package userinfo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.moumita.caloriecountergeb.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import generalpersonactivities.ShowBMRActivity;

import generalpersonactivities.BMICalculation;
import generalpersondatabase.Person;
import generalpersondatabase.PersonOperations;

public class UserWeightInfoActivity extends AppCompatActivity {
    private boolean isFemale, isfeet = true, iskg = true;
    private double height, weight, targetweight;
    private int age;
    private long intActivityLevel;
    private Person newPerson;
    private PersonOperations personData;
    private double BMRWithActivity, BMRWithoutActivity;

    EditText mWeightInputText, mTargetWeightText;
    TextView mWeightText, mKgText, mKgText2;
    Button mNextPageBtn;
    ToggleButton mlbsvsKg;
    private Typeface mTfRegular, mtfBold;
    private TextView activityLevelText, unitText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_weight_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        Bundle bundle = getIntent().getExtras();
        isFemale = bundle.getBoolean("isfemale");
        isfeet = bundle.getBoolean("isfeet");
        height = bundle.getDouble("height");
        age = bundle.getInt("age");


        mWeightInputText = findViewById(R.id.weight_editText_id);
        mTargetWeightText = findViewById(R.id.weight_editText_id2);
        mWeightText = findViewById(R.id.weight_info_text);
        activityLevelText = findViewById(R.id.activity_level_text);
        unitText = findViewById(R.id.kg_vs_lbs_text);
        /*mKgText = findViewById(R.id.kg_text);
        mKgText2 = findViewById(R.id.kg_text2);*/
        mNextPageBtn = findViewById(R.id.next_page_btn);

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mtfBold = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        mWeightText.setTypeface(mTfRegular);
        activityLevelText.setTypeface(mTfRegular);
        unitText.setTypeface(mTfRegular);


        newPerson = new Person();
        personData = new PersonOperations(this);
        personData.open();

        mlbsvsKg = findViewById(R.id.lbs_vs_kg);

        mNextPageBtn.setTypeface(mTfRegular);
        mlbsvsKg.setTypeface(mTfRegular);

        mlbsvsKg.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    iskg = true;

                } else {

                    iskg = false;

                }
            }
        });

        mNextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Activity level
                Spinner spinnerActivityLevel = findViewById(R.id.spinnerActivityLevel);
                //  0: Little to no exercise
                // 1: Light exercise (1–3 days per week)
                // 2: Moderate exercise (3–5 days per week)
                // 3: Heavy exercise (6–7 days per week)
                // 4: Very heavy exercise (twice per day, extra heavy workouts)
                intActivityLevel = spinnerActivityLevel.getSelectedItemPosition();

                final String weightstr = mWeightInputText.getText().toString();
                final String targetweightstr = mTargetWeightText.getText().toString();

                if (weightstr.isEmpty()) {
                    mWeightInputText.setError("enter current weight");
                    return;
                } else {
                    mWeightInputText.setError(null);
                }

                if (targetweightstr.isEmpty()) {
                    mTargetWeightText.setError("enter target weight");
                    return;
                } else {
                    mTargetWeightText.setError(null);
                }

                weight = Double.parseDouble(weightstr);
                targetweight = Double.parseDouble(targetweightstr);

                if (iskg) {
                } else {
                    weight *= 0.454;
                    targetweight *= 0.454;
                }


                height = BMICalculation.Round(height, 2);
                weight = BMICalculation.Round(weight, 2);
                targetweight = BMICalculation.Round(targetweight, 2);

                String BMRHeight, BMRWeight, BMRAge, BMRGender = "";
                BMRHeight = Double.toString(height * 2.54);
                BMRWeight = Double.toString(targetweight);
                BMRAge = Double.toString(age);
                if (isFemale) BMRGender += "female";
                else BMRGender += "male";
                BMRWithoutActivity = BMICalculation.BMRWithoutActivity(BMRHeight, BMRWeight, BMRAge, BMRGender);
                BMRWithActivity = BMICalculation.BMRWithActivity(BMRHeight, BMRWeight, BMRAge, BMRGender, intActivityLevel);

                String current_date_str = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                //push targetweight and intActivityLevel into person table.
                newPerson.setAge(String.valueOf(age));
                if (isFemale) newPerson.setGender("female");
                else newPerson.setGender("male");
                newPerson.setHeight(String.valueOf(height));
                newPerson.setWeight(String.valueOf(weight));
                newPerson.setActivityLevel(intActivityLevel);
                newPerson.setTargetWeight(String.valueOf(targetweight));
                newPerson.setBMRWithoutActivity(String.valueOf(BMRWithoutActivity));
                newPerson.setBMRWithActivity(String.valueOf(BMRWithActivity));
                newPerson.setWeightUpdateAmount("0");
                newPerson.setWeightUpdateDate(current_date_str);

                personData.addPerson(newPerson);

                personData.close();

                Intent intent = new Intent(UserWeightInfoActivity.this, ShowBMRActivity.class);
                intent.putExtra("withoutactivity", BMRWithoutActivity);
                intent.putExtra("withactivity", BMRWithActivity);

                startActivity(intent);
                finish();
            }
        });
    }
}
