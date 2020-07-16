package com.example.projectdy2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.example.projectdy2.DataBase.FavorRelation;
import com.example.projectdy2.DataBase.LikeRelation;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.DataBase.UserEntity;
import com.example.projectdy2.Util.StatusBarUtils;
import com.example.projectdy2.View.ShineButtonView;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class VideoActivity extends AppCompatActivity {
	String url;
	ShineButtonView likeButton;
	ShineButtonView favorButton;

	String TAG = "测试尺寸" ;

	public static void launch(Activity activity, String url) {
		Intent intent = new Intent(activity, VideoActivity.class);
		intent.putExtra("url", url);
		activity.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StatusBarUtils.setColor(this,0xFF000000);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video);
		url = getIntent().getStringExtra("url");

		final VideoView videoView = findViewById(R.id.video_container);
		final ProgressBar progressBar = findViewById(R.id.progress_bar);
		videoView.setMediaController(new MediaController(this));
		videoView.setVideoURI(Uri.parse(url));
//		videoView.requestFocus();
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				progressBar.setVisibility(View.GONE);
				int width = mp.getVideoWidth();
				int height = mp.getVideoHeight();
				Log.d(TAG, "onCreate: " + "宽:"+width + " 高:" + height);
				ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)videoView.getLayoutParams();
				layoutParams.height = layoutParams.width*height/width;
				videoView.setLayoutParams(layoutParams);
			}
		});
		videoView.start();
//		Log.d(TAG, "onCreate: " + "宽:"+width + " 高:" + height);
//		videoView.

		progressBar.setVisibility(View.VISIBLE);

		likeButton = findViewById(R.id.videoLikeButton);
		initLikeButton();
		favorButton = findViewById(R.id.videoFavorButton);
		initFavorButton();
		likeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {

				new Thread() {
					@Override
					public void run() {
						QueryDao query = MyDataBase.inst(VideoActivity.this).queryDao();
						if ( query.hasLogin().size() == 0 ) {
							Snackbar.make(v,"未登录无法使用", Snackbar.LENGTH_SHORT).show();
						} else {
							//TODO：数据库更新
							if ( likeButton.getDrawableId() == R.drawable.like_button ) {
								query.addLike(new LikeRelation(query.hasLogin().get(0).getId(),url));

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										//TODO:动画
										ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(likeButton, "scaleX", 1.0f, 0f);
										enlargeXAnimatorRecover.setDuration(500);
										enlargeXAnimatorRecover.setInterpolator(new AnticipateInterpolator());
										ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(likeButton, "scaleY", 1.0f, 0f);
										enlargeYAnimatorRecover.setDuration(500);
										enlargeYAnimatorRecover.setInterpolator(new AnticipateInterpolator());

										AnimatorSet animatorSet = new AnimatorSet();
										animatorSet.playTogether(enlargeXAnimatorRecover, enlargeYAnimatorRecover);
										animatorSet.start();

										new Handler().postDelayed(new Runnable() {
											public void run() {
												likeButton.loadImage(R.drawable.like_button_press);

												ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(likeButton, "scaleX", 0f, 1f);
												enlargeXAnimatorRecover.setDuration(500);
												enlargeXAnimatorRecover.setInterpolator(new OvershootInterpolator());
												ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(likeButton, "scaleY", 0f, 1f);
												enlargeYAnimatorRecover.setDuration(500);
												enlargeYAnimatorRecover.setInterpolator(new OvershootInterpolator());

												AnimatorSet animatorSet = new AnimatorSet();
												animatorSet.playTogether(enlargeXAnimatorRecover, enlargeYAnimatorRecover);
												animatorSet.start();
											}
										}, 500);
									}
								});
							} else {
								query.delLike(query.hasLogin().get(0).getId(),url);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										likeButton.loadImage(R.drawable.like_button);
									}
								});
							}
						}
					}
				}.start();
			}
		});

		favorButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
//				Log.d(TAG, "onClick: 按键");

				new Thread() {
					@Override
					public void run() {
						QueryDao query = MyDataBase.inst(VideoActivity.this).queryDao();
						if ( query.hasLogin().size() == 0 ) {
							Snackbar.make(v,"未登录无法使用", Snackbar.LENGTH_SHORT).show();
						} else {
							//TODO：数据库更新

							if ( favorButton.getDrawableId() == R.drawable.favor_button ) {
								//TODO:动画
								query.addFavor(new FavorRelation(query.hasLogin().get(0).getId(),url));

								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(favorButton, "scaleX", 1.0f, 0f);
										enlargeXAnimatorRecover.setDuration(500);
										enlargeXAnimatorRecover.setInterpolator(new AnticipateInterpolator());
										ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(favorButton, "scaleY", 1.0f, 0f);
										enlargeYAnimatorRecover.setDuration(500);
										enlargeYAnimatorRecover.setInterpolator(new AnticipateInterpolator());

										AnimatorSet animatorSet = new AnimatorSet();
										animatorSet.playTogether(enlargeXAnimatorRecover,enlargeYAnimatorRecover);
										animatorSet.start();

										new Handler().postDelayed(new Runnable(){
											public void run(){
												favorButton.loadImage(R.drawable.favor_button_press);

												ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(favorButton, "scaleX", 0f, 1f);
												enlargeXAnimatorRecover.setDuration(500);
												enlargeXAnimatorRecover.setInterpolator(new OvershootInterpolator());
												ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(favorButton, "scaleY", 0f, 1f);
												enlargeYAnimatorRecover.setDuration(500);
												enlargeYAnimatorRecover.setInterpolator(new OvershootInterpolator());

												AnimatorSet animatorSet = new AnimatorSet();
												animatorSet.playTogether(enlargeXAnimatorRecover,enlargeYAnimatorRecover);
												animatorSet.start();
											}
										},500);
									}
								});
							} else {
								query.delFavor(query.hasLogin().get(0).getId(),url);
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										favorButton.loadImage(R.drawable.favor_button);
									}
								});
							}
						}

					}
				}.start();
			}
		});
	}

	private void initLikeButton() {
		//TODO 查询当前状态
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(VideoActivity.this).queryDao();
				List<UserEntity> temp = query.hasLogin();


				if ( temp.size() == 0 ) { //没有登陆
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							likeButton.loadImage(R.drawable.like_button);
						}
					});
				} else {
					final List<LikeRelation> temp2 = query.isLike(temp.get(0).getId(),url);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (temp2.size() == 0)
								likeButton.loadImage(R.drawable.like_button);
							else
								likeButton.loadImage(R.drawable.like_button_press);
						}
					});
				}
			}
		}.start();
	}

	private void initFavorButton() {
		//TODO 查询当前状态
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(VideoActivity.this).queryDao();
				List<UserEntity> temp = query.hasLogin();

				if ( temp.size() == 0 ) { //没有登陆
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							favorButton.loadImage(R.drawable.favor_button);
						}
					});
				} else {
					final List<FavorRelation> temp2 = query.isFavor(temp.get(0).getId(),url);
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if (temp2.size() == 0)
								favorButton.loadImage(R.drawable.favor_button);
							else
								favorButton.loadImage(R.drawable.favor_button_press);
						}
					});
				}
			}
		}.start();
	}

}
