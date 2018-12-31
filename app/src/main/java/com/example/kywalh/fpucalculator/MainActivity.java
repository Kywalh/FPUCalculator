package com.example.kywalh.fpucalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private EditText mEditText_Carbs;
    private EditText mEditText_Fat;
    private EditText mEditText_Protein;
    private EditText mEditText_CR;


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



        mTextView_InsulinCarbs = findViewById(R.id.textview_InsulinCarbCalculated);
        mTextView_InsulinFatProtein = findViewById(R.id.textview_InsulinFPUCalculated);
        mTextView_EquivCarbs = findViewById(R.id.textview_EquivCarbsCalculated);
        mTextView_FP_duration = findViewById(R.id.textview_DurationCalculated);
        mTextView_TBR = findViewById(R.id.textview_EquivTBRvalue);


        mButtonCalculate = findViewById(R.id.button_calculate);


        mButtonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Verify inputs from user and act accordingly

                if (mEditText_Carbs.getText().toString().length() == 0) {
                    mEditText_Carbs.setText("0");
                }
                if (mEditText_Fat.getText().toString().length() == 0) {
                    mEditText_Fat.setText("0");
                }
                if (mEditText_Protein.getText().toString().length() == 0) {
                    mEditText_Protein.setText("0");
                }
                if (mEditText_CR.getText().toString().length() == 0) {
                    mEditText_CR.setText(String.valueOf(255)); // safety feature
                }



                int num2 = Integer.parseInt(mEditText_Carbs.getText().toString());
                int num3 = Integer.parseInt(mEditText_Fat.getText().toString());
                int num4 = Integer.parseInt(mEditText_Protein.getText().toString());
                int num5 = Integer.parseInt(mEditText_CR.getText().toString());




                /*Insulin for Carbs calculation based on Carbs divided by Carb Ratio */
                double rounded_Insulin_for_Carbs = Math.round((num2 * 20.0 / num5)) / 20.0; //rounding to 0.05
                String var0 = String.format("%.2f", rounded_Insulin_for_Carbs); // display with 2 digits
                mTextView_InsulinCarbs.setText(String.valueOf(var0));



                /*Calculation of FPU based on 9 x Fat + 4 x Protein
                 * See  (WPTS) or ‘Warsaw’ formula for explanations  */

                double FPU = ((double)num3 * 9 + (double)num4 * 4)  ;

                // Calculation of insulin needed for Fat and Protein
                double  InsulinforFP = (FPU / 100) * (10 / (double)num5); // scale the FPU with the Carb ratio
                double rounded_InsulinFatProtein = Math.round(InsulinforFP * 20.0) / 20.0; //rounding to 0.05
                String var1 = String.format("%.2f", rounded_InsulinFatProtein); // display with 2 digits
                mTextView_InsulinFatProtein.setText(var1);


                /*Calculation of equivalent carbs from Fat and Protein for AAPS users using eCarbs
                 * Solves Issue #5 Additional carbs instead of insulin amounts please
                 * See  (WPTS) or ‘Warsaw’ formula for explanations  */


                double CarbsFromFatProtein = Math.round(InsulinforFP * (int)num5 ) ;
                mTextView_EquivCarbs.setText(String.valueOf((long)CarbsFromFatProtein));


                /*Calculation of duration for the Fat & protein
                 * See  (WPTS) or ‘Warsaw’ formula for explanations  */

                double FPUduration = 0;
                if (InsulinforFP == 0 )
                    FPUduration = 0;                // 0 hours
                else if (InsulinforFP <= 1 )
                    FPUduration = 180;              // 3 hours
                else if (InsulinforFP <= 2 )
                    FPUduration = 240;              // 4 hours
                else if (InsulinforFP <= 3 )
                    FPUduration = 300;              // 5 hours
                else if (InsulinforFP > 3 )
                    FPUduration = 480;              // 8 hours
                else
                    FPUduration = 0;                // Default and safe 0 hours



                /* Display also the duration in Hours*/
                double DurationInHours = Math.round(FPUduration / 60);
                String var4 = String.valueOf((long)FPUduration) + " / " + String.valueOf((int)DurationInHours); /* correction of formatting hours for Duration */
                mTextView_FP_duration.setText(var4);


                /*Calculation of equivalent additional basal rate for the same duration */

                double TBRequiv = InsulinforFP / (FPUduration / 60);
                double rounded_TBRequiv = Math.round(TBRequiv * 20.0) / 20.0; //rounding to 0.05
                String var2 = String.format("%.2f", rounded_TBRequiv); // display with 2 digits
                mTextView_TBR.setText(String.valueOf(var2));




            }
        });

    }
}
