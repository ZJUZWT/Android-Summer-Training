package com.example.projectdy2;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.DataBase.UserEntity;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
	Button register;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		register = findViewById(R.id.register);
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				final EditText username = findViewById(R.id.username);
				final EditText password = findViewById(R.id.password);
				final EditText rePassword = findViewById(R.id.password2);

				//TODO : check text filled
				if ( username.getText().toString().equals("") || password.getText().toString().equals("") || rePassword.getText().toString().equals("")) {
					Snackbar.make(v, "用户名或密码不能为空", Snackbar.LENGTH_SHORT).show();
					return;
				}

				//TODO : check database
				new Thread() {
					@Override
					public void run() {
						QueryDao dao = MyDataBase.inst(RegisterActivity.this).queryDao();

						List<UserEntity> list = dao.userInfo(username.getText().toString());
						if ( list.size() != 0 ) {
							Snackbar.make(v, "用户名已经存在，请更换", Snackbar.LENGTH_SHORT).show();
						} else if ( !password.getText().toString().equals(rePassword.getText().toString())) {
							Snackbar.make(v, "两次密码不相同，请修改", Snackbar.LENGTH_SHORT).show();
						}
						else {
							UserEntity userEntity = new UserEntity(username.getText().toString(),password.getText().toString(),1);
							dao.register( userEntity ) ;
//							dao.login(username.getText().toString());
							Snackbar.make(v, "注册成功", Snackbar.LENGTH_SHORT).show();
							finish();
						}
					}
				}.start();
			}
		});
	}
}
