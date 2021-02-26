package com.jihong.footprint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jihong.footprint.R;
import com.jihong.footprint.activity.DetailActivity;
import com.jihong.footprint.model.Foot;

import java.util.ArrayList;

public class FootAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Activity activity;
    private ArrayList<Foot> footItems;

    public FootAdapter(Activity activity, ArrayList<Foot> footItems) {
        this.activity = activity;
        this.footItems = footItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.item_foot, parent, false);
        return new ViewHolder(view, footItems);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 썸네일 가져오기
        Glide.with(activity).asBitmap()
                .load(footItems.get(position).getFirstimage())
                .override(512, 512)
                .centerCrop()
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                .into(holder.thumbnail);

        holder.title.setText(footItems.get(position).getTitle());

    }

    @Override
    public int getItemCount() {
        return footItems.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    ImageView thumbnail;

    public ViewHolder(@NonNull View convertView, ArrayList<Foot> footItems) {
        super(convertView);

        title = convertView.findViewById(R.id.text_title);
        thumbnail = convertView.findViewById(R.id.img_thumbnail);

        convertView.setOnClickListener(v -> {
            int position = getAdapterPosition();

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("title", footItems.get(position).getTitle());
            intent.putExtra("coverImage", footItems.get(position).getFirstimage());
            intent.putExtra("contentid", footItems.get(position).getContentid());
            v.getContext().startActivity(intent);
        });
    }
}