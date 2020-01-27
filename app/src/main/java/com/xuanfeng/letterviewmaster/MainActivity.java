package com.xuanfeng.letterviewmaster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.xuanfeng.letterviewlib.LetterView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LetterView letterView = findViewById(R.id.letterView);
//        String[] ss = {"A","B","W","Z"};
//
//        letterView.setData(Arrays.asList(ss));
        letterView.setListener(new LetterView.LetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                Toast.makeText(MainActivity.this, letter, Toast.LENGTH_SHORT).show();
            }
        });
    }


}
