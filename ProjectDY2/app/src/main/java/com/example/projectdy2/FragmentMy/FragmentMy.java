package com.example.projectdy2.FragmentMy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentMy extends Fragment {
	Button buttonLogin;
	Button buttonRegister;
	View layoutLogin;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		layoutLogin = view.findViewById(R.id.my_page_login);

		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				if ( query.hasLogin().size() == 0 ) {
//					view.setVisibility(View.GONE);
//					recyclerView.setVisibility(View.GONE);
					layoutLogin.setVisibility(View.VISIBLE);
				} else {
					layoutLogin.setVisibility(View.GONE);
				}

			}
		}.start();
	}
}
