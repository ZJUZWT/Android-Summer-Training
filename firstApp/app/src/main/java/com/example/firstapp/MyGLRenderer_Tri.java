package com.example.firstapp;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MyGLRenderer_Tri implements GLSurfaceView.Renderer {

    float sizeRatio;
    private Triangle mTriangle;

    public void onSurfaceCreated(GL10 unused, EGLConfig config) {
        // Set the background frame color
//        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mTriangle = new Triangle();
    }

    public void onDrawFrame(GL10 unused) {
        // Redraw background color
//        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        mTriangle.draw();
    }

    public void onSurfaceChanged(GL10 unused, int width, int height) {
        //利用glViewport()设置Screen space的大小，在onSurfaceChanged中回调
        GLES20.glViewport(0, 0, width, height);
    }

    public static int loadShader(int type, String shaderCode){
        int shader = GLES20.glCreateShader(type);
        //加载shader代码 glShaderSource 和 glCompileShader
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }
}