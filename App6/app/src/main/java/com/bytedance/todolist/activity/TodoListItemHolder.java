package com.bytedance.todolist.activity;

import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.todolist.R;
import com.bytedance.todolist.database.TodoListDao;
import com.bytedance.todolist.database.TodoListDatabase;
import com.bytedance.todolist.database.TodoListEntity;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wangrui.sh
 * @since Jul 11, 2020
 */
public class TodoListItemHolder extends RecyclerView.ViewHolder{
    ToFragmentListener toFragmentListener;

    private TextView mContent;
    private TextView mTimestamp;
    private CheckBox checkBox;
    private ImageView imageView;

    private Long id;
    private int done;

    public TodoListItemHolder(@NonNull final View itemView) {
        super(itemView);
        toFragmentListener = (TodoListActivity)itemView.getContext();

        mContent = itemView.findViewById(R.id.tv_content);
        mTimestamp = itemView.findViewById(R.id.tv_timestamp);
        checkBox = itemView.findViewById(R.id.checkBox);
        imageView = itemView.findViewById(R.id.imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                toFragmentListener.onTypeClick(id);
            }
        });
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if ( isChecked ) {
                    mContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    mContent.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG);
                }
                mContent.invalidate();
                if (isChecked == (done==1)) return ;
                toFragmentListener.onChangeDone(id);
//                mContent.invalidate();
            }
        });
    }

    public void bind(TodoListEntity entity) {
        id = entity.getId();
        done = entity.getDone();
        mContent.setText(entity.getContent());
        if ( checkBox.isChecked() ) {
            mContent.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            mContent.invalidate();
//            mTimestamp.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        mTimestamp.setText(formatDate(entity.getTime()));
        checkBox.setChecked(entity.getDone() != 0);
    }

    private String formatDate(Date date) {
        DateFormat format = SimpleDateFormat.getDateInstance();
        return format.format(date);
    }
}
