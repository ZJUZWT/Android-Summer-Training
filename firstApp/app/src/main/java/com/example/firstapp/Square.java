package com.example.firstapp;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class Square {

    private final int mProgram;
    private FloatBuffer vertexBuffer;
    private static final int COORDS_PER_VERTEX = 3;

    // 坐标数组中的顶点坐标个数
    static final int COORDINATES_PRE_VERTEX = 4;
    static float triangleCoords[] = {   // 以逆时针顺序;
            -0.5f,0.5f,0.0f,
            -0.5f,-0.5f,0.0f,
            0.5f,-0.5f,0.0f,
            -0.5f,0.5f,0.0f,
            0.5f,0.5f,0.0f,
            0.5f,-0.5f,0.0f,
    };

    float color[] = {0.01176470f, 0.8549019f, 0.772549019f, 1.0f};

    public Square() {
        // 初始化形状中顶点坐标数据的字节缓冲区
        // 通过 allocateDirect 方法获取到 DirectByteBuffer 实例
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(
                triangleCoords.length * 4
        );

        // 设置缓冲区使用设备硬件的原本字节顺序进行读取;
        byteBuffer.order(ByteOrder.nativeOrder());
        // ByteBuffer 是将数据移进移出通道的唯一方式，这里使用 “as” 方法从 ByteBuffer 中获得一个基本类型缓冲区
        vertexBuffer = byteBuffer.asFloatBuffer();
        // 把顶点坐标信息数组存储到 FloatBuffer
        vertexBuffer.put(triangleCoords);
        // 设置从缓冲区的第一个位置开始读取顶点坐标信息
        vertexBuffer.position(0);

        // 加载编译顶点渲染器
        int vertexShader = MyGLRenderer_Tri.loadShader(GLES20.GL_VERTEX_SHADER,
                vertexShaderCode);

        // 加载编译片元渲染器
        int fragmentShader = MyGLRenderer_Tri.loadShader(GLES20.GL_FRAGMENT_SHADER,
                fragmentShaderCode);

        // 创建空的程式 - create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram();

        // attach shader 代码 - add the vertex shader to program
        GLES20.glAttachShader(mProgram, vertexShader);

        // attach shader 代码 - add the fragment shader to program
        GLES20.glAttachShader(mProgram, fragmentShader);

        // 链接GLSL程式 - creates OpenGL ES program executables
        GLES20.glLinkProgram(mProgram);

    }
    private int mPositionHandle; //变量 用于存取attribute修饰的变量的位置编号
    private int mColorHandle; //变量 用于存取uniform修饰的变量的位置编号

    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public void draw() {
        // 使用GLSL程式 - Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram);

        // 获取shader代码中的变量索引 get handle to vertex shader's vPosition member
        // Java代码中需要获取shader代码中定义的变量索引，用于在后面的绘制代码中进行赋值
        // 变量索引在GLSL程式生命周期内（链接之后和销毁之前）都是固定的，只需获取一次
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");

        // 绑定vertex坐标值 调用glVertexAttribPointer()告诉OpenGL，它可以在
        // 缓冲区vertexBuffer中获取vPosition的数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

        // 启用vertex Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(mPositionHandle);

        // get handle to fragment shader's vColor member
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);

        // 通过 GLES20.glDrawArrays 或者 GLES20.glDrawElements 开始绘制
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    private final String vertexShaderCode =
            "attribute vec4 vPosition;" +  // 应用程序传入顶点着色器的顶点位置
                    "void main() {" +
                    "  gl_Position = vPosition;" + // 设置此次绘制此顶点位置
                    "}";
    private final String fragmentShaderCode =
            "precision mediump float;" +  // 设置工作精度
                    "uniform vec4 vColor;" +  // 应用程序传入着色器的颜色变量
                    "void main() {" +
                    "  gl_FragColor = vColor;" + // 颜色值传给 gl_FragColor内建变量，完成片元的着色
                    "}";
}
