package com.example.app2.Chat;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.app2.R;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
	private List<Chat> mChatList ;
	private static final String TAG = "ChatAdapter";

	static class ViewHolder extends RecyclerView.ViewHolder {
		ImageView left_icon,right_icon ;
		TextView left_sentence,right_sentence ;
		LinearLayout leftLayout ;
		LinearLayout rightLayout ;

		public ViewHolder(View view) {
			super( view ) ;
			left_icon = (ImageView) view.findViewById(R.id.left_chat_icon) ;
			left_sentence = (TextView) view.findViewById(R.id.left_chat_text) ;
			right_icon = (ImageView) view.findViewById(R.id.right_chat_icon) ;
			right_sentence = (TextView) view.findViewById(R.id.right_chat_text) ;
			leftLayout = (LinearLayout) view.findViewById(R.id.left_layout) ;
			rightLayout = (LinearLayout) view.findViewById(R.id.right_layout) ;
		}
	}

	public ChatAdapter(List<Chat> chatList) { mChatList = chatList ; }

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from( parent.getContext() ).inflate( R.layout.chat_item_reverse , parent , false ) ;
		ViewHolder holder = new ViewHolder(view) ;
		return holder ;
	}

	@Override
	public  void onBindViewHolder(ViewHolder holder,int position) {
		Chat chat = mChatList.get(position) ;
		if ( chat.getType() == chat.BOT )
		{
			holder.leftLayout.setVisibility(View.VISIBLE);
			holder.rightLayout.setVisibility(View.GONE);
			holder.left_icon.setImageResource(chat.getIconId());
			holder.left_sentence.setText(chat.getSentence());
		}
		else
		{
			holder.leftLayout.setVisibility(View.GONE);
			holder.rightLayout.setVisibility(View.VISIBLE);
			holder.right_icon.setImageResource(chat.getIconId());
			holder.right_sentence.setText(chat.getSentence());
		}
	}

	@Override
	public int getItemCount() {
		Log.d(TAG, "getItemCount: "+mChatList.size());
		return mChatList.size() ;
	}
}
