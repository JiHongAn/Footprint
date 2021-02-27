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

        if (footItems.get(position).getContenttypeid().equals("12")) {
            holder.text_content_type.setText("관광지");
        } else if (footItems.get(position).getContenttypeid().equals("14")) {
            holder.text_content_type.setText("문화시설");
        } else if (footItems.get(position).getContenttypeid().equals("15")) {
            holder.text_content_type.setText("행사");
        } else if (footItems.get(position).getContenttypeid().equals("25")) {
            holder.text_content_type.setText("여행코스");
        } else if (footItems.get(position).getContenttypeid().equals("32")) {
            holder.text_content_type.setText("숙박");
        } else if (footItems.get(position).getContenttypeid().equals("39")) {
            holder.text_content_type.setText("음식점");
        } else {
            holder.text_content_type.setText("기타");
        }
    }

    @Override
    public int getItemCount() {
        return footItems.size();
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView text_content_type;
    ImageView thumbnail;

    public ViewHolder(@NonNull View convertView, ArrayList<Foot> footItems) {
        super(convertView);

        title = convertView.findViewById(R.id.text_title);
        text_content_type = convertView.findViewById(R.id.text_content_type);
        thumbnail = convertView.findViewById(R.id.img_thumbnail);

        convertView.setOnClickListener(v -> {
            int position = getAdapterPosition();

            Intent intent = new Intent(v.getContext(), DetailActivity.class);
            intent.putExtra("title", footItems.get(position).getTitle());
            intent.putExtra("coverImage", footItems.get(position).getFirstimage());
            intent.putExtra("contentid", footItems.get(position).getContentid());
            intent.putExtra("mapx", footItems.get(position).getMapx());
            intent.putExtra("mapy", footItems.get(position).getMapy());
            v.getContext().startActivity(intent);
        });
    }
}