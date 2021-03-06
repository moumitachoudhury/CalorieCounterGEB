package userinfo;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.moumita.caloriecountergeb.R;

public class UserHeightInfoActivity extends AppCompatActivity {

    private boolean isFemale, isfeet = true;
    private double height;
    private Typeface mTfRegular, mtfBold;

    private String[] feetValues = new String[10];
    private String[] inchValues = new String[100];

    NumberPicker mFeetPicker, mInchPicker;
    TextView mHeightText, mFeetText, mInchText;
    ImageView mHeightImg;
    Button mNextPageBtn;
    ToggleButton mFeetvsInch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_height_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }


        for (int i = 0; i < 10; i++) {
            feetValues[i] = String.valueOf(i+1);
        }
        for (int i = 0; i < 100; i++) {
            inchValues[i] = String.valueOf(i);
        }

        mTfRegular = Typeface.createFromAsset(getAssets(), "OpenSans-Regular.ttf");
        mtfBold = Typeface.createFromAsset(getAssets(), "OpenSans-Bold.ttf");

        mHeightText = findViewById(R.id.height_info_text);
        mFeetText = findViewById(R.id.feet_text);
        mInchText = findViewById(R.id.inch_text);
        mHeightImg = findViewById(R.id.height_info_img);
        mNextPageBtn = findViewById(R.id.next_page_btn);

        mFeetPicker = findViewById(R.id.num_picker_feet);
        mFeetPicker.setMinValue(2);
        mFeetPicker.setMaxValue(8);
        mFeetPicker.setDisplayedValues(feetValues);
        mFeetPicker.setValue(4);

        mInchPicker = findViewById(R.id.num_picker_inch);
        mInchPicker.setMinValue(0);
        mInchPicker.setMaxValue(11);
        mInchPicker.setDisplayedValues(inchValues);

        mFeetvsInch = findViewById(R.id.feet_vs_inch);

        mHeightText.setTypeface(mTfRegular);
        mFeetText.setTypeface(mTfRegular);
        mInchText.setTypeface(mTfRegular);
        mNextPageBtn.setTypeface(mTfRegular);
        mFeetvsInch.setTypeface(mTfRegular);

        mFeetvsInch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mFeetText.setText("Feet");
                    mInchText.setText("Inches");
                    mFeetPicker.setMinValue(2);
                    mFeetPicker.setMaxValue(8);
                    mInchPicker.setMinValue(0);
                    mInchPicker.setMaxValue(11);
                    isfeet = true;
                } else {
                    mFeetText.setText("Meter");
                    mInchText.setText("Cm");
                    mFeetPicker.setMinValue(2);
                    mFeetPicker.setMaxValue(5);
                    mInchPicker.setMinValue(0);
                    mInchPicker.setMaxValue(99);

                    isfeet = false;

                }
            }
        });

        mNextPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isfeet) {
                    height = mFeetPicker.getValue() * 12;
                    height += mInchPicker.getValue();

                } else {
                    height = mFeetPicker.getValue() * 100;
                    height += mInchPicker.getValue();
                    height *= 0.393701;
                }

                Intent intent = new Intent(UserHeightInfoActivity.this, UserAgeInfoActivity.class);
                intent.putExtra("isfemale", isFemale);
                intent.putExtra("height", height);

                startActivity(intent);
                finish();
            }
        });
    }
}
