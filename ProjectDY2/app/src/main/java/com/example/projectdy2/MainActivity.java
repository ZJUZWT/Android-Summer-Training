package com.example.projectdy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.Fragment.FragmentList;
import com.example.projectdy2.FragmentChat.FragmentChat;
import com.example.projectdy2.FragmentMainPage.FragmentMainPage;
import com.example.projectdy2.FragmentMy.FragmentMy;
import com.example.projectdy2.InterfaceForInteract.BringFrontWaveButton;
import com.example.projectdy2.InterfaceForInteract.RefreshList;
import com.example.projectdy2.InterfaceForInteract.showLikePage;
import com.example.projectdy2.InterfaceForInteract.showRecommendPage;
import com.example.projectdy2.Util.StatusBarUtils;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.View.WaveButtonView;

import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.Video;

//网络
import java.util.ArrayList;
import java.util.List;

import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements BringFrontWaveButton {

	private String TAG = "测试";

	private boolean isFetchData = false;
	private boolean isQuit = false;

	private showRecommendPage showRecommendPage;
	private showLikePage showLikePage;

	private WaveButtonView waveButtonView1;
	private WaveButtonView waveButtonView2;
	private WaveButtonView waveButtonView4;
	private WaveButtonView waveButtonView5;
	private WaveButtonView pressedButton;
	private Handler waveButtonHandler = new Handler() ;
	private float waveProgress = 0.f;
	private float waveIncrement = 0.02f;

	private ImageButton midButton;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private Fragment nowFragment = new Fragment();
	private Fragment fragmentMainPage = new FragmentMainPage();
	private Fragment fragmentList = new FragmentList();
	private Fragment fragmentChat = new FragmentChat();
	private Fragment fragmentMy = new FragmentMy();

	private LinearLayout linearLayout;
	private FrameLayout frameLayout;
	private View contentViewGroup;

	//视频网络数据
	public Uri mSelectedImage;
	private Uri mSelectedVideo;
	private List<Video> mVideos = new ArrayList<>();
	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(IMiniDouyinService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		StatusBarUtils.setColor(this,getColor(R.color.colorLightThemeBlue));
//		StatusBarUtils.setColor(this,getColor(R.color.MainPageTitleBar));
		StatusBarUtils.setColor(this,0xFF000000);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		showRecommendPage = (showRecommendPage) fragmentMainPage;
		showLikePage = (showLikePage) fragmentMainPage;
//		StatusBarUtils.setStatusBarFullTransparent(this);
//		StatusBarUtils.setFitSystemWindow(contentViewGroup,false,this);
		//TODO:
		// 网络交互提取视频信息
		// 这个要在fragment出现前执行
//		initVideo();

		//TODO:
		// fragment的处理
		initFragment();

		//TODO:
		// 1：实现下面所有的WaveButton
		// 2：利用多线程处理下面4个WaveButton的关系
		initWaveButton();

		//TODO:
		// 视频跳转
		initMidButton();
	}

	private void initVideo() {
		miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
			@Override
			public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
				if (response.body() != null && response.body().videos != null) {
					mVideos = response.body().videos;
//					isFetchData = true;
//					setData
				}
			}
			@Override
			public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
				Toast.makeText(MainActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
	public List<Video> getVideo() { return mVideos; }
	private void initFragment() {
		linearLayout = findViewById(R.id.linearLayout);
		frameLayout = findViewById(R.id.replace);
		fragmentManager = getSupportFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.replace,fragmentMainPage,"FragmentMainPage").show(fragmentMainPage).commit();
		nowFragment = fragmentMainPage;
	}
	private void initWaveButton() {
		waveButtonView1 = findViewById(R.id.WaveButton1);
		waveButtonView2 = findViewById(R.id.WaveButton2);
		waveButtonView4 = findViewById(R.id.WaveButton4);
		waveButtonView5 = findViewById(R.id.WaveButton5);
		pressedButton = waveButtonView1;
		WaveButtonView.OnClickListener onClickListener = new WaveButtonView.OnClickListener() {
			@Override
			public void onClick(View v) {
				WaveButtonView waveButtonView = (WaveButtonView) v;
				if ( waveButtonView.getIsPressed() ) {
					if (waveButtonView1.equals(pressedButton)) {
						RefreshList refreshList = (RefreshList) fragmentMainPage;
						refreshList.refresh();
					}
					if (waveButtonView2.equals(pressedButton)) {
						RefreshList refreshList = (RefreshList) fragmentList;
						refreshList.refresh();
					}

					return ;
				}
				pressedButton.setIsPressed(false);
				waveButtonView.setIsPressed(true);

				pressedButton.invalidate();
				waveButtonView.invalidate();

				pressedButton = waveButtonView;

				if (waveButtonView1.equals(pressedButton))
					fragment_replace(fragmentMainPage);
				if (waveButtonView2.equals(pressedButton))
					fragment_replace(fragmentList);
				if (waveButtonView4.equals(pressedButton))
					fragment_replace(fragmentChat);
				if (waveButtonView5.equals(pressedButton))
					fragment_replace(fragmentMy);
			}
		};
		waveButtonView1.setOnClickListener(onClickListener);
		waveButtonView2.setOnClickListener(onClickListener);
		waveButtonView4.setOnClickListener(onClickListener);
		waveButtonView5.setOnClickListener(onClickListener);

		final Runnable temp = new Runnable() {
			@Override
			public void run() {
				try{
					pressedButton.setWaveProgress(waveProgress);
					waveProgress += waveIncrement;
					if ( waveProgress >= 2 ) waveProgress -= 2 ;
					waveButtonHandler.postDelayed(this,10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		waveButtonHandler.postDelayed(temp,10);

		linearLayout = findViewById(R.id.linearLayout);
	}
	private void initMidButton() {
		midButton = findViewById(R.id.MidButton);
		midButton.setOnTouchListener(new ButtonListener());
	}

	private void fragment_replace(Fragment fragment) {
		if (fragment != nowFragment) {
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);


			if ( nowFragment instanceof FragmentMainPage ) {
				showLikePage.pauseLikePage();
				showRecommendPage.pauseRecommendPage();
			}

			if (!fragment.isAdded())
				fragmentTransaction.hide(nowFragment).add(R.id.replace, fragment).commit();
			else {
				if ( nowFragment instanceof FragmentMy ) {
					LottieAnimationView lottieAnimationView = nowFragment.getView().findViewById(R.id.lottie_login);
					lottieAnimationView.pauseAnimation();
				}
				if ( fragment instanceof FragmentMy ) {
					LottieAnimationView lottieAnimationView = fragment.getView().findViewById(R.id.lottie_login);
					lottieAnimationView.playAnimation();
				}
				if ( fragment instanceof FragmentMainPage ) {
					showLikePage.showLikePage();
					showRecommendPage.showRecommendPage();
//					viewPager.playAnimation();
				}
				fragmentTransaction.hide(nowFragment).show(fragment).commit();
			}
			nowFragment = fragment;
		}
	}

	@Override
	public void onBackPressed() {
		if (!isQuit) {
			Toast.makeText(
					MainActivity.this,
					"再次点击返回退出",
					Toast.LENGTH_SHORT
			).show();
			isQuit = true;

			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						isQuit = false;
					}
				}
			}).start();
		} else
			finish();
	}

	@Override
	public void bringAllUp() {
		linearLayout.bringToFront();
	}

	private class ButtonListener implements View.OnTouchListener {
		boolean touchFlag;
		AnimatorSet animatorSet = new AnimatorSet();
		float x = -1;
		float y = -1;
		float lastX = -1;
		float lastY = -1;
		int height;
		int progressInColor = 0 ;
		ValueAnimator colorAnimator;

		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			final ImageButton button = v.findViewById(R.id.MidButton);

			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				ConstraintLayout.LayoutParams layoutParams= (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
				ConstraintLayout.LayoutParams sampleParams= (ConstraintLayout.LayoutParams) frameLayout.getLayoutParams();
				height = layoutParams.height;
				layoutParams.height = sampleParams.height;
				linearLayout.setLayoutParams(layoutParams);

				colorAnimator = ValueAnimator.ofInt(0,180);
				colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int animatedValue = (int) animation.getAnimatedValue() ;
						progressInColor = animatedValue;
						animatedValue <<= 24 ;
						linearLayout.setBackgroundColor(animatedValue);
					}
				});
				colorAnimator.setDuration(500).start();

				if ( x == -1 ) {
					x = button.getX(); y = button.getY();
				}
				lastX = event.getRawX() ; lastY = event.getRawY();

				animatorSet.end();
				touchFlag = false ;

				animatorSet = new AnimatorSet();
				ObjectAnimator shakeAnimator = ObjectAnimator.ofFloat(button,"rotation",-10,10) ;
				shakeAnimator.setDuration(50);
				shakeAnimator.setInterpolator(new LinearOutSlowInInterpolator());
				shakeAnimator.setRepeatCount(ValueAnimator.INFINITE);
				shakeAnimator.setRepeatMode(ValueAnimator.REVERSE);

				ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(button,"color",1f) ;
				alphaAnimator.setDuration(100);
				ObjectAnimator enlargeXAnimator = ObjectAnimator.ofFloat(button,"scaleX" , 1.0f,2f);
				enlargeXAnimator.setDuration(100);
				ObjectAnimator enlargeYAnimator = ObjectAnimator.ofFloat(button,"scaleY" , 1.0f,2f);
				enlargeYAnimator.setDuration(100);

				animatorSet.playTogether(shakeAnimator,enlargeXAnimator,enlargeYAnimator,alphaAnimator);
				animatorSet.start();
			}

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				if ( x == -1 ) return false;
				touchFlag = true;
				button.setX(button.getX()+(event.getRawX()-lastX));
				button.setY(button.getY()+(event.getRawY()-lastY));
				lastX = event.getRawX();
				lastY = event.getRawY();
				Log.d(TAG, "onTouch: " + "lastY : " + lastY);
			}

			if (event.getAction() == MotionEvent.ACTION_UP) {
				colorAnimator.end();
				colorAnimator = ValueAnimator.ofInt(progressInColor,0);
				colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int animatedValue = (int) animation.getAnimatedValue() ;
						progressInColor = animatedValue;
						animatedValue <<= 24 ;
						linearLayout.setBackgroundColor(animatedValue);

						if ( animatedValue == 0 ) {
							ConstraintLayout.LayoutParams layoutParams= (ConstraintLayout.LayoutParams) linearLayout.getLayoutParams();
							layoutParams.height = height;
							linearLayout.setLayoutParams(layoutParams);

							if ( lastY < 1750 ) {
								Intent intent = new Intent(v.getContext(),RecordActivity.class);
								startActivity(intent);
							}
						}
					}
				});
				colorAnimator.setDuration(progressInColor*500/128).start();

				animatorSet.end();
				animatorSet = new AnimatorSet();

				ObjectAnimator shakeAnimatorRecover = ObjectAnimator.ofFloat(button, "rotation", button.getRotation(), 0);
				shakeAnimatorRecover.setDuration(50);

				ObjectAnimator alphaAnimatorRecover = ObjectAnimator.ofFloat(button, "alpha", 1.0f);
				alphaAnimatorRecover.setDuration(100);
				ObjectAnimator enlargeXAnimatorRecover = ObjectAnimator.ofFloat(button, "scaleX", 2f, 1.0f);
				enlargeXAnimatorRecover.setDuration(100);
				ObjectAnimator enlargeYAnimatorRecover = ObjectAnimator.ofFloat(button, "scaleY", 2f, 1.0f);
				enlargeYAnimatorRecover.setDuration(100);

				ObjectAnimator translationXAnimation = ObjectAnimator.ofFloat(button,"translationX" , 0);
				translationXAnimation.setDuration(500);
				ObjectAnimator translationYAnimation = ObjectAnimator.ofFloat(button,"translationY" , 0);
				translationYAnimation.setDuration(500);

				animatorSet.playTogether(shakeAnimatorRecover,enlargeXAnimatorRecover,enlargeYAnimatorRecover,alphaAnimatorRecover,translationXAnimation,translationYAnimation);
//				animatorSet.playTogether(shakeAnimatorRecover, enlargeXAnimatorRecover, enlargeYAnimatorRecover, alphaAnimatorRecover);
				animatorSet.start();
			}
			return true;
		}
	}
}