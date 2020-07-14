package com.bytedance.videoplayer;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

public class VideoActivity extends AppCompatActivity {
	boolean isPressed = false;

	MySurfaceView surfaceView;
	MediaPlayer mediaPlayer;
	SurfaceHolder surfaceHolder;

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

		surfaceView = findViewById(R.id.surfaceView);
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setDataSource(this,Uri.parse(url));
			surfaceHolder = surfaceView.getHolder();
			surfaceHolder.addCallback(new PlayerCallBack());

			int myVideoWidth = mediaPlayer.getVideoWidth();
			int myVideoHeight = mediaPlayer.getVideoHeight();

			float scale_width = surfaceView.getWidth()*1.0f/myVideoWidth;
			float scale_height = surfaceView.getHeight()*1.0f/myVideoHeight;

//			float scale_width = myVideoWidth*1.0f/surfaceView.getWidth();
//			float scale_height = myVideoHeight*1.0f/surfaceView.getHeight();

			if (scale_width<scale_height){
				float mVideoWidth = myVideoWidth*scale_width;
				float mVideoHeight = myVideoHeight*scale_width;
//						surfaceView.getHolder().setFixedSize((int)mVideoWidth,surfaceView.getWidth());
//						surfaceView.setMeasure(mVideoWidth,surfaceView.getHeight());
//						surfaceView.getHolder().setFixedSize((int)mVideoWidth,(int)(mVideoHeight));
						surfaceView.setMeasure(mVideoWidth,mVideoHeight);
//				surfaceView.setMeasure(myVideoWidth,myVideoHeight);
				surfaceView.requestLayout();
			} else {
				float mVideoWidth = myVideoWidth*scale_height;
				float mVideoHeight = myVideoHeight*scale_height;
//						surfaceView.getHolder().setFixedSize((int)mVideoWidth,surfaceView.getHeight());
//						surfaceView.setMeasure(mVideoWidth,surfaceView.getHeight());
//						surfaceView.getHolder().setFixedSize((int)mVideoWidth,(int)(mVideoHeight));
						surfaceView.setMeasure(mVideoWidth,mVideoHeight);
//				surfaceView.setMeasure(myVideoWidth,myVideoHeight);
				surfaceView.requestLayout();
			}

			mediaPlayer.prepare();
			mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
				@Override
				public void onPrepared(MediaPlayer mp) {

//					mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);

					mediaPlayer.start();
//					mediaPlayer.setLooping(true);
				}
			});

		} catch (IOException e) {
			e.printStackTrace();
		}

		surfaceView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isPressed) {
					isPressed = true;

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Thread.sleep(500);
							} catch (Exception e) {
								e.printStackTrace();
							} finally {
								isPressed = false;
							}
						}
					}).start();
				} else {
					if ( mediaPlayer.isPlaying() ) mediaPlayer.pause(); else mediaPlayer.start();
				}
			}
		});

//		surfaceView.setOn
	}

	private class PlayerCallBack implements SurfaceHolder.Callback {
		@Override
		public void surfaceCreated(SurfaceHolder holder) {
			mediaPlayer.setDisplay(holder);
		}
		@Override
		public void surfaceDestroyed(SurfaceHolder holder) {

		}
		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

		}
	}
}
