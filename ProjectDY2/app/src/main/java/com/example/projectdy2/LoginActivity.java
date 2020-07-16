package com.example.projectdy2;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.DataBase.UserEntity;
import com.google.android.material.snackbar.Snackbar;

import java.util.Comparator;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

	private Button login;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		login = findViewById(R.id.login);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final EditText username = findViewById(R.id.username);
				final EditText password = findViewById(R.id.password);

				//TODO : check text filled
				if ( username.getText().toString().equals("") || password.getText().toString().equals("") ) {
					Snackbar.make(v, "用户名或密码不能为空", Snackbar.LENGTH_SHORT).show();
					return;
				}

				//TODO : check database
				new Thread() {
					@Override
					public void run() {
						QueryDao dao = MyDataBase.inst(LoginActivity.this).queryDao();

						List<UserEntity> list = dao.userInfo(username.getText().toString());
						if ( list.size() == 0 || !list.get(0).getPassword().equals(password.getText().toString()) ) {
							Snackbar.make(v, "用户名或密码错误", Snackbar.LENGTH_SHORT).show();
						} else {
							dao.login(username.getText().toString());
							Snackbar.make(v, "登录成功", Snackbar.LENGTH_SHORT).show();
							finish();
						}
					}
				}.start();
			}
		});
	}
}
