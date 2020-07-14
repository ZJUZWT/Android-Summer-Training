package com.bytedance.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
	public static void launch(Activity activity, String url) {
		Intent intent = new Intent(activity, VideoActivity.class);
		intent.putExtra("url", url);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		String url = getIntent().getStringExtra("url");
		if ( url == null ) url = getIntent().getData().toString();
		final VideoView videoView = findViewById(R.id.videoView);
//		final ProgressBar progressBar = findViewById(R.id.progress_bar);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(Uri.parse(url));
		videoView.requestFocus();
		videoView.start();

		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
//				mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
////					@Override
////					public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
////						//FixMe 获取视频资源的宽度
////						mVideoWidth = mp.getVideoWidth();
////						//FixMe 获取视频资源的高度
////						mVideoHeight = mp.getVideoHeight();
////
////						scale = (float) mVideoWidth / (float) mVideoHeight;
////						refreshPortraitScreen(showVideoHeight == 0 ? DensityUtil.dip2px(context, 300) : showVideoHeight);
////					}
////				});
//				mp.getVideoWidth();
//				mp.getVideoHeight();

//				videoView.set
			}
		});

	}
}
