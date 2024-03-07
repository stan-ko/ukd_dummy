package ua.ukd.dummy.net;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide; // Import Glide library
import java.util.List;

import ua.ukd.dummy.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_user, parent, false);
        return new UserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        // tvUserName.setText(user.getFirstName() + " " + user.getLastName());
        holder.tvFirstName.setText(user.getFirstName());
        holder.tvLastName.setText(user.getLastName());
        holder.tvUserEmail.setText(user.getEmail());

        // Load avatar using Glide
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                    .placeholder(R.drawable.ic_camera) // Placeholder image (optional)
//                    .error(R.drawable.error_avatar) // Error image (optional)
                .into(holder.ivUserAvatar);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateData(List<User> updatedUserList) {
        userList.clear();
        userList.addAll(updatedUserList);
        notifyDataSetChanged();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFirstName;
        private final TextView tvLastName;
        private final TextView tvUserEmail;
        private final ImageView ivUserAvatar;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstName = itemView.findViewById(R.id.tvFirstName);
            tvLastName = itemView.findViewById(R.id.tvLastName);
            tvUserEmail = itemView.findViewById(R.id.tvEmail);
            ivUserAvatar = itemView.findViewById(R.id.ivAvatar);
        }
    }
}
