package ua.ukd.dummy;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static boolean isEmailValid(CharSequence email) {
        if (TextUtils.isEmpty(email))
            return false;
        else
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isPasswordValid(String pwd) {
        return !(TextUtils.isEmpty(pwd) || pwd.length() < 6);
    }

    public static File createTempImageFile(Context context) {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File image = null;
        try {
            image = File.createTempFile(
                    imageFileName,  // prefix
                    ".jpg",         // suffix
                    context.getCacheDir()      // directory
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public static void saveImageToJpeg(Context context, Uri imageUri, int quality) {
        try {
            final InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Create a file to save the image
            final File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            final File imageFile = new File(directory, "my_image.jpg");

            final FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
            Log.d("Utils", "Image saved to: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveImageToJpeg(
            Context context,
            Uri imageUri,
            int maxWidth,
            int maxHeight,
            int quality) {
        try {
//            // Set the desired dimensions (Full HD)
//            int maxWidth = 1920;
//            int maxHeight = 1080;

            // Create BitmapFactory.Options for downsampling
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true; // Only read image dimensions
            InputStream inputStream = context.getContentResolver().openInputStream(imageUri);
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Calculate the inSampleSize to downsample the image
            options.inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);

            // Decode the image with the calculated inSampleSize
            options.inJustDecodeBounds = false;
            inputStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            // Create a file to save the downsized image
            File directory = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(directory, "my_downsized_image.jpg");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            Log.d("Utils", "Image saved to: " + imageFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int originalWidth = options.outWidth;
        final int originalHeight = options.outHeight;
        int inSampleSize = 1;

        if (originalWidth > reqWidth || originalHeight > reqHeight) {
            final int halfWidth = originalWidth / 2;
            final int halfHeight = originalHeight / 2;

            while ((halfWidth / inSampleSize) >= reqWidth && (halfHeight / inSampleSize) >= reqHeight) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
