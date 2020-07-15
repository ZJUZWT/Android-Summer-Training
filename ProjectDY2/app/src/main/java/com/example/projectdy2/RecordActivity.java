package com.example.projectdy2;

//import android.graphics.Camera;
import android.Manifest;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.media.CamcorderProfile;
import android.media.Image;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;

import com.example.projectdy2.Util.StatusBarUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;


public class RecordActivity extends AppCompatActivity implements SurfaceHolder.Callback {
	private boolean isRecording;
	private int flashMode = 0;//0:关闭 1:打开 2:AUTO
	private boolean isDelay = false;
	private int mCameraId = 1;

	private Camera mCamera;
	private SurfaceView mSurfaceView;
	private MediaRecorder mMediaRecorder;
	private SurfaceHolder mHolder;
	private String mp4Path;
	private View contentViewGroup;

	private ImageButton flashButton;
	private ImageButton recordButton;
	private ImageButton switchButton;

	private Button uploadButton;
	private Button deleteButton;

	private VideoView videoView;

	private String[] permissions = new String[] {Manifest.permission.CAMERA};

	String TAG = "相机";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);

		ActivityCompat.requestPermissions(this,permissions,0);

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
		deleteButton.setVisibility(View.GONE);
		uploadButton.setVisibility(View.GONE);
		deleteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				videoView.setVisibility(View.GONE);
				deleteButton.setVisibility(View.GONE);
				uploadButton.setVisibility(View.GONE);

				videoView.stopPlayback();

				delete(mp4Path);
//				videoView = findViewById(R.id.videoView);
//				videoView.pause();
//				mp4Path = getOutputMediaPath();
//				mMediaRecorder.setOutputFile(mp4Path);
//				prepareVideoRecorder();
			}
		});
		uploadButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				prepareVideoRecorder();
//				mp4Path = getOutputMediaPath();
//				mMediaRecorder.setOutputFile(mp4Path);
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
		recordButton = findViewById(R.id.recordButton);
		recordButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
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
	}

	private void initCamera(int id) {
		mCamera = Camera.open(id);
		Camera.Parameters parameters = mCamera.getParameters();
//		parameters.setPictureFormat(ImageFormat.JPEG);
//		parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
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

//		mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));

		mp4Path = getOutputMediaPath();
		mMediaRecorder.setOutputFile(mp4Path);

		mMediaRecorder.setPreviewDisplay(mHolder.getSurface());
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
			mMediaRecorder.stop();
			mMediaRecorder.reset();
			mMediaRecorder.release();
			mMediaRecorder = null;
			mCamera.lock();
//			videoView.stopPlayback();
//			videoView.re
//			videoView.setVideoPath("");
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
//		Log.d(TAG, "record: "+isRecording);
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

}
