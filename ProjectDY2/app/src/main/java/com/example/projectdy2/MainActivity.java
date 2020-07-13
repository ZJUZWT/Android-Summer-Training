package com.example.projectdy2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.projectdy2.Fragment.FragmentAround;
import com.example.projectdy2.Fragment.FragmentChat;
import com.example.projectdy2.Fragment.FragmentMainPage;
import com.example.projectdy2.Fragment.FragmentMy;
import com.example.projectdy2.Util.StatusBarUtils;
import com.example.projectdy2.View.WaveButtonView;

public class MainActivity extends AppCompatActivity {

	private boolean isQuit = false;

	private WaveButtonView waveButtonView1;
	private WaveButtonView waveButtonView2;
	private WaveButtonView waveButtonView4;
	private WaveButtonView waveButtonView5;
	private WaveButtonView pressedButton;
	private Handler waveButtonHandler = new Handler() ;
	private float waveProgress = 0.f;
	private float waveIncrement = 0.02f;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	private Fragment nowFragment = new Fragment();
	private Fragment fragmentMainPage = new FragmentMainPage();
	private Fragment fragmentAround = new FragmentAround();
	private Fragment fragmentChat = new FragmentChat();
	private Fragment fragmentMy = new FragmentMy();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
//		StatusBarUtils.setColor(this,getColor(R.color.colorLightThemeBlue));
		StatusBarUtils.setColor(this,getColor(R.color.MainPageTitleBar));

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//TODO:
		// 网络交互提取视频信息
		initVideo();

		//TODO:
		// fragment的处理
		initFragment();

		//TODO:
		// 1：实现下面所有的WaveButton
		// 2：利用多线程处理下面4个WaveButton的关系
		initWaveButton();
	}

	private void initVideo() {

	}
	private void initFragment() {
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
				if ( waveButtonView.getIsPressed() ) return ;
				pressedButton.setIsPressed(false);
				waveButtonView.setIsPressed(true);

				pressedButton.invalidate();
				waveButtonView.invalidate();

				pressedButton = waveButtonView;

				if (waveButtonView1.equals(pressedButton))
					fragment_replace(fragmentMainPage);
				if (waveButtonView2.equals(pressedButton))
					fragment_replace(fragmentAround);
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
	}

	private void fragment_replace(Fragment fragment) {
		if (fragment != nowFragment) {
			fragmentManager = getSupportFragmentManager();
			fragmentTransaction = fragmentManager.beginTransaction();
//			fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);

			if (!fragment.isAdded()) {
				fragmentTransaction.hide(nowFragment).add(R.id.replace, fragment).commit();
			} else {
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
}