package com.example.app2.Recycle2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.app2.R;
import com.example.app2.Recycle1.MyAdapter1;
import com.example.app2.Recycle1.TestData1;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder2> {     //一定记得继承的里面是自己的类型
	private List<TestData2> mDataSet = new ArrayList<>();                           //这就是我们的数据列表
	private IOnItemClickListenerDown mItemClickListener;

	public static class MyViewHolder2 extends RecyclerView.ViewHolder{               //定义自己的ViewHolder
		private ImageView ivIcon;
		private TextView tvTitle;	//定义的上面左右滑动的栏目，使用一张图片和一个文字的模式
		private View contentView;

		public MyViewHolder2(View v) {
			super(v);
			contentView = v ;
			ivIcon = v.findViewById(R.id.ivIcon);
			tvTitle = v.findViewById(R.id.tvTitle);		//初始化ViewHolder，将这里面添加上需要的部件，View的作用暂时不明显？
		}

		public void onBind(int position,TestData2 data) {	//绑定数据，前面只能算作容器，view什么的都是容器，而内容是string,icon什么的
			//虽然这里没有使用但是例程里面对现在position的调用还是比较有参考价值的
			//new StringBuilder().append(position).append(".  ").toString()
			ivIcon.setImageDrawable(data.icon);											//此处对图片的处理有待商榷
			tvTitle.setText(data.name);
		}
		public void setOnClickListener(View.OnClickListener listener) {			//设置两种监听模式，这个监听反馈应该在别的地方
			if (listener != null) {
				contentView.setOnClickListener(listener);
			}
		}
		public void setOnLongClickListener(View.OnLongClickListener listener) {
			if (listener != null) {
				contentView.setOnLongClickListener(listener);
			}
		}
	}

	public MyAdapter2(List<TestData2> mDataSet_) {                                  //构造函数只需要传递数据，只有数据能够初始化与改变
		mDataSet.addAll(mDataSet_);
	}

	public void setOnItemClickListener(IOnItemClickListenerDown listener) {				//给监听值
		mItemClickListener = listener;
	}

	public void addData(int position, TestData2 data) {								//添加新的data
		mDataSet.add(position, data);
		notifyItemInserted(position);												//notifyItem似乎是一个专门处理这个的类型
		if (position != mDataSet.size()) {
			notifyItemRangeChanged(position, mDataSet.size() - position);		//修改位置
		}
	}
	public void removeData(int position) {											//同上
		if (null != mDataSet && mDataSet.size() > position) {
			mDataSet.remove(position);
			notifyItemRemoved(position);
			if (position != mDataSet.size()) {
				notifyItemRangeChanged(position, mDataSet.size() - position);
			}
		}
	}

	@Override
	public MyAdapter2.MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
		return new MyViewHolder2(LayoutInflater.from(parent.getContext())
				.inflate(R.layout.recycler_down, parent, false));
	}

	@Override
	public void onBindViewHolder(MyViewHolder2 holder, final int position) {
		holder.onBind(position, mDataSet.get(position));
		holder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemCLickDown(position, mDataSet.get(position));
				}
			}
		});
		holder.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (mItemClickListener != null) {
					mItemClickListener.onItemLongCLickDown(position, mDataSet.get(position));
				}
				return false;
			}

		});
	}

	@Override
	public int getItemCount() {
		return mDataSet.size();
	}

	public interface IOnItemClickListenerDown {
		void onItemCLickDown(int position, TestData2 data);
		void onItemLongCLickDown(int position, TestData2 data);
	}
}

