package com.example.projectdy2;

//import android.graphics.Camera;
import android.Manifest;
import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.projectdy2.Util.StatusBarUtils;
import com.example.projectdy2.VideoManager.api.IMiniDouyinService;
import com.example.projectdy2.VideoManager.model.PostVideoResponse;
import com.example.projectdy2.VideoManager.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecordActivity extends AppCompatActivity implements SurfaceHolder.Callback {
//	private boolean isPic = true;

	private boolean isRecording = false;
	private int flashMode = 0;//0:关闭 1:打开 2:AUTO
	private boolean isDelay = false;
	private int mCameraId = 0;

	private static final int PICK_IMAGE = 1;
	private static final int PICK_VIDEO = 2;

	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder mHolder;
	private String mp4Path;
	private ConstraintLayout contentViewGroup;

	private ImageButton flashButton;
	private LottieAnimationView recordButton;
	private ImageButton switchButton;
	private ImageButton delayButton;

	private Button uploadButton2;
	private Button uploadButton;
	private Button deleteButton;

	private VideoView videoView;
	private Handler handler = new Handler();

	private String[] permissions = new String[] {Manifest.permission.CAMERA};

	private Uri imageURI = null;
	private Uri videoURI = null;

	private Runnable runnable;

	private Retrofit retrofit = new Retrofit.Builder()
			.baseUrl(IMiniDouyinService.BASE_URL)
			.addConverterFactory(GsonConverterFactory.create())
			.build();
	private IMiniDouyinService miniDouyinService = retrofit.create(IMiniDouyinService.class);

	private TextView clock ;

	private CountDownTimer timer = new CountDownTimer(15 * 1000, 1000) {
		/**
		 * 固定间隔被调用,就是每隔countDownInterval会回调一次方法onTick
		 * @param millisUntilFinished
		 */
		@Override
		public void onTick(long millisUntilFinished) {
			clock.setText(formatTime(millisUntilFinished));
		}


		/**
		 * 倒计时完成时被调用
		 */
		@Override
		public void onFinish() {
			clock.setText("00:00");
			record();
		}
	};

	String TAG = "相机";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		contentViewGroup = findViewById(R.id.contentALL);

		StatusBarUtils.setColor(this,0xFF000000);
		videoView = findViewById(R.id.videoView);
		videoView.setVisibility(View.GONE);
		videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.setLooping(true);
			}
		});
		videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
			@Override
			public boolean onError(MediaPlayer mp, int what, int extra) {
				return true;
			}
		});

		deleteButton = findViewById(R.id.deleteButton);
		uploadButton = findViewById(R.id.uploadButton);
		uploadButton2 = findViewById(R.id.uploadButton2);
		deleteButton.setVisibility(View.GONE);
		uploadButton.setVisibility(View.GONE);
		uploadButton2.setVisibility(View.GONE);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				videoView.setVisibility(View.GONE);
				deleteButton.setVisibility(View.GONE);
				uploadButton.setVisibility(View.GONE);
				uploadButton2.setVisibility(View.GONE);

				videoView.stopPlayback();
				delete(mp4Path);
			}
		});
		uploadButton2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				if ( isPic )
					chooseImage();
//				else
//					chooseVideo();
//				isPic = !isPic;
			}
		});
		uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				chooseImage();
//				File file = new File(mp4Path);
//				file.

//				videoURI = Uri.fromFile(new File(mp4Path));
//				getMultipartFromUri()
				if ( imageURI == null ) {
					Toast.makeText(RecordActivity.this,"请先选择封面",Toast.LENGTH_SHORT).show();
					return;
				}
				MultipartBody.Part coverImagePart = getMultipartFromUri("cover_image", imageURI);
				MultipartBody.Part videoPart = getMultipartFromUri("video", mp4Path);

				Log.d(TAG, "onClick: IMAGE_URI" + imageURI);
				Log.d(TAG, "onClick: VIDEO_URI" + videoURI);

				uploadButton.setText("上传中...");
				uploadButton.setEnabled(false);
				deleteButton.setEnabled(false);
				uploadButton2.setEnabled(false);
				Log.d(TAG, "onClick: 执行到上传");

				miniDouyinService.postVideo("18973100927", "张文韬", coverImagePart, videoPart).enqueue(
						new Callback<PostVideoResponse>() {
							@Override
							public void onResponse(Call<PostVideoResponse> call, Response<PostVideoResponse> response) {
								Log.d(TAG, "onResponse: onResponse");
								
								if (response.body() != null) {
//									Toast.makeText(RecordActivity.this, response.body().toString(), Toast.LENGTH_SHORT)
//											.show();
								}
								Toast.makeText(RecordActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
//								finish();
								uploadButton.setText("上传");
								uploadButton.setEnabled(true);
								deleteButton.setEnabled(true);
								uploadButton2.setEnabled(true);
							}

							@Override
							public void onFailure(Call<PostVideoResponse> call, Throwable throwable) {
								Log.d(TAG, "onFailure: ");
								
//								Toast.makeText(RecordActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
//								Toast.makeText(RecordActivity.this, "上传失败", Toast.LENGTH_SHORT).show();
								uploadButton.setText("上传");
								uploadButton.setEnabled(true);
								deleteButton.setEnabled(true);
								uploadButton2.setEnabled(true);
//								finish();
							}
						});
			}
		});

		initSurfaceView();
		initCamera(mCameraId);

		flashButton = findViewById(R.id.flashButton) ;
		flashButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (flashMode) {
					case 0:
						flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_on));
						turnFlashOn(mCamera);
						flashMode = 1 ;
						break;
					case 1:
						flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_auto));
						turnFlashAuto(mCamera);
						flashMode = 2 ;
						break;
					case 2:
						flashButton.setImageDrawable(getResources().getDrawable(R.drawable.flash_off));
						turnFlashOff(mCamera);
						flashMode = 0 ;
						break;
					default:
						break;
				}
				Log.d(TAG, "onClick: "+flashMode);
			}
		});
		delayButton = findViewById(R.id.delayButton);
		delayButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( isDelay )
					delayButton.setImageDrawable(getResources().getDrawable(R.drawable.delay_off));
				else
					delayButton.setImageDrawable(getResources().getDrawable(R.drawable.delay_three));
				isDelay = !isDelay;
				Log.d(TAG, "onClick: "+isDelay);
			}
		});

		recordButton = findViewById(R.id.recordButton);
		recordButton.addAnimatorListener(new Animator.AnimatorListener() {
			@Override
			public void onAnimationStart(Animator animation) {
//				animation.setDuration(15000);
//				animation.getDuration();
				Log.d(TAG, "onAnimationStart: " + animation.getDuration());
			}

			@Override
			public void onAnimationEnd(Animator animation) {

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}
		});
		recordButton.addAnimatorUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				if ( animation.getCurrentPlayTime() > 3000 ) {
					record();
				}
			}
		});
		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if ( mCameraId != 0 ) {
					final AlertDialog.Builder alterDiaglog = new AlertDialog.Builder(RecordActivity.this);
					alterDiaglog.setTitle("提示");//文字
					alterDiaglog.setMessage("前置摄像头不可用");//提示消息
					//积极的选择
					alterDiaglog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
						}
					});
					//显示
					alterDiaglog.show();
				}
				else
					record(v);
			}
		});
		switchButton = findViewById(R.id.switchButton);
		switchButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switchCamera();
			}
		});

		clock = findViewById(R.id.clock);
	}

	private void initCamera(int id) {
		mCamera = Camera.open(id);
		Camera.Parameters parameters = mCamera.getParameters();
		parameters.setPictureFormat(ImageFormat.JPEG);
		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
		parameters.set("orientation","portrait");
		parameters.set("rotate",90);
//		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//		Camera.Size size = parameters.getSupportedPreviewSizes().get(0);
//		int t = size.width ; size.width = size.height; size.height = t;
//		parameters.setPreviewSize(size.width,size.height);
//		parameters.setPreviewSize(size.height,size.width);

//		ViewGroup.LayoutParams params = mSurfaceView.getLayoutParams();
//		params.height = params.width*size.height/size.width;
//		mSurfaceView.setLayoutParams(params);

		mCamera.setParameters(parameters);
		mCamera.setDisplayOrientation(90);
	}
	private void initSurfaceView() {
		mSurfaceView = findViewById(R.id.surfaceView);
		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
	}
	private boolean prepareVideoRecorder() {
		mMediaRecorder = new MediaRecorder();

		mCamera.unlock();
		mMediaRecorder.setCamera(mCamera);

		mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
		mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		mp4Path = getOutputMediaPath();
		mMediaRecorder.setOutputFile(mp4Path);

//		mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
		mMediaRecorder.setOrientationHint(90);
		try {
			mMediaRecorder.prepare();
		} catch (Exception e) {
//			e.printStackTrace();
//			releaseMediaRecorder();
			return false;
		}
		return true;
	}

	public void record(View view) {
		if ( isRecording ) {
			recordButton.pauseAnimation();
			recordButton.setProgress(0);
			mMediaRecorder.stop();
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
			mCamera.lock();
//			videoView.stopPlayback();
//			videoView.re
//			videoView.setVideoPath("");
			clock.setVisibility(View.GONE);
			deleteButton.setVisibility(View.VISIBLE);
			uploadButton.setVisibility(View.VISIBLE);
			uploadButton2.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoPath(mp4Path);
//			Log.d(TAG, "record: "+mp4Path);
//			recordButton
			videoView.start();
		} else {
			if ( prepareVideoRecorder() ) {
				if ( isDelay ) {
//					ConstraintLayout container = new ConstraintLayout(this);//主布局container
					final TextView tv = new TextView(this);//子View TextView
					// 为主布局container设置布局参数
					ConstraintLayout.LayoutParams llp = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT);
					tv.setLayoutParams(llp);//设置container的布局
					tv.setTextColor(0xFFFFFFFF);
					tv.setBackgroundColor(0x88000000);
					tv.setTextSize(200);
					tv.setText("3");
					tv.setGravity(Gravity.CENTER);
					contentViewGroup.addView(tv);// 将TextView 添加到container中

					new Thread(new Runnable() {
						@Override
						public void run() {
							try {
								Log.d(TAG, "run: 开始sleep");
								Thread.sleep(1000);
								Log.d(TAG, "run: sleep1");
								tv.setText("2");
								Thread.sleep(1000);
								Log.d(TAG, "run: sleep2");
								tv.setText("1");
								Thread.sleep(1000);
								Log.d(TAG, "run: sleep3");
								mMediaRecorder.start();
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										recordButton.playAnimation();
										contentViewGroup.removeView(tv);
										clock.setVisibility(View.VISIBLE);
										timer.start();
									}
								});
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}).start();
				}
				else {
					mMediaRecorder.start();
					recordButton.playAnimation();
					clock.setVisibility(View.VISIBLE);
					Log.d(TAG, "record: 已经打开clock");
					timer.start();
				}
			}
		}
		isRecording = !isRecording;
		Log.d(TAG, "record: "+isRecording);
	}
	public void record() {
		if ( isRecording ) {
			recordButton.pauseAnimation();
			recordButton.setProgress(0);

			mMediaRecorder.stop();
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
			mCamera.lock();
//			videoView.stopPlayback();
//			videoView.re
//			videoView.setVideoPath("");
			clock.setVisibility(View.GONE);
			uploadButton2.setVisibility(View.VISIBLE);
			deleteButton.setVisibility(View.VISIBLE);
			uploadButton.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.VISIBLE);
			videoView.setVideoPath(mp4Path);
			Log.d(TAG, "record: "+mp4Path);
			videoView.start();
		} else {
			if ( prepareVideoRecorder() ) {

				mMediaRecorder.start();
			}
		}
		isRecording = !isRecording;
		Log.d(TAG, "record: "+isRecording);
	}

	private String getOutputMediaPath() {
		File mediaStorageDir = getExternalFilesDir(Environment.DIRECTORY_MOVIES);
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

		File mediaFile = new File(mediaStorageDir,"Video"+timeStamp+".mp4");
		if ( !mediaFile.exists() ) {
			mediaFile.getParentFile().mkdirs();
		}
		return mediaFile.getAbsolutePath();
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		if (holder.getSurface() == null) {
			return ;
		}
		mCamera.stopPreview();
		try {
			mCamera.setPreviewDisplay(holder);
			mCamera.startPreview();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();
		mCamera = null;
	}
	@Override
	protected void onResume() {
		super.onResume();
		if ( mCamera == null ) {
			initCamera(mCameraId);
		}
		mCamera.startPreview();
	}
	@Override
	protected void onPause() {
		super.onPause();
		mCamera.stopPreview();
	}

	private void turnFlashOn(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		if (flashModes == null) {
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			}
		}
	}
	private void turnFlashAuto(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		if (flashModes == null) {
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Camera.Parameters.FLASH_MODE_AUTO.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_AUTO)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
				mCamera.setParameters(parameters);
			}
		}
	}
	private void turnFlashOff(Camera mCamera) {
		if (mCamera == null) {
			return;
		}
		Camera.Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		String flashMode = parameters.getFlashMode();
		if (flashModes == null) {
			return;
		}
		if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			}
		}
	}
	public void switchCamera() {
		mCamera.stopPreview();
		releaseCamera();
		mCameraId = (mCameraId + 1) % Camera.getNumberOfCameras();
		initCamera(mCameraId);
//		initSurfaceView();

		if (mHolder != null) {
//			prepareVideoRecorder();
			try {
				mCamera.setPreviewDisplay(mHolder);
				mCamera.startPreview();
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	private void delete(String filePath) {
		File file = new File(filePath);
		if ( file.exists() ) file.delete();
	}

	public void chooseImage() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Picture"),
				PICK_IMAGE);
	}
	public void chooseVideo() {
		Intent intent = new Intent();
		intent.setType("video/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(Intent.createChooser(intent, "Select Video"), PICK_VIDEO);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK && null != data) {
			if (requestCode == PICK_IMAGE) {
				imageURI = data.getData();
			} else if (requestCode == PICK_VIDEO) {
				videoURI = data.getData();
			}
		}
	}

	private MultipartBody.Part getMultipartFromUri(String name, Uri uri) {
		File f = new File(ResourceUtils.getRealPath(RecordActivity.this, uri));
		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
		return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
	}

	private MultipartBody.Part getMultipartFromUri(String name, String filePath) {
		File f = new File(filePath);
		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), f);
		return MultipartBody.Part.createFormData(name, f.getName(), requestFile);
	}

	public String formatTime(long millisecond) {
		int minute;//分钟
		int second;//秒数
		minute = (int) ((millisecond / 1000) / 60);
		second = (int) ((millisecond / 1000) % 60);
		if (minute < 10) {
			if (second < 10) {
				return "0" + minute + ":" + "0" + second;
			} else {
				return "0" + minute + ":" + second;
			}
		}else {
			if (second < 10) {
				return minute + ":" + "0" + second;
			} else {
				return minute + ":" + second;
			}
		}
	}

}
