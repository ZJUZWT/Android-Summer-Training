package com.example.app2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app2.Chat.Chat;
import com.example.app2.Chat.ChatAdapter;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DialogActivity extends AppCompatActivity {

	List<Chat> chatList = new ArrayList<>();

	public void initChat() {
		Intent intent = getIntent();

		Chat bot = new Chat( "Hi，我是你列表里面第"+intent.getIntExtra("num",0)+"个朋友哦。我的名字叫做"+intent.getStringExtra("name"), R.drawable.icon , 1 );
		chatList.add(bot) ;
	}

	private void new_chat_come(Chat chat, RecyclerView recyclerView, ChatAdapter adapter, EditText editText) {
		chatList.add(chat) ;
		adapter.notifyItemInserted( chatList.size()-1 );
		recyclerView.scrollToPosition( chatList.size()-1 );
		editText.setText("");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.chat_activity);
		overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out) ;

		initChat();
		final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_text) ;
		LinearLayoutManager layoutManager = new LinearLayoutManager(this ) ;
		recyclerView.setLayoutManager(layoutManager);
		final ChatAdapter adapter =new ChatAdapter(chatList) ;
		recyclerView.setAdapter(adapter);

		final EditText inputText = (EditText) findViewById(R.id.editText) ;
		Button send = (Button) findViewById(R.id.button_1) ;

		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String input = inputText.getText().toString() ;
				if ( !input.equals("") ) {
					Chat chat = new Chat( input , R.drawable.icon , -1 ) ;
					new_chat_come( chat , recyclerView , adapter , inputText ) ;

					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							Chat chat = new Chat( "Let me look look." , R.drawable.icon , 1 ) ;
							new_chat_come( chat , recyclerView , adapter , inputText ) ;
						}
					},500 ) ;
				}
				else
				{
					Chat chat = new Chat( "Trust me! You don't want to have a meaningless conversation." , R.drawable.icon , 1 ) ;
					new_chat_come( chat , recyclerView , adapter , inputText );
				}
			}
		});

		View viewGroup = getWindow().getDecorView();
		int num = countView(viewGroup);
		Toast.makeText(this,"当前页面一共有"+num+"个view",Toast.LENGTH_LONG).show();
	}

	private int countView(View view) {
		int count = 0 ;

		if (view instanceof ViewGroup) {
			ViewGroup viewGroup = (ViewGroup) view;

			for (int i = 0; i < viewGroup.getChildCount(); i++)
				count += countView(viewGroup.getChildAt(i));
		} else count++;

		return count;
	}


}
