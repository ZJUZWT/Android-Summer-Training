package com.example.app2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.app2.Recycle1.LinearItemDecorationUp;
import com.example.app2.Recycle1.MyAdapter1;
import com.example.app2.Recycle1.TestData1;
import com.example.app2.Recycle1.TestDataSet1;
import com.example.app2.Recycle2.LinearItemDecorationDown;
import com.example.app2.Recycle2.MyAdapter2;
import com.example.app2.Recycle2.TestData2;
import com.example.app2.Recycle2.TestDataSet2;

public class MainActivity extends AppCompatActivity implements MyAdapter1.IOnItemClickListenerUp, MyAdapter2.IOnItemClickListenerDown {

    private RecyclerView upRV;
    private RecyclerView downRV;
    private MyAdapter1 upAdapter;
    private MyAdapter2 downAdapter;
    private LinearLayoutManager linearLayoutManager1;
    private GridLayoutManager gridLayoutManagerTest1;
    private LinearLayoutManager linearLayoutManager2;
    private GridLayoutManager gridLayoutManagerTest2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViewUp();
        initViewDown();

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

    private void initViewUp() {                     //初始化横向滚动条
        upRV = findViewById(R.id.upRV);
        upRV.setHasFixedSize(true);                 //还不知道这个参数的具体含义
        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(RecyclerView.HORIZONTAL);
        gridLayoutManagerTest1 = new GridLayoutManager(this,3);//同时测试一下这个girdmanager

        upAdapter = new MyAdapter1(TestDataSet1.getData(this));
        upAdapter.setOnItemClickListener(this);

        upRV.setLayoutManager(linearLayoutManager1);    //View是容器，容器需要一个Layout管理和数据，需要将层次分析清楚
        upRV.setAdapter(upAdapter);

        LinearItemDecorationUp itemDecoration = new LinearItemDecorationUp(Color.BLACK);
//        upRV.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        upRV.addItemDecoration(itemDecoration);
    }

    private void initViewDown() {
        downRV = findViewById(R.id.downRV);
        downRV.setHasFixedSize(true);                 //还不知道这个参数的具体含义
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);
        gridLayoutManagerTest2 = new GridLayoutManager(this,3);//同时测试一下这个girdmanager

        downAdapter = new MyAdapter2(TestDataSet2.getData(this));
        downAdapter.setOnItemClickListener(this);

        downRV.setLayoutManager(linearLayoutManager2);    //View是容器，容器需要一个Layout管理和数据，需要将层次分析清楚
        downRV.setAdapter(downAdapter);

        LinearItemDecorationDown itemDecoration = new LinearItemDecorationDown(Color.BLACK);
//        upRV.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        downRV.addItemDecoration(itemDecoration);
    }

    @Override
    public void onItemCLickUp(int position, TestData1 data) {
        Toast.makeText(MainActivity.this, "点击了第" + (position+1) + "条", Toast.LENGTH_SHORT).show();
        if (data.name.equals("test")) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_DIAL);
            startActivity(intent);
        } else
            upAdapter.addData(position + 1, new TestData1(getDrawable(R.drawable.dial), "test"));
    }

    @Override
    public void onItemLongCLickUp(int position, TestData1 data) {
        Toast.makeText(MainActivity.this, "长按了第" + position + "条", Toast.LENGTH_SHORT).show();
        upAdapter.removeData(position);
    }

    @Override
    public void onItemCLickDown(int position, TestData2 data) {
//        Toast.makeText(MainActivity.this, "点击了第" + position + "条", Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putInt("num",position+1);
        bundle.putString("name",data.name);

        Intent intent = new Intent(this,DialogActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
//        downAdapter.addData(position + 1, new TestData2(getDrawable(R.drawable.ic_action_name), "test"));
    }

    @Override
    public void onItemLongCLickDown(int position, TestData2 data) {
        Toast.makeText(MainActivity.this, "你就这么残忍么", Toast.LENGTH_SHORT).show();
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("提示");
//        builder.setMessage("确定删除聊天记录?");
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                upAdapter.removeData(position);
//                alertDialog.dismiss();
//            }
//        });
        //发现AlertDialog是异步的操作

        downAdapter.removeData(position);
    }
}