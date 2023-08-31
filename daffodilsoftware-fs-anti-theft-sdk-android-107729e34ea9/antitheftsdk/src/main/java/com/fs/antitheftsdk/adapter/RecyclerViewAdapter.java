package com.fs.antitheftsdk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fs.antitheftsdk.R;
import com.fs.antitheftsdk.base.Constants;
import com.fs.antitheftsdk.permissions.PermissionsUtil;

/* This class is the recycler adapter of permission activity */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.PermissionViewHolder> {
    private String[] data;
    private Context context;

    public RecyclerViewAdapter(String[] data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public PermissionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycle_row, parent, false);
        return new PermissionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PermissionViewHolder holder, int position) {
        String title = data[position];
        if (title.equals(Constants.ADMIN)) {
            if (Boolean.FALSE.equals(PermissionsUtil.checkadminPermission(context.getApplicationContext()))) {
                holder.txtHeading.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtHeading.setText(R.string.admin_permission_heading);
                holder.txtContent.setText(R.string.admin_permission_content);
            } else {
                holder.txtHeading.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.GONE);
            }
        }
        if (title.equals(Constants.LOCATION)) {
            if (Boolean.FALSE.equals(PermissionsUtil.checkLocationPermission(context))) {
                holder.txtHeading.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtHeading.setText(R.string.background_location_heading);
                holder.txtContent.setText(R.string.background_location_content);
            } else {
                holder.txtHeading.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.GONE);
            }
        }
        if (title.equals(Constants.CAMERA)) {
            if (Boolean.FALSE.equals(PermissionsUtil.checkCameraPermission(context))) {
                holder.txtHeading.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtHeading.setText(R.string.camera_heading);
                holder.txtContent.setText(R.string.camera_content);
            } else {
                holder.txtHeading.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.GONE);
            }
        }
        if (title.equals(Constants.DISPLAY_OVERLAY)) {
            if (Boolean.FALSE.equals(PermissionsUtil.checkDisplayOverlayPermission(context))) {
                holder.txtHeading.setVisibility(View.VISIBLE);
                holder.txtContent.setVisibility(View.VISIBLE);
                holder.txtHeading.setText(R.string.display_overlay_heading);
                holder.txtContent.setText(R.string.display_overlay_content);
            } else {
                holder.txtHeading.setVisibility(View.GONE);
                holder.txtContent.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.length;
    }

    /*view holder of permission activity */
    public class PermissionViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeading;
        TextView txtContent;

        public PermissionViewHolder(@NonNull View itemView) {
            super(itemView);
            txtHeading = (TextView) itemView.findViewById(R.id.permission_title);
            txtContent = (TextView) itemView.findViewById(R.id.permission_content);
        }
    }
}