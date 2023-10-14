package org.tensorflow.blindhelp.examples.classification;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.view.SurfaceView;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import org.tensorflow.blindhelp.examples.classification.activities.HomePage;
import org.tensorflow.blindhelp.examples.classification.activities.helpDesk;
import org.tensorflow.lite.*;
import org.tensorflow.lite.DataType;
import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.ml.Modelone;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Locale;

public class CameraTest extends AppCompatActivity implements TextToSpeech.OnInitListener, PictureCallback, SurfaceHolder.Callback,View.OnLongClickListener {



    private TextToSpeech tts;
    private TextView result;
    private Button picture;
    private int imageSize = 224;
    private Camera camera;
    private SurfaceView surfaceView;
    private boolean capturing = false;
    private SurfaceHolder surfaceHolder;
    private RelativeLayout relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_test);

        tts = new TextToSpeech(this, this);
        result = findViewById(R.id.result);

        relative=findViewById(R.id.dd);

        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);



        relative.setOnLongClickListener(this);

    }

    public void layoutClicked(View view)
    {
        if(capturing==false){
            captureImage();}
    }





    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            // Open the camera
            camera = Camera.open();
            camera.setDisplayOrientation(90);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (Exception e) {
            // Handle camera error
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }
    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);

        if (image != null) {
            // Display the captured image in the ImageView

            // Resize the image to your desired dimensions
            Bitmap resizedImage = ThumbnailUtils.extractThumbnail(image, imageSize, imageSize);

            // Perform image classification on the resized image
            classifyImage(resizedImage);

            // Close and reopen the camera
            closeAndReopenCamera();
        } else {
            // Handle the case where the captured image is null
            // This can happen if there was an issue with capturing the image
        }
    }

    // Method to trigger image capture
    private void captureImage() {
        capturing=true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                if (camera != null) {
                    camera.takePicture(null, null, this);
                }
            } else {
                // Request camera permission if we don't have it.
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
            }
        }
    }

    // Close and reopen the camera
    private void closeAndReopenCamera() {



        if (camera != null) {
            camera.stopPreview();
            camera.release();
        }

        // Open the camera after a short delay
        final int reopenDelay = 500; // 2 seconds
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        try {
                            // Reopen the camera
                            camera = Camera.open();
                            camera.setDisplayOrientation(90);
                            camera.setPreviewDisplay(surfaceHolder);
                            camera.startPreview();
                            capturing=false;
                        } catch (Exception e) {
                            // Handle camera error
                        }
                    }
                },
                reopenDelay
        );
    }

    // Your image classification code
    public void classifyImage(Bitmap image){
        try {
            Modelone model = Modelone.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int [] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for(int i = 0; i < imageSize; i++){
                for(int j = 0; j < imageSize; j++){
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }

            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Modelone.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for(int i = 0; i < confidences.length; i++){
                if(confidences[i] > maxConfidence){
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }
            String[] classes = {"1000", "500","100"};
            result.setText(classes[maxPos]);
            speak("THis is "+classes[maxPos]);

            String s = "";
            for(int i = 0; i < classes.length; i++){
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }



            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setLanguage(Locale.UK);
           tts.speak("This Currency Scanner. Single tap any place for Capture Currency and long press to Navigate Home.", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    private File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraTest");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + System.currentTimeMillis() + ".jpg");
    }

    @Override
    public boolean onLongClick(View v) {

        Intent intent;
        if (v.getId() == R.id.dd) {
            intent = new Intent(this, HomePage.class);
            startActivity(intent);
            return true;
        }
        return false;
    }
}
