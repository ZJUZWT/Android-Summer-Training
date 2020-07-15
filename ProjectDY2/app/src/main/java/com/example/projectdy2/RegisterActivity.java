package com.example.projectdy2;

import android.os.Bundle;
import android.text.InputFilter;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
	EditText editText;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		editText = findViewById(R.id.username);
		editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
	}
}
