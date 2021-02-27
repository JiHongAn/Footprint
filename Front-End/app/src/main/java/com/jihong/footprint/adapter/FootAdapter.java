package com.jihong.footprint.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jihong.footprint.R;
import com.jihong.footprint.activity.DetailActivity;
import com.jihong.footprint.activity.PickActivity;
import com.jihong.footprint.helper.PreferenceHelper;
import com.jihong.footprint.model.Foot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FootAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Activity activity;
    private ArrayList<Foot> footItems;

    // header 달기
    private final int TYPE_HEADER = 0;
    private final int TYPE_ITEM = 1;

    public FootAdapter(Activity activity, ArrayList<Foot> footItems) {
        this.activity = activity;
        this.footItems = footItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;

        View view;
        if (viewType == TYPE_HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            holder = new HeaderViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_foot, parent, false);
            holder = new ViewHolder(view, footItems);
        }
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;
        else
            return TYPE_ITEM;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            // Header
            HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.layout_search.setOnClickListener(v -> {
                Intent intent = new Intent(v.getContext(), PickActivity.class);
                v.getContext().startActivity(intent);
            });

            // 프리퍼런스 가져오기
            PreferenceHelper preferenceHelper = new PreferenceHelper();
            String mapx = preferenceHelper.preferenceRead(activity, "mapx");
            String mapy = preferenceHelper.preferenceRead(activity, "mapy");

            // 만약 비어있으면
            if (mapx.equals("") || mapy.equals("")) {
                // 기본 위치 서울시청으로 이동한다.
                mapx = "126.9757564";
                mapy = "37.5662952";
            }

            // 주소 가져오기
            Geocoder geocoder = new Geocoder(activity, Locale.KOREA);
            List<Address> address;
            try {
                if (geocoder != null) {
                    address = geocoder.getFromLocation(Double.parseDouble(mapy), Double.parseDouble(mapx), 1);
                    if (address != null && address.size() > 0) {
                        String[] addressData = address.get(0).getAddressLine(0).split(" ");

                        headerViewHolder.text_city.setText(addressData[1] + " " + addressData[2]);
                        headerViewHolder.text_full_city.setText(addressData[3]);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 여행지 데이터
            ViewHolder viewHolder = (ViewHolder) holder;

            // 썸네일 가져오기
            Glide.with(activity).asBitmap()
                    .load(footItems.get(position).getFirstimage())
                    .override(512, 512)
                    .centerCrop()
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(24)))
                    .into(viewHolder.thumbnail);

            viewHolder.title.setText(footItems.get(position).getTitle());

            if (footItems.get(position).getContenttypeid().equals("12")) {
                viewHolder.text_content_type.setText("관광지");
            } else if (footItems.get(position).getContenttypeid().equals("14")) {
                viewHolder.text_content_type.setText("문화시설");
            } else if (footItems.get(position).getContenttypeid().equals("15")) {
                viewHolder.text_content_type.setText("행사");
            } else if (footItems.get(position).getContenttypeid().equals("25")) {
                viewHolder.text_content_type.setText("여행코스");
            } else if (footItems.get(position).getContenttypeid().equals("32")) {
                viewHolder.text_content_type.setText("숙박");
            } else if (footItems.get(position).getContenttypeid().equals("39")) {
                viewHolder.text_content_type.setText("음식점");
            } else {
                viewHolder.text_content_type.setText("기타");
            }

            // 거리
            float dist = (Float.parseFloat(footItems.get(position).getDist())) / 1000;
            dist = (float) (Math.round(dist * 100) / 100.0);
            String distString = dist + "km";
            viewHolder.text_dist.setText(distString);
        }
    }

    @Override
    public int getItemCount() {
        return footItems.size();
    }
}

class HeaderViewHolder extends RecyclerView.ViewHolder {
    RelativeLayout layout_search;

    TextView text_city;
    TextView text_full_city;

    HeaderViewHolder(View headerView) {
        super(headerView);

        layout_search = headerView.findViewById(R.id.layout_search);

        text_city = headerView.findViewById(R.id.text_city);
        text_full_city = headerView.findViewById(R.id.text_full_city);
    }
}

class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView text_content_type;
    ImageView thumbnail;
    TextView text_dist;

    public ViewHolder(@NonNull View convertView, ArrayList<Foot> footItems) {
        super(convertView);

        title = convertView.findViewById(R.id.text_title);
        text_content_type = convertView.findViewById(R.id.text_content_type);
        thumbnail = convertView.findViewById(R.id.img_thumbnail);
        text_dist = convertView.findViewById(R.id.text_dist);

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