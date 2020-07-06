package com.example.firstapp;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class InteractActivity extends AppCompatActivity implements View.OnClickListener {

    private ToFragmentListener toFragmentListener;
    private static final String TAG = "Log ";
    private Button button_tri;
    private Button button_squ;
    private SeekBar seekBar_size;
    private CheckBox lock_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("OPENGL TEST");
        setContentView(R.layout.interact_activity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toFragmentListener = (SecondFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
        button_tri = (Button) findViewById(R.id.button_tri);
        button_squ = (Button) findViewById(R.id.button_squ);
        seekBar_size = (SeekBar) findViewById(R.id.seekBar);
        lock_size = (CheckBox) findViewById(R.id.checkBox);

        button_tri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != toFragmentListener) toFragmentListener.onTypeClick("tri");
                Log.d(TAG, "你的button选择: 三角形");
            }
        });
        button_squ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != toFragmentListener) toFragmentListener.onTypeClick("squ");
                Log.d(TAG, "你的button选择: 正方形");
            }
        });
        seekBar_size.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "onProgressChanged: 正在修改滑竿");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStartTrackingTouch: 正在触摸滑竿");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "onStopTrackingTouch: 停止触摸滑竿");
            }
        });
        lock_size.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                seekBar_size.setEnabled(!isChecked);
                if ( isChecked )
                    Log.d(TAG, "onCheckedChanged: 禁用滑竿");
                else
                    Log.d(TAG, "onCheckedChanged: 启用滑竿");
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_tri:
                if(null != toFragmentListener) toFragmentListener.onTypeClick("tri");
                Log.d(TAG, "你的button选择: 三角形");
                break;
            case R.id.button_squ:
                if(null != toFragmentListener) toFragmentListener.onTypeClick("squ");
                Log.d(TAG, "你的button选择: 正方形");
                break;
        }
    }

}
