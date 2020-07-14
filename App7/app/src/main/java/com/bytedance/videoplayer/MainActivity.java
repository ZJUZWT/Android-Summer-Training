package com.bytedance.videoplayer;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImageView imageView = findViewById(R.id.imageView);
//        String url = "https://s3.pstatp.com/toutiao/static/img/logo.271e845.png";
        String url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1594721867182&di=c9b59073929fa1702e02a7cb1bc0136b&imgtype=0&src=http%3A%2F%2Fyouimg1.c-ctrip.com%2Ftarget%2Ffd%2Ftg%2Fg6%2FM04%2F4B%2F2E%2FCggYslazWbSACGqpAB4jv9jFFfU496.jpg";
//        String url = "https://github.com/ChadCSong/ShineButton/blob/master/image/demo_more_shine.gif";  //github加载不出来，可以看见error的效果

        DrawableCrossFadeFactory drawableCrossFadeFactory = new DrawableCrossFadeFactory.Builder(300).setCrossFadeEnabled(true).build();
        Glide.with(this).load(url)
                .placeholder(R.drawable.loading)
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
                .error(R.drawable.wrong)
                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
//                .transform(new GlideCircleTransform(this))
                .into(imageView);

        button = findViewById(R.id.button);
        editText = findViewById(R.id.textView);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VideoActivity.launch((MainActivity) v.getContext(),editText.getText().toString());
            }
        });

    }
}
