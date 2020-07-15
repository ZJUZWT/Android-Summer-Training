package com.example.projectdy2.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.projectdy2.MainActivity;
import com.example.projectdy2.R;
import com.example.projectdy2.VideoActivity;
import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.GetVideosResponse;
import com.example.projectdy2.VideoManager.model.Video;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FragmentList extends Fragment {
	private List<Video> mVideos = new ArrayList<>();
	private RecyclerView recyclerView;

	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(IMiniDouyinService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

	String TAG = "测试第二页面" ;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list,container,false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initRecyclerView(view);
		initVideo();
	}

	public static class MyViewHolder extends RecyclerView.ViewHolder {
		public ImageView img;

		public MyViewHolder(@NonNull View itemView) {
			super(itemView);
			img = itemView.findViewById(R.id.img);
		}

		public void bind(final Activity activity, final Video video) {
			displayWebImage(video.imageUrl, img);
			img.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					VideoActivity.launch(activity, video.videoUrl);
				}
			});
		}
	}

	private void initRecyclerView(View view) {
		recyclerView = view.findViewById(R.id.list_page_rv);
//        mRv.setLayoutManager(new LinearLayoutManager(this));
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
		recyclerView.setAdapter(new RecyclerView.Adapter<MyViewHolder>() {
			@NonNull
			@Override
			public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
				return new MyViewHolder(
						LayoutInflater.from(getContext())
								.inflate(R.layout.fragment_list_video_element, viewGroup, false));
			}

			@Override
			public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {
				final Video video = mVideos.get(i);
				viewHolder.bind(getActivity(), video);
			}

			@Override
			public int getItemCount() {
				return mVideos.size();
			}
		});
	}

	public static void displayWebImage(String url, ImageView imageView) {
		Glide
				.with(imageView.getContext())
				.load(url)
				.placeholder(R.drawable.pic_loading)
				.apply(RequestOptions.bitmapTransform(new RoundedCorners(20)))
				.error(R.drawable.pic_fail_loading)
				.into(imageView);
	}

	private void initVideo() {
		miniDouyinService.getVideos().enqueue(new Callback<GetVideosResponse>() {
			@Override
			public void onResponse(Call<GetVideosResponse> call, Response<GetVideosResponse> response) {
				if (response.body() != null && response.body().videos != null) {
					mVideos = response.body().videos;
					Log.d(TAG, "onResponse: " + mVideos.size());
//					recyclerView.getAdapter().set
//					isFetchData = true;

//					Objects.requireNonNull(recyclerView.getAdapter()).notifyItemChanged(0,mVideos.size());
					recyclerView.getAdapter().notifyDataSetChanged();
				}
			}
			@Override
			public void onFailure(Call<GetVideosResponse> call, Throwable throwable) {
				Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
			}
		});
	}
}
