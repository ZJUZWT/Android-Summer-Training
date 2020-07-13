package com.bytedance.todolist.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.DateConverter;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TakeANote extends AppCompatActivity {

	Context context;
	Button button;
	EditText editText;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.take_a_note);

		button = findViewById(R.id.button);
		editText = findViewById(R.id.editTextTextPersonName);

		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				if ( editText.getText().toString().equals("") ) {
					Toast.makeText(TakeANote.this,"啥都没写确认啥呢",Toast.LENGTH_LONG).show();
					return;
				}
				Intent intent = new Intent();
//				intent.putExtra("id",);
				intent.putExtra("content",editText.getText().toString());
//				intent.putExtra("time", System.currentTimeMillis() );

				setResult(RESULT_OK,intent);
				finish();
			}
		});
	}
}
