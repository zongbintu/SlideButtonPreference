package com.tu.preference.simple;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.tu.preference.SlideButtonPreference;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SlideButtonPreference sbp = (SlideButtonPreference) findViewById(R.id.slide_btn);

        sbp.setStateChanageListener(new SlideButtonPreference.StateChanageListener() {
            @Override
            public void stateChange(SlideButtonPreference sb) {
                Toast.makeText(MainActivity.this, "SlideButtonPreference is " + (sb.isOn() ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        });
        SlideButtonPreference switchp = (SlideButtonPreference) findViewById(R.id.switch_btn);
        switchp.setStateChanageListener(new SlideButtonPreference.StateChanageListener() {
            @Override
            public void stateChange(SlideButtonPreference sb) {
                Toast.makeText(MainActivity.this, "switch SlideButtonPreference is " + (sb.isOn() ? "on" : "off"), Toast.LENGTH_SHORT).show();
            }
        });

        Toast.makeText(MainActivity.this,"SlideButtonPreference is "+(sbp.isOn()?"on":"off\n")+"switch SlideButtonPreference is "+(switchp.isOn()?"on":"off"),Toast.LENGTH_LONG).show();
    }
}
