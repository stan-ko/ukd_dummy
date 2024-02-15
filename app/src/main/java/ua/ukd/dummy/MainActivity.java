package ua.ukd.dummy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final int RC_CAMERA_PERMISSION = 1025;
    private static final int RC_CAMERA = 1024;
    private File photoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View editControls = findViewById(R.id.editControls);
        //editControls.setVisibility(View.INVISIBLE);
        View vp = findViewById(R.id.progress);
        EditText etWord = findViewById(R.id.etText);
        etWord.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                editControls.setVisibility(TextUtils.isEmpty(editable) ? View.INVISIBLE : View.VISIBLE);
                vp.setVisibility(TextUtils.isEmpty(editable) ? View.VISIBLE : View.GONE);
            }
        });

        Switch sw = findViewById(R.id.swSwitch);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(MainActivity.this, "Switch is " + (b ? "On" : "Off"), Toast.LENGTH_SHORT).show();
            }
        });

        CheckBox chk = findViewById(R.id.checkBox);
        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Toast.makeText(MainActivity.this, "CheckBox is " + (b ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
            }
        });

        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setVisibility(View.VISIBLE);
        // tvResult.setText(R.string.button_label);
        View btCheck = findViewById(R.id.btCheck);
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPalindrome = checkIsPalindrome();
                if (isPalindrome)
                    tvResult.setText(R.string.yes);
                else
                    tvResult.setText(R.string.no);
            }
        });

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        TextView btCam = findViewById(R.id.btCam);

        btCam.setOnClickListener(v -> {
            checkCameraPermissions();
        });

    }

    private void checkCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            takePhoto();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
            Toast.makeText(this,"No Camera Permission granted", Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, RC_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case RC_CAMERA_PERMISSION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePhoto();
                } else {
                    Toast.makeText(this,"No Camera Permission granted", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void takePhoto() {
        //            // browser activity start
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            String url = "https://www.stackoverflow.com";
//            intent.setData(Uri.parse(url));
//            startActivity(intent);
        // Create the camera_intent ACTION_IMAGE_CAPTURE it will open the camera for capture the image
        final File photoFile = createImageFile();
        if (photoFile != null) {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            // Start the activity with camera_intent, and request pic id
            startActivityForResult(cameraIntent, RC_CAMERA);
        }
    }

    private File createImageFile() {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    getCacheDir()      // directory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    private boolean checkIsPalindrome() {
        EditText etWord = findViewById(R.id.etText);
        String word = etWord.getText().toString();
//        public static boolean isPalindrome(String word) {
        int i = 0, j = word.length() - 1;
        while (i < j) {
            if (word.charAt(i) != word.charAt(j))
                return false;
            i++;
            j--;
        }
        return true;
    }

    // This method will help to retrieve the image
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Match the request 'pic id with requestCode
        if (requestCode == RC_CAMERA) {
            if (resultCode== Activity.RESULT_OK) {
                // BitMap is data structure of image file which store the image in memory
                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                // Set the image in imageview for display
                ImageView ivImg = findViewById(R.id.ivPhoto);
                ivImg.setImageBitmap(photo);
//                try {
//                    Bitmap photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
//                    ivImg.setImageBitmap(photo);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

}
