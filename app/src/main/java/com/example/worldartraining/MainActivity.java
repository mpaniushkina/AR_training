package com.example.worldartraining;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.TrackingState;
import com.google.ar.core.exceptions.UnavailableApkTooOldException;
import com.google.ar.core.exceptions.UnavailableArcoreNotInstalledException;
import com.google.ar.core.exceptions.UnavailableSdkTooOldException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    ArFragment arFragment;
    ModelRenderable earthPostRenderable;
    FloatingActionButton floatingActionButton;
    AnchorNode anchorNode;
    private LocationScene locationScene;
    private Session session;
    private Snackbar mMessageSnackbar;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        setContentView(R.layout.activity_main);
        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

        floatingActionButton = findViewById(R.id.floatingActionButton);
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ModelRenderable.builder()
//                        .setSource(MainActivity.this, Uri.parse("model.sfb"))
//                        .build()
//                        .thenAccept(renderable -> earthPostRenderable = renderable)
//                        .exceptionally(throwable -> {
//                            Toast toast =
//                                    Toast.makeText(MainActivity.this, "Unable to load object renderable", Toast.LENGTH_LONG);
//                            toast.setGravity(Gravity.CENTER, 0, 0);
//                            toast.show();
//                            return null;
//                        });
//
//            }
//        });

        ModelRenderable.builder()
                .setSource(MainActivity.this, Uri.parse("model.sfb"))
                .build()
                .thenAccept(renderable -> earthPostRenderable = renderable)
                .exceptionally(throwable -> {
                    Toast toast =
                            Toast.makeText(MainActivity.this, "Unable to load object renderable", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    return null;
                });

        arFragment.setOnTapArPlaneListener(
                (HitResult hitresult, Plane plane, MotionEvent motionevent) -> {
                    if (earthPostRenderable == null) {
                        return;
                    }
                    Vector3 vector3 = new Vector3((float) 0.5, (float) -0.5, (float) 0.5);
                    Anchor anchor = hitresult.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arFragment.getArSceneView().getScene());

                    TransformableNode lamp = new TransformableNode(arFragment.getTransformationSystem());
                    lamp.setParent(anchorNode);
                    lamp.setRenderable(earthPostRenderable);
                    lamp.select();
                    lamp.setLocalPosition(vector3);
                }
        );

//        Exception exception = null;
//        String message = null;
//        try {
//            session = new Session(this);
//        } catch (UnavailableArcoreNotInstalledException e) {
//            message = "Please install ARCore";
//            exception = e;
//        } catch (UnavailableApkTooOldException e) {
//            message = "Please update ARCore";
//            exception = e;
//        } catch (UnavailableSdkTooOldException e) {
//            message = "Please update this app";
//            exception = e;
//        } catch (Exception e) {
//            message = "This device does not support AR";
//            exception = e;
//        }
//
//        if (message != null) {
//            showSnackbarMessage(message, true);
//            Log.e(TAG, "Exception creating session", exception);
//            return;
//        }
//
//
//        locationScene = new LocationScene(this, this, session);
//        locationScene.mLocationMarkers.add(
//                new com.example.worldartraining.LocationMarker(
//                        42.1419,
//                        57.1419,
//                        new AnnotationRenderer("Buckingham Palace")
//                )
//        );
//
//        locationScene.mLocationMarkers.add(
//                new LocationMarker(
//                        42.2945,
//                        57.858222,
//                        new ImageRenderer("eiffel.png")
//                )
//        );

//        // Find a position half a meter in front of the user.
//        Vector3 cameraPos = arFragment.getArSceneView().getScene().getCamera().getWorldPosition();
//        Vector3 cameraForward = arFragment.getArSceneView().getScene().getCamera().getForward();
//        Vector3 position = Vector3.add(cameraPos, cameraForward.scaled(0.5f));
//
//// Create an ARCore Anchor at the position.
//        Pose pose = Pose.makeTranslation(position.x, position.y, position.z);
//        if (arFragment.getArSceneView().getSession() != null) {
//            Anchor anchor = arFragment.getArSceneView().getSession().createAnchor(pose);
//
//// Create the Sceneform AnchorNode
//            AnchorNode anchorNode = new AnchorNode(anchor);
//            anchorNode.setParent(arFragment.getArSceneView().getScene());
//
//// Create the node relative to the AnchorNode
//            Node node = new Node();
//            node.setParent(anchorNode);
//        }

//        arFragment.getArSceneView().getScene().setOnUpdateListener(this::onSceneUpdate);
    }

    public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }

    private void onSceneUpdate(FrameTime frameTime) {
        // Let the fragment update its state first.
        arFragment.onUpdate(frameTime);

        // If there is no frame then don't process anything.
        if (arFragment.getArSceneView().getArFrame() == null) {
            return;
        }

        // If ARCore is not tracking yet, then don't process anything.
        if (arFragment.getArSceneView().getArFrame().getCamera().getTrackingState() != TrackingState.TRACKING) {
            return;
        }

        // Place the anchor 1m in front of the camera if anchorNode is null.
        if (this.anchorNode == null) {
            session = arFragment.getArSceneView().getSession();
            float[] pos = { 0,0,-1 };
            float[] rotation = {0,0,0,1};
            if (session != null) {
                Anchor anchor = session.createAnchor(new Pose(pos, rotation));
                anchorNode = new AnchorNode(anchor);
                anchorNode.setRenderable(earthPostRenderable);
                anchorNode.setParent(arFragment.getArSceneView().getScene());
            }
        }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        locationScene.resume();
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        locationScene.pause();
//    }

    private void showSnackbarMessage(String message, boolean finishOnDismiss) {
        mMessageSnackbar = Snackbar.make(
                MainActivity.this.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_INDEFINITE);
        mMessageSnackbar.getView().setBackgroundColor(0xbf323232);
        if (finishOnDismiss) {
            mMessageSnackbar.setAction(
                    "Dismiss",
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mMessageSnackbar.dismiss();
                        }
                    });
            mMessageSnackbar.addCallback(
                    new BaseTransientBottomBar.BaseCallback<Snackbar>() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            super.onDismissed(transientBottomBar, event);
                            finish();
                        }
                    });
        }
        mMessageSnackbar.show();
    }

}