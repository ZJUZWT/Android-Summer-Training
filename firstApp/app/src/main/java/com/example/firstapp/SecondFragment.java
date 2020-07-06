package com.example.firstapp;

import android.opengl.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment {

//    private GLSurfaceView testGLView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        testGLView = new GLSurfaceView(this.getContext());

    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
        // Inflate the layout for this fragment

//        setContentView


        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.button_second).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }
}
//
//public interface MyRenderer extends GLSurfaceView.Renderer {
//    // Surface创建的时候调用
//    @Override
//    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
//        // 设置清屏颜色
//        gl.glClearColor(0f, 1f, 0f, 0f);
//    }
//    // Surface改变的的时候调用
//    @Override
//    public void onSurfaceChanged(GL10 gl, int width, int height) {
//        // 设置窗口大小
//        gl.glViewport(width / 4, width / 2, width / 2, height / 2);
//    }
//    // 在Surface上绘制的时候调用
//    @Override
//    public void onDrawFrame(GL10 gl) {
//        //设置渲染模式
//        glSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
//        // 清除屏幕
//        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
//    }
//}

//public class MyGLRenderer implements GLSurfaceView.Renderer {
//
//    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
//        // Set the background frame color
//        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
//    }
//
//    public void onDrawFrame(GL10 unused) {
//        // Redraw background color
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//    }
//
//    public void onSurfaceChanged(GL10 unused, int width, int height) {
//        //利用glViewport()设置Screen space的大小，在onSurfaceChanged中回调
//        GLES20.glViewport(0, 0, width, height);
//    }
//}
//
//public class MyGLSurfaceView extends GLSurfaceView {
//
//    private final MyGLRenderer mRenderer;
//
//    public MyGLSurfaceView(Context context){
//        super(context);
//
//        // Create an OpenGL ES 2.0 context
//        setEGLContextClientVersion(2);
//
//        mRenderer = new MyGLRenderer();
//
//        // Set the Renderer for drawing on the GLSurfaceView
//        setRenderer(mRenderer);
//        // Set the RenderMode
//        mGLSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
//    }
//}