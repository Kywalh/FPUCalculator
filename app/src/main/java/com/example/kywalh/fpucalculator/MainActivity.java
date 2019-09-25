package com.example.kywalh.fpucalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private EditText mEditText_Carbs;
    private EditText mEditText_Fat;
    private EditText mEditText_Protein;
    private EditText mEditText_CR;
    private EditText mEditText_Ratio;

    private TextView mTextView_InsulinCarbs;
    private TextView mTextView_TBR;
    private TextView mTextView_InsulinFatProtein;
    private TextView mTextView_EquivCarbs;
    private TextView mTextView_FP_duration;

    private Button mButtonCalculate;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText_Carbs = findViewById(R.id.editText_Carbs);
        mEditText_Fat = findViewById(R.id.editText_Fat);
        mEditText_Protein = findViewById(R.id.editText_Protein);
        mEditText_CR = findViewById(R.id.editText_CarbRatio);
        mEditText_Ratio = findViewById(R.id.editText_Ratio);
        mEditText_Ratio.setText("100");

        mTextView_InsulinCarbs = findViewById(R.id.textview_InsulinCarbCalculated);
        mTextView_InsulinFatProtein = findViewById(R.id.textview_InsulinFPUCalculated);
        mTextView_EquivCarbs = findViewById(R.id.textview_EquivCarbsCalculated);
        mTextView_FP_duration = findViewById(R.id.textview_DurationCalculated);
        mTextView_TBR = findViewById(R.id.textview_EquivTBRvalue);


        mButtonCalculate = findViewById(R.id.button_calculate);


        mButtonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String strCarbs = (mEditText_Carbs).getText().toString();
                String strFat = (mEditText_Fat).getText().toString();
                String strProtein = (mEditText_Protein).getText().toString();
                String strCR = (mEditText_CR).getText().toString();
                String strRatio = (mEditText_Ratio).getText().toString();

                if (strCarbs.equals("") || strFat.equals("") || strProtein.equals("") || strCR.equals("") || strRatio.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.EmptyFieldsMessage, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 150);
                    toast.show();
                } else {
                    int numCarbs = Integer.parseInt(mEditText_Carbs.getText().toString());
                    int numFat = Integer.parseInt(mEditText_Fat.getText().toString());
                    int numProtein = Integer.parseInt(mEditText_Protein.getText().toString());
                    double numCR = Double.parseDouble(mEditText_CR.getText().toString());
                    int numRatio = Integer.parseInt(mEditText_Ratio.getText().toString());


                    //Verify inputs from user and act accordingly

                    if (mEditText_Carbs.getText().toString().length() == 0) {
                        mEditText_Carbs.setText("0");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultValuesLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numCarbs = 0;
                    }
                    if (mEditText_Fat.getText().toString().length() == 0) {
                        mEditText_Fat.setText("0");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultValuesLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numFat = 0;
                    }
                    if (mEditText_Protein.getText().toString().length() == 0) {
                        mEditText_Protein.setText("0");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultValuesLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numProtein = 0;
                    }
                    if (strCR.equals("0")) { //(mEditText_CR.getText().toString().length() == 0) ||
                        mEditText_CR.setText(String.valueOf(255)); // safety feature
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultValuesLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numCR = 255;
                    }

                    if (mEditText_Ratio.getText().toString().length() == 0) {
                        mEditText_Ratio.setText("100");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultValuesLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numRatio = 100;
                    }

                    if (numRatio < 20) {
                        mEditText_Ratio.setText("20");
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.DefaultMinimumSplitRatioLoadedToastMessage, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 150);
                        toast.show();
                        numRatio = 20;
                    }
                    if (numRatio > 100) {
                        mEditText_Ratio.setText("100");
                        numRatio = 100;
                    }


                    /*Insulin for Carbs calculation based on Carbs divided by Carb Ratio */
                    double Insulin_for_Carbs = (((double) numRatio * numCarbs) / ((double) numCR * 100.0));
                    double rounded_Insulin_for_Carbs = Math.round(Insulin_for_Carbs * 20.0) / 20.0; //rounding to 0.05
                    String var0 = String.format("%.2f", rounded_Insulin_for_Carbs); // display with 2 digits
                    mTextView_InsulinCarbs.setText(String.valueOf(var0));



                    /*Calculation of FPU based on 9 x Fat + 4 x Protein
                     * See  (WPTS) or ‘Warsaw’ formula for explanations  */

                    double FPU = ((double) numFat * 9 + (double) numProtein * 4);

                    // Calculation of insulin needed for Fat and Protein
                    double InsulinforFP = (100.0 - (double) numRatio) * numCarbs / ((double) numCR * 100.0) + (FPU / 100.0) * (10.0 / (double) numCR); // scale the FPU with the Carb ratio
                    double rounded_InsulinFatProtein = Math.round(InsulinforFP * 20.0) / 20.0; //rounding to 0.05
                    String var1 = String.format("%.2f", rounded_InsulinFatProtein); // display with 2 digits
                    mTextView_InsulinFatProtein.setText(var1);


                    /*Calculation of equivalent carbs from Fat and Protein for AAPS users using eCarbs
                     * Solves Issue #5 Additional carbs instead of insulin amounts please
                     * See  (WPTS) or ‘Warsaw’ formula for explanations  */


                    double CarbsFromFatProtein = Math.round(InsulinforFP * (double) numCR); /* correction of issue #11 about limitation to 255 linked to casting to int instead of long*/
                    mTextView_EquivCarbs.setText(String.valueOf((long) CarbsFromFatProtein));


                    /*Calculation of duration for the Fat & protein
                     * See  (WPTS) or ‘Warsaw’ formula for explanations  */

                    double FPUduration = 0;
                    if (InsulinforFP == 0)
                        FPUduration = 0;                // 0 hours
                    else if (InsulinforFP <= 1)
                        FPUduration = 180;              // 3 hours
                    else if (InsulinforFP <= 2)
                        FPUduration = 240;              // 4 hours
                    else if (InsulinforFP <= 3)
                        FPUduration = 300;              // 5 hours
                    else if (InsulinforFP > 3)
                        FPUduration = 480;              // 8 hours
                    else
                        FPUduration = 0;                // Default and safe 0 hours



                    /* Display also the duration in Hours*/
                    double DurationInHours = Math.round(FPUduration / 60);
                    String var4 = String.valueOf((long) FPUduration) + " / " + String.valueOf((int) DurationInHours); /* correction of formatting hours for Duration */
                    mTextView_FP_duration.setText(var4);


                    /*Calculation of equivalent additional basal rate for the same duration */

                    double TBRequiv = InsulinforFP / (FPUduration / 60);
                    double rounded_TBRequiv = Math.round(TBRequiv * 20.0) / 20.0; //rounding to 0.05
                    String var2 = String.format("%.2f", rounded_TBRequiv); // display with 2 digits
                    mTextView_TBR.setText(String.valueOf(var2));


                }
            }
        });

    }
}

