package com.example.filterapp;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.zomato.photofilters.SampleFilters;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView img, filter1, filter2, filter3, filter4, filter5;
    Bitmap originalBitmap;
    Button camera, save;
    Bitmap refresh;

    File directory;
    String directorypath;
    OutputStream outputstream;


    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        img = (ImageView) findViewById(R.id.imgv1);
        filter1 = findViewById(R.id.filter1);
        filter2 = findViewById(R.id.filter2);
        filter3 = findViewById(R.id.filter3);
        filter4 = findViewById(R.id.filter4);
        filter5 = findViewById(R.id.filter5);
        camera = findViewById(R.id.camera);
        save = findViewById(R.id.save);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(MainActivity.this)
                        /* .crop()	    			//Crop image(Optional), Check Customization for more option
                         .compress(1024)			//Final image size will be less than 1 MB(Optional)
                         .maxResultSize(1080, 1080)*/    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage();
            }
        });

        //getting path where to store the image
        directorypath = Environment.getExternalStorageDirectory().getAbsolutePath();
        directory = new File(directorypath, "/DCIM");
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 201);
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 202);
            }

        }


        filter1.setOnClickListener(this);
        filter2.setOnClickListener(this);
        filter3.setOnClickListener(this);
        filter4.setOnClickListener(this);
        filter5.setOnClickListener(this);


        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        originalBitmap = drawable.getBitmap();
        refresh = originalBitmap;
    }

    private void saveImage() {

        String imagename = "img-" + SystemClock.currentThreadTimeMillis() + "-filteredimg" + ".jpg";
        File file2 = new File(directory, imagename);
        try {
            outputstream = new FileOutputStream(file2);
            BitmapDrawable drawablet = (BitmapDrawable) img.getDrawable();
            Bitmap bitmaptemp = drawablet.getBitmap();
            bitmaptemp.compress(Bitmap.CompressFormat.JPEG, 100, outputstream);
            outputstream.flush();
            outputstream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.filter1:

                Filter filter = new Filter();
                filter.addSubFilter(new BrightnessSubFilter(30));
                filter.addSubFilter(new ContrastSubFilter(1.1f));
                // BitmapDrawable drawable = (BitmapDrawable)img.getDrawable();
                // Bitmap bitmap = drawable.getBitmap();
                Bitmap image = refresh.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap outputImage = filter.processFilter(image);

                img.setImageBitmap(outputImage);
                break;


            case R.id.filter2:
                Filter BlueMessFilter = SampleFilters.getBlueMessFilter();
                // BitmapDrawable drawable1 = (BitmapDrawable)img.getDrawable();
                // Bitmap bitmap1 = drawable1.getBitmap();
                Bitmap BlueMessFilterimage = refresh.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap BlueMessFilteroutputImage = BlueMessFilter.processFilter(BlueMessFilterimage);

                img.setImageBitmap(BlueMessFilteroutputImage);
                break;


            case R.id.filter3:
                Filter StarLitFilter = SampleFilters.getStarLitFilter();
                Bitmap StarLitFilterimage = refresh.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap StarLitFilteroutputImage = StarLitFilter.processFilter(StarLitFilterimage);

                img.setImageBitmap(StarLitFilteroutputImage);
                break;


            case R.id.filter4:

                Filter AweStruckVibeFilter = SampleFilters.getAweStruckVibeFilter();
                Bitmap AweStruckVibeFilterimage = refresh.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap AweStruckVibeFilteroutputImage = AweStruckVibeFilter.processFilter(AweStruckVibeFilterimage);

                img.setImageBitmap(AweStruckVibeFilteroutputImage);
                break;


            case R.id.filter5:

                Filter LimeStutterFilter = SampleFilters.getLimeStutterFilter();
                Bitmap LimeStutterFilterimage = refresh.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap LimeStutterFilteroutputImage = LimeStutterFilter.processFilter(LimeStutterFilterimage);

                img.setImageBitmap(LimeStutterFilteroutputImage);
                break;


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();
        img.setImageURI(uri);
        filter1.setImageURI(uri);
        filter2.setImageURI(uri);
        filter3.setImageURI(uri);
        filter4.setImageURI(uri);
        filter5.setImageURI(uri);
        try {
            refresh = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}