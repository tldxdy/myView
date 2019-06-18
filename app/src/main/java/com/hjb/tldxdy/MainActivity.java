package com.hjb.tldxdy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hjb.tldxdy.view.ScoreProgressView;
import com.hjb.tldxdy.view.SinusoidalWaveView;
import com.hjb.tldxdy.view.TimeSetView;

public class MainActivity extends AppCompatActivity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        SinusoidalWaveView ripple_view = findViewById(R.id.ripple_view);
//
//        ripple_view.startAnimation();
//
//        TimeSetView tsv_view = findViewById(R.id.tsv_view);
//
//        tsv_view.setEndTime(0,500);
        ScoreProgressView spv = findViewById(R.id.spv);
        spv.setResIds_waves(getRes(151,"waves_00000"));

        spv.setResIds_bubble(getRes(50,"bubble",1));
        spv.setScore(25);
        spv.deployAnimation();
        spv.startAnimation();
    }
    private int[] getRes(int num,String nanme) {
        int[] resId = new int[num];
        for (int i = 0; i < num; i++) {
            StringBuilder sb = new StringBuilder(nanme);
            String Istr = String.valueOf(i);
            String str = sb.replace(nanme.length() - Istr.length(),nanme.length(),Istr).toString();
            resId[i] = getResources().getIdentifier(str, "mipmap", getBaseContext().getPackageName());
        }
        return resId;
    }

    private int[] getRes(int num,String nanme,int zero) {
        int[] resId = new int[num];
        for (int i = 0; i < num; i++) {
            String Istr = String.valueOf(i + zero);
            resId[i] = getResources().getIdentifier(nanme + Istr, "mipmap", getBaseContext().getPackageName());
        }
        return resId;
    }

}
