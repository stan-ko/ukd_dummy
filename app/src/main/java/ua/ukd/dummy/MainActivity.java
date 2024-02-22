package ua.ukd.dummy;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

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

//        Switch sw = findViewById(R.id.swSwitch);
//        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(MainActivity.this, "Switch is " + (b ? "On" : "Off"), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        CheckBox chk = findViewById(R.id.checkBox);
//        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                Toast.makeText(MainActivity.this, "CheckBox is " + (b ? "Checked" : "Unchecked"), Toast.LENGTH_SHORT).show();
//            }
//        });

        TextView tvResult = findViewById(R.id.tvResult);
        tvResult.setVisibility(View.VISIBLE);
        // tvResult.setText(R.string.button_label);
        View btCheck = findViewById(R.id.btCheck);
        btCheck.setOnClickListener(view -> {
            boolean isPalindrome = checkIsPalindrome();
            if (isPalindrome)
                tvResult.setText(R.string.yes);
            else
                tvResult.setText(R.string.no);
        });

        // Camera_open button is for open the camera and add the setOnClickListener in this button
        TextView btCam = findViewById(R.id.btCam);
        btCam.setOnClickListener(v -> checkCameraPermission());

        View btAppInfo = findViewById(R.id.btnAppInfo);
        btAppInfo.setOnClickListener(view -> showInfo());

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


    ActivityResultLauncher<String> requestCameraPermission = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            isGranted -> {
                if (isGranted) {
                    // Permission granted, you can proceed with camera-related tasks
                    takePhoto();
                } else {
                    showNoCameraPermissionDialog();
                }
            }
    );


    private void showNoCameraPermissionDialog() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.dialog_camera_permission_title)
                .setMessage(R.string.dialog_permission_camera_message)
                .setNeutralButton(R.string.open_settings, (dialog1, which) -> {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                })
                .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss())
                .show();
    }

    private void checkCameraPermission() {
        requestCameraPermission.launch(Manifest.permission.CAMERA);
    }

    final ActivityResultLauncher<Intent> startForCameraResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // Handle the result here (e.g., process the photo)
                    final Intent data = result.getData();
                    if (data != null) {
                        // Get the full-resolution photo (not just the thumbnail)
                        final Uri photoUri = data.getData();
                        // Now you can work with the photoUri (e.g., save it to a file)
                        // Utils.saveImageToJpeg(MainActivity.this, photoUri, 77);

                        // and load it into an ImageView for display
                        // ...
                        // thumbnail image?
                        final Bitmap photo = (Bitmap) data.getExtras().get("data");
                        // Set the image in imageview for display
                        final ImageView ivImg = findViewById(R.id.ivPhoto);
                        ivImg.setImageBitmap(photo);
                    }
                }
            }
    );

    private void takePhoto() {
        // modern activity result API
        // Launch the camera activity
        final Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startForCameraResult.launch(cameraIntent);
    }


    private void showInfo() {
        // Create a BottomSheetDialog
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);

        // Inflate your custom layout for the bottom sheet
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet_layout, null);

        TextView txtInfo = bottomSheetView.findViewById(R.id.txtInfo);
        txtInfo.setText(R.string.app_info);

        // Set any listeners or perform actions on views within the bottom sheet
        // For example:
        bottomSheetView
                .findViewById(R.id.btnClose)
                .setOnClickListener(v -> bottomSheetDialog.dismiss());

        // Set the custom view for the bottom sheet
        bottomSheetDialog.setContentView(bottomSheetView);

        // Show the bottom sheet
        bottomSheetDialog.show();
    }
}
