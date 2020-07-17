package com.example.projectdy2.FragmentMainPage;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.projectdy2.DataBase.FavorRelation;
import com.example.projectdy2.DataBase.LikeRelation;
import com.example.projectdy2.DataBase.MyDataBase;
import com.example.projectdy2.DataBase.QueryDao;
import com.example.projectdy2.DataBase.UserEntity;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.MainPageRVAdapter;
import com.example.projectdy2.FragmentMainPage.RecyclerViewManager.VideoListLayoutManager;
import com.example.projectdy2.InterfaceForInteract.FindCurrentTab;
import com.example.projectdy2.InterfaceForInteract.OnViewPagerListener;
import com.example.projectdy2.InterfaceForInteract.RefreshList;
import com.example.projectdy2.InterfaceForInteract.showRecommendPage;
import com.example.projectdy2.R;
import com.example.projectdy2.VideoActivity;
import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;
import com.example.projectdy2.View.ShineButtonView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.OrientationHelper;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecommendPage extends Fragment implements RefreshList, showRecommendPage {
	boolean isFocus;
	int nowPosition;

	boolean isPressed = false;
	List<Video> data;
	List<Video> usedData = new ArrayList<>();
	RecyclerView recyclerView;
	MainPageRVAdapter adapter;
	LinearLayoutManager linearLayoutManager;
	VideoListLayoutManager myLayoutManager;
	ProgressBar progressBar;
	String TAG = "测试" ;

	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(IMiniDouyinService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_mainpage_video_like_page,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		recyclerView = view.findViewById(R.id.like_page);
		recyclerView.setHasFixedSize(true);

		linearLayoutManager = new LinearLayoutManager(view.getContext());
		linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
		myLayoutManager = new VideoListLayoutManager(getContext(), OrientationHelper.VERTICAL,false);
		adapter = new MainPageRVAdapter();

		recyclerView.setLayoutManager(myLayoutManager);
		recyclerView.setAdapter(adapter);

		initVideo();
		initInterface();
	}


	private void initVideo() {
		miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
			@Override
			public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
				if (response.body() != null && response.body().videos != null) {
					usedData.clear();
					data = response.body().videos;
					adapter.setData(data);

					adapter.notifyDataSetChanged();
					recyclerView.scrollToPosition(0);
					isFocus = false;
					nowPosition = 0 ;
//					adapter.notifyItemChanged(0,data.size());
				}
			}
			@Override
			public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
				Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
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


	private void playVideo(int position) {
		isPressed = false;
		final Uri videoLink ;
		int random = (int) (Math.random()*data.size());
		if ( position >= usedData.size() ) {
			usedData.add(data.get(random));
			data.remove(random);
		}
		videoLink = Uri.parse(usedData.get(position).videoUrl);

		Log.d(TAG,"position : " + position + " 视频URL:"+videoLink );
//		View itemView = recyclerView.getChildAt(position);
		View itemView = recyclerView.getLayoutManager().findViewByPosition(position);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
//		final ImageView imgPlay = itemView.findViewById(R.id.img_play);
//		final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
//		final RelativeLayout rootView = itemView.findViewById(R.id.);
//		final MediaPlayer[] mediaPlayer = new MediaPlayer[1];
//		Log.d(TAG,"position : " + position + " 视频URL:"+data.get(position).videoUrl );

		Log.d(TAG, "playVideo: 开启progressBar");
		progressBar = itemView.findViewById(R.id.progress_bar);
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
				Log.d(TAG, "playVideo: 关闭progressBar");
				progressBar.setVisibility(View.GONE);

				int width = mp.getVideoWidth();
				int height = mp.getVideoHeight();
				ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams)videoView.getLayoutParams();
				layoutParams.height = layoutParams.width*height/width;
//				layoutParams.topMargin = (1920-layoutParams.height)/2;
				Log.d(TAG, "onCreate: !!!" + "宽:"+layoutParams.width + " 高:" + layoutParams.height);
				videoView.setLayoutParams(layoutParams);

				if ( isFocus ) {
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
							try { Thread.sleep(500); }
							catch (Exception e) { e.printStackTrace(); }
							finally	{ isPressed = false; }
						}
					}).start();
				} else {
					if ( videoView.isPlaying() ) videoView.pause(); else videoView.start();
				}
			}
		});

		final ShineButtonView likeButton = itemView.findViewById(R.id.mainPageLikeButton);
		initLikeButton(likeButton,videoLink.toString());
		final ShineButtonView favorButton = itemView.findViewById(R.id.mainPageFavorButton);
		initFavorButton(favorButton,videoLink.toString());
		likeButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				Log.d(TAG, "onClick: 按键");

				new Thread() {
					@Override
					public void run() {
						QueryDao query = MyDataBase.inst(getContext()).queryDao();
						if ( query.hasLogin().size() == 0 ) {
							Snackbar.make(v,"未登录无法使用", Snackbar.LENGTH_SHORT).show();
						} else {
							//TODO：数据库更新
							if ( likeButton.getDrawableId() == R.drawable.like_button ) {
								query.addLike(new LikeRelation(query.hasLogin().get(0).getId(),videoLink.toString()));

								getActivity().runOnUiThread(new Runnable() {
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
								query.delLike(query.hasLogin().get(0).getId(),videoLink.toString());
								getActivity().runOnUiThread(new Runnable() {
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
				Log.d(TAG, "onClick: 按键");

				new Thread() {
					@Override
					public void run() {
						QueryDao query = MyDataBase.inst(getContext()).queryDao();
						if ( query.hasLogin().size() == 0 ) {
							Snackbar.make(v,"未登录无法使用", Snackbar.LENGTH_SHORT).show();
						} else {
							//TODO：数据库更新

							if ( favorButton.getDrawableId() == R.drawable.favor_button ) {
								//TODO:动画
								query.addFavor(new FavorRelation(query.hasLogin().get(0).getId(),videoLink.toString()));

								getActivity().runOnUiThread(new Runnable() {
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
								query.delFavor(query.hasLogin().get(0).getId(),videoLink.toString());
								getActivity().runOnUiThread(new Runnable() {
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

	@Override
	public void refresh() {
		initVideo();
	}

	@Override
	public void showRecommendPage() {
		Log.d(TAG, "showRecommendPage: 过来");
		
		isFocus = true;
		View itemView = recyclerView.getLayoutManager().findViewByPosition(nowPosition);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
		videoView.start();
	}
	@Override
	public void pauseRecommendPage() {
		isFocus = false;
		View itemView = recyclerView.getLayoutManager().findViewByPosition(nowPosition);
		final VideoView videoView = itemView.findViewById(R.id.mainPageVideoView);
		videoView.pause();
	}

	private void initLikeButton(final ShineButtonView likeButton, final String url) {
		//TODO 查询当前状态
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				List<UserEntity> temp = query.hasLogin();


				if ( temp.size() == 0 ) { //没有登陆
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							likeButton.loadImage(R.drawable.like_button);
						}
					});
				} else {
					final List<LikeRelation> temp2 = query.isLike(temp.get(0).getId(),url);
					getActivity().runOnUiThread(new Runnable() {
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

	private void initFavorButton(final ShineButtonView favorButton, final String url) {
		//TODO 查询当前状态
		new Thread() {
			@Override
			public void run() {
				QueryDao query = MyDataBase.inst(getContext()).queryDao();
				List<UserEntity> temp = query.hasLogin();

				if ( temp.size() == 0 ) { //没有登陆
					getActivity().runOnUiThread(new Runnable() {
						@Override
						public void run() {
							favorButton.loadImage(R.drawable.favor_button);
						}
					});
				} else {
					final List<FavorRelation> temp2 = query.isFavor(temp.get(0).getId(),url);
					getActivity().runOnUiThread(new Runnable() {
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
