package com.linyuting.opencvtest;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.TextView;

import com.microsoft.projectoxford.face.FaceServiceClient;
import com.microsoft.projectoxford.face.contract.Face;

import java.io.InputStream;


public class FaceRecognizeRequest extends AsyncTask<InputStream, String, Face[]> {
    private boolean mSucceed = true;
    private int viewId;
    private Bitmap bitmap;
    private RecognizeHelper recognizeHelper;

    public FaceRecognizeRequest(int viewId) {
        this.viewId = viewId;
    }

    public  FaceRecognizeRequest(int viewId, Bitmap bitmap, RecognizeHelper recognizeHelper) {
        this.viewId = viewId;
        this.bitmap = bitmap;
        this.recognizeHelper = recognizeHelper;
    }


    @Override
    protected Face[] doInBackground(InputStream... params) {
        // Get an instance of face service client to detect faces in image.
        FaceServiceClient faceServiceClient = FaceServiceClientInstance.getFaceServiceClient();
        try {
            publishProgress("Detecting...");

            // Start detection.
            return faceServiceClient.detect(
                    params[0],  /* Input stream of image to detect */
                    true,       /* Whether to return face ID */
                    true,       /* Whether to return face landmarks */
                        /* Which face attributes to analyze, currently we support:
                           age,gender,headPose,smile,facialHair */
                    new FaceServiceClient.FaceAttributeType[]{
                            FaceServiceClient.FaceAttributeType.Age,
                            FaceServiceClient.FaceAttributeType.Gender,
                            FaceServiceClient.FaceAttributeType.Glasses,
                            FaceServiceClient.FaceAttributeType.Smile,
                            FaceServiceClient.FaceAttributeType.HeadPose
                    });
        } catch (Exception e) {
            mSucceed = false;
            publishProgress(e.getMessage());
            return null;
        }
    }

    @Override
    protected void onProgressUpdate(String... progress) {

    }

    @Override
    protected void onPostExecute(Face[] result) {
        recognizeHelper.step();

        final int fibalViewId = viewId;

        if (result == null || result.length < 1) {
            AppHelper.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    recognizeHelper.step();
                    AppHelper.activity.DeleteImageView(fibalViewId);
                }
            });
        } else {
            for (Face r : result) {
                final Face rTmp = r;
                AppHelper.mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AppHelper.activity.setFaceByTag(viewId, new RecognizeTextView.Face(rTmp.faceAttributes.gender, String.valueOf(rTmp.faceAttributes.age)));
                    }
                });

            }
        }

        AppHelper.FaceAnalysing = false;


    }
}


