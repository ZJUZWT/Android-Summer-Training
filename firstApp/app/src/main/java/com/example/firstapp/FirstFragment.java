package com.example.firstapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class FirstFragment extends Fragment {

    Button button_next;
    EditText editText;
    ProgressBar progressBar;
    private final String TAG = "Log ";

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button_next = view.findViewById(R.id.button_first);
        button_next.setEnabled(false);
        progressBar = view.findViewById(R.id.progressBar);

        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(2 * 1000);
                            Intent intent = new Intent(getActivity(),InteractActivity.class);
                            startActivity(intent);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                Log.d(TAG, "onClick: 进入正式交互activity");
            }
        });

        view.findViewById(R.id.textview_first).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: 你点这玩意干嘛");
                Toast.makeText(getActivity(),"你点这玩意干嘛",Toast.LENGTH_SHORT).show();
            }
        });
        editText = view.findViewById(R.id.editTextTextPassword);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "beforeTextChanged: 修改文本 \"" + editText.getText() + "\"");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "beforeTextChanged: 正在修改文本 \"" + editText.getText() + "\"");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "afterTextChanged: 修改完毕成为 \"" + editText.getText() + "\"");
                if (editText.getText().toString().equals("ByteDance")) {
                    button_next.setEnabled(true);
                    Log.d(TAG, "afterTextChanged: 成功解锁");
                }
            }
        });
    }
}