package ua.ukd.dummy.net;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.ukd.dummy.R;

public class NetActivity extends AppCompatActivity {

    private static boolean useDynamicBuildView = true;

    private ViewGroup rootView;
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private View progress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!useDynamicBuildView) {
            // використання статичної вью для активіті (activity_net)
            setContentView(R.layout.activity_net);
            recyclerView = findViewById(R.id.recyclerView);
            progress = findViewById(R.id.progress);
        } else {
            // використання динамічної вью для активіті, побудова "на льоту"
            rootView = new FrameLayout(this);
            ViewGroup.LayoutParams commonLP = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            rootView.setLayoutParams(commonLP);
            setContentView(rootView);
            recyclerView = new RecyclerView(this);
            recyclerView.setLayoutParams(commonLP);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            rootView.addView(recyclerView);
            progress = getLayoutInflater().inflate(R.layout.progress, null);
            rootView.addView(progress);
        }
        // загальна частина
        userAdapter = new UserAdapter(new ArrayList<>(0));
        recyclerView.setAdapter(userAdapter);
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        getNetData();
    }

    private void getNetData() {
        progress.setVisibility(View.VISIBLE);
        final UserService userService = RetrofitClient.getUserService();

        userService.getUsers(10).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        List<User> userList = response.body().getUsers();
                        userAdapter.updateData(userList);
                    }
                    // Populate your RecyclerView adapter with userList
                } else {
                    // Handle error
                    showErrorDialog();
                }
                progress.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                progress.setVisibility(View.GONE);
                // Handle failure
                showErrorDialog();
            }
        });

    }

    private void showErrorDialog() {
        new MaterialAlertDialogBuilder(this)
                .setCancelable(false)
                .setTitle(R.string.network_api_error)
                .setMessage(R.string.something_went_wrong)
                .setPositiveButton(R.string.ok, (dialog1, which) -> dialog1.dismiss())
                .show();
    }
}
