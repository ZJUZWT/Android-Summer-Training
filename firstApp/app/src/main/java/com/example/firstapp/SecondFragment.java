package com.example.firstapp;

import android.opengl.*;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Context;
import android.widget.Button;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

public class SecondFragment extends Fragment implements ToFragmentListener{

    private MyGLSurfaceView testGLView;

    @Override
    public void onTypeClick(String message) {
//        if ( message.equals("tri") ) testGLView.setMode(MyGLSurfaceView.TRI);
//        if ( message.equals("squ") ) testGLView.setMode(MyGLSurfaceView.SQU);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState ) {
//        return inflater.inflate(R.layout.fragment_show, container, false);
        testGLView = new MyGLSurfaceView(this.getActivity());
        return testGLView;
    }

    class MyGLSurfaceView extends GLSurfaceView {

        protected static final int TRI = 132 ;
        protected static final int SQU = 954 ;

        private final MyGLRenderer_Tri myGLRenderer_tri;
        private final MyGLRenderer_Squ myGLRenderer_squ;

        public MyGLSurfaceView(Context context){
            super(context);

            setEGLContextClientVersion(2);

            myGLRenderer_tri = new MyGLRenderer_Tri();
            myGLRenderer_squ = new MyGLRenderer_Squ();

            setRenderer(myGLRenderer_tri);
            setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
        }

        public void setMode(int mode) {
            if ( mode == TRI ) {
                setRenderer(myGLRenderer_tri);
                setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
            }
            if ( mode == SQU ) setRenderer(myGLRenderer_squ);
        }
    }
}