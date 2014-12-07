
package com.mathesoft.renderermodecomparison.opengles10empty;

import android.content.Context;
import android.opengl.GLSurfaceView;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 */
public class MyGLSurfaceView extends GLSurfaceView {

    private final MyGLRenderer mRenderer;

    public MyGLSurfaceView(Context context) {
        super(context);

        // Set the Renderer for drawing on the GLSurfaceView
        mRenderer = new MyGLRenderer(context);
        setRenderer(mRenderer);

        // Render the view always
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

   

}
