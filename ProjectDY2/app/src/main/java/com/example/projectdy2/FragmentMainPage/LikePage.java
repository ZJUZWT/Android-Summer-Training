package com.example.projectdy2.FragmentMainPage;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.DataBase.FavorRelation;
import com.example.projectdy2.DataBase.LikeRelation;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.DataBase.UserEntity;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.FavorPageRVAdapter;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.MainPageRVAdapter;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.VideoListLayoutManager;
import com.example.projectdy2.InterfaceForInteract.LogoutListener;
import com.example.projectdy2.InterfaceForInteract.OnViewPagerListener;
import com.example.projectdy2.InterfaceForInteract.showLikePage;
import com.example.projectdy2.LoginActivity;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.example.projectdy2.RegisterActivity;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikePage extends Fragment implements showLikePage, LogoutListener {
	boolean isInit = false;
	boolean isPause = false ;
	boolean isPressed = false;
	boolean isFocus = true;
	boolean isPlay;
	int nowPosition = 0 ;

	List<FavorRelation> data;
	RecyclerView recyclerView;
	FavorPageRVAdapter adapter;
	LinearLayoutManager linearLayoutManager;
	LottieAnimationView lottieAnimationView;

	ProgressBar progressBar;

	VideoListLayoutManager  myLayoutManager;

	//登录操作的东西
	Button buttonLogin;
	Button buttonRegister;
	View layoutLogin;
	TextView noLikeVideo;

	String TAG = "Like";

	@Override
	public void onAttach(@NonNull Context context) {
		super.onAttach(context);
//		data = ((MainActivity)context).getVideo();//通过强转成宿主activity，就可以获取到传递过来的数据
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage_video_like_page,container,false);
	}

	@Override
	public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		//处理注册
		layoutLogin = view.findViewById(R.id.like_page_login);

		recyclerView = view.findViewById(R.id.like_page);
		recyclerView.setHasFixedSize(true);

//		linearLayoutManager = new LinearLayoutManager(view.getContext());
//		linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
		myLayoutManager = new VideoListLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
		adapter = new FavorPageRVAdapter();

		recyclerView.setLayoutManager(myLayoutManager);
		recyclerView.setAdapter(adapter);

		lottieAnimationView = view.findViewById(R.id.lottie_login);
		lottieAnimationView.playAnimation();
		isPlay = true;

		buttonLogin = view.findViewById(R.id.no_user_login);
		buttonRegister = view.findViewById(R.id.no_user_register);
		buttonLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			}
		});
		buttonRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), RegisterActivity.class);
				startActivity(intent);
			}
		});

		noLikeVideo = view.findViewById(R.id.no_like_video);

		initVideo();
		initInterface();
	}

	private void initVideo() {
		isInit = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				List<UserEntity> temp = query.hasLogin();
				if ( temp.size() == 0 ) {

				} else {
					List<FavorRelation> temp2 = query.favor(temp.get(0).getId());
					data = temp2 ;
					adapter.setData(data);
//					getActivity().runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							recyclerView.scrollToPosition(0);
//							nowPosition = 0 ;
//						}
//					});
					adapter.notifyItemChanged(0,data.size()-1);
				}
			}
		}).start();
//		recyclerView.scrollToPosition(0);
//		nowPosition = 0 ;
		isInit = false;
	}


	private void initInterface() {
		myLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
			@Override
			public void onInitComplete() {

			}

			@Override
			public void onPageRelease(boolean isNext, int position) {
				Log.d(TAG,"释放位置:"+position +" 下一页:"+isNext);
				int index;
				if (isNext)		index = 0;
				else 			index = 1;

				releaseVideo(position);
			}

			@Override
			public void onPageSelected(int position, boolean isNext) {
				Log.d(TAG,"释放位置:"+position +" 下一页:"+isNext);
				int index;
				if (isNext)	index = 0;
				else		index = 1;

				Log.d(TAG, "onPageSelected: data.size() = " + data.size() + " position : " + position);
				if ( position >= data.size() ) { position = data.size()-1 ; return ; }
//				if ( isInit ) position = 0 ;
				Log.d(TAG, "onPageSelected: data.size() = " + data.size() + " position : " + position);
				nowPosition = position;
				playVideo(position);
			}
		});
	}

	private void releaseVideo(int index){
//		View itemView = recyclerView.getChildAt(index);
		View itemView = recyclerView.getLayoutManager().findViewByPosition(index);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
//		final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//		final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//		final
		videoView.stopPlayback();
//		imgThumb.animate().alpha(1).start();
//		imgPlay.animate().alpha(0f).start();
	}


	private void playVideo(final int position) {
		isPressed = false;
		final Uri videoLink;
//		int random = position;

		videoLink = Uri.parse(data.get(position).getVideoLink());

//		Log.d(TAG, "position : " + position + " 视频URL:" + videoLink);

//		View itemView = recyclerView.getChildAt(position);
		View itemView = recyclerView.getLayoutManager().findViewByPosition(position);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
		Log.d(TAG, "playVideo: 开启progressBar" + position);
//		final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//		final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//		final RelativeLayout rootView = itemView.findViewById(R.id.);
//		final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
//		Log.d(TAG,"position : " + position + " 视频URL:"+data.get(position).videoUrl );

		progressBar = itemView.findViewById(R.id.progress_bar2);
		progressBar.setVisibility(View.VISIBLE);

		videoView.setVideoURI(videoLink);
		videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
			@Override
			public boolean onInfo(MediaPlayer mp, int what, int extra) {
//				mediaPlayer[0] = mp;
				mp.setLooping(true);
//				imgThumb.animate().alpha(0).setDuration(200).start();
				return false;
			}
		});
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
//				FindCurrentTab findCurrentTab = (FindCurrentTab) getParentFragment();
				Log.d(TAG, "playVideo: 关闭progressBar" + position);
				progressBar.setVisibility(View.GONE);

				int width = mp.getVideoWidth();
				int height = mp.getVideoHeight();
				ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) videoView.getLayoutParams();
				layoutParams.height = layoutParams.width * height / width;
//				layoutParams.topMargin = (1920-layoutParams.height)/2;
				Log.d(TAG, "onCreate: !!!" + "宽:" + layoutParams.width + " 高:" + layoutParams.height);
				videoView.setLayoutParams(layoutParams);

				if (isFocus) {
					videoView.start();
				}
			}
		});

		videoView.setOnClickListener(new View.OnClickListener() {
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
					if (videoView.isPlaying()) videoView.pause();
					else videoView.start();
				}
			}
		});
	}

	@Override
	public void showLikePage() {
		isFocus = true;

			//TODO:视频播放
			new Thread() {
				@Override
				public void run() {
					QueryDao query = MyDataBase.inst(getContext()).queryDao();
					List<UserEntity> test = query.hasLogin();
					if ( test.size() == 0 ) {
						getActivity().runOnUiThread(new Runnable() {
							@Override
							public void run() {
								recyclerView.setVisibility(View.GONE);
								layoutLogin.setVisibility(View.VISIBLE);
								noLikeVideo.setVisibility(View.GONE);
							}
						});
					} else {
						final List<FavorRelation> test2 = query.favor(test.get(0).getId());


						if (test2.size() == 0)
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									layoutLogin.setVisibility(View.GONE);
									recyclerView.setVisibility(View.GONE);
									noLikeVideo.setVisibility(View.VISIBLE);
									data = test2 ;
									adapter.setData(data);

									adapter.notifyItemChanged(0,data.size());
								}
							});
						else {
//							query = MyDataBase.inst(getContext()).queryDao();
							List<UserEntity> temp = query.hasLogin();
							final List<FavorRelation> temp2 = query.favor(temp.get(0).getId());

							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
//									initVideo();
									layoutLogin.setVisibility(View.GONE);
									recyclerView.setVisibility(View.VISIBLE);
									noLikeVideo.setVisibility(View.GONE);

									data = temp2 ;
									adapter.setData(data);

									adapter.notifyItemChanged(0,data.size());
									Log.d(TAG, "run: FIX.data.size()");
									isFocus = true;
									for ( int i = 0 ; i < data.size() ; i++ ) {
										View itemView = recyclerView.getLayoutManager().findViewByPosition(i);
										if ( itemView != null ) {
											final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
											videoView.start();
										}
									}
								}
							});
						}
					}
				}
			}.start();

		if ( layoutLogin.getVisibility() == View.VISIBLE) {
			lottieAnimationView.playAnimation();
			isPlay = true;
		}

	}
	@Override
	public void pauseLikePage() {
		isFocus = false;
		if ( layoutLogin.getVisibility() == View.VISIBLE) {
			lottieAnimationView.pauseAnimation();
			isPlay = false;
		}
		else {
			//TODO:视频暂停
			if ( recyclerView.getVisibility() != View.GONE) {
//				recyclerView.scrollToPosition(2);
//				nowPosition = 0 ;

				for ( int i = 0 ; i < data.size() ; i++ ) {
					View itemView = recyclerView.getLayoutManager().findViewByPosition(i);
					if ( itemView != null ) {
						final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
						videoView.pause();
					}
				}
			}
		}
	}

	@Override
	public void onResume() {
		showLikePage();
//		Log.d(TAG, "onResume: " + lottieAnimationView.isAnimating());
		super.onResume();
//		new Thread() {
//			@Override
//			public void run() {
//				QueryDao query = MyDataBase.inst(getContext()).queryDao();
//				List<UserEntity> test = query.hasLogin();
//				if ( test.size() == 0 ) {
//					getActivity().runOnUiThread(new Runnable() {
//						@Override
//						public void run() {
//							recyclerView.setVisibility(View.GONE);
//							layoutLogin.setVisibility(View.VISIBLE);
//							noLikeVideo.setVisibility(View.GONE);
//						}
//					});
//				} else {
//					List<LikeRelation> test2 = query.like(test.get(0).getId());
//					if (test2.size() == 0)
//						getActivity().runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								layoutLogin.setVisibility(View.GONE);
//								recyclerView.setVisibility(View.GONE);
//								noLikeVideo.setVisibility(View.VISIBLE);
//							}
//						});
//					else
//						getActivity().runOnUiThread(new Runnable() {
//							@Override
//							public void run() {
//								layoutLogin.setVisibility(View.GONE);
//								recyclerView.setVisibility(View.VISIBLE);
//								noLikeVideo.setVisibility(View.GONE);
//							}
//						});
//				}
//
//			}
//		}.start();
//		if ( isPause && isPlay ) lottieAnimationView.playAnimation();
//		isPause = false;
	}

	@Override
	public void onPause() {
		isPause = true;
//		isPlay = lottieAnimationView.isAnimating();
		Log.d(TAG, "onPause: " + isPlay);
		super.onPause();
	}

	@Override
	public void onStop() {
		Log.d(TAG, "onStop: ");
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		Log.d(TAG, "onDestroyView: ");
		super.onDestroyView();
	}

	@Override
	public void logout() {
		onResume();
	}
}
