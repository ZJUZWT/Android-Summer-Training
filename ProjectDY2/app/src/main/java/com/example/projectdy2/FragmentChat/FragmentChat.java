package com.example.projectdy2.FragmentChat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projectdy2.DialogActivity;
import com.example.projectdy2.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class FragmentChat extends Fragment implements FragmentChatAdapter.IOnItemClickListenerDown{
	private RecyclerView downRV;
	private FragmentChatAdapter downAdapter;
	private LinearLayoutManager linearLayoutManager1;
	private GridLayoutManager gridLayoutManagerTest1;
	private LinearLayoutManager linearLayoutManager2;
	private GridLayoutManager gridLayoutManagerTest2;


	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_chat,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		downRV = view.findViewById(R.id.downRV);
		downRV.setHasFixedSize(true);
		linearLayoutManager2 = new LinearLayoutManager(view.getContext());
		linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);

		downAdapter = new FragmentChatAdapter(TestDataSet.getData(view.getContext()));
		downAdapter.setOnItemClickListener(this);

		downRV.setLayoutManager(linearLayoutManager2);    //View是容器，容器需要一个Layout管理和数据，需要将层次分析清楚
		downRV.setAdapter(downAdapter);

		LinearItemDecorationDown itemDecoration = new LinearItemDecorationDown(Color.GRAY);
//        upRV.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
		downRV.addItemDecoration(itemDecoration);
	}

	@Override
	public void onItemCLickDown(int position, FragmentChatData data) {
		Bundle bundle = new Bundle();
		bundle.putInt("num",position+1);
		bundle.putString("name",data.name);

		Intent intent = new Intent(getContext(), DialogActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onItemLongCLickDown(int position, FragmentChatData data) {

	}
}

