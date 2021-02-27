package com.jihong.footprint.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jihong.footprint.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue requestQueue;

    // Google 지도
    private GoogleMap mMap;

    // 데이터
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // requestQueue
        requestQueue = Volley.newRequestQueue(this);

        // 툴바 설정
        setToolbar();

        // 커버 이미지 그리기
        String getCoverString = getIntent().getStringExtra("coverImage");
        ImageView coverImage = findViewById(R.id.img_cover);
        Glide.with(this).asBitmap()
                .load(getCoverString)
                .into(coverImage);

        // content id 받아오기
        String contentid = getIntent().getStringExtra("contentid");

        // data 파싱
        String url = getString(R.string.rest_url) + "/get-detail.php?contentId=" + contentid;
        getData(url);

        // 이미지 가져오기
        url = getString(R.string.rest_url) + "/get-image.php?contentId=" + contentid;
        getImage(url);

        // 지도 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // 툴바 설정
    public void setToolbar() {
        // title 받아오기
        title = getIntent().getStringExtra("title");

        // 툴바 그림자 없애기
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);

        // 툴바 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 툴바 텍스트 보이고 안보이게
        CollapsingToolbarLayout collapsingToolbar = findViewById(R.id.toolbar_layout);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(title);
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    // 데이터 파싱
    public void getData(String url) {
        // Json 파싱
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String overview = "";
                    String title = "";

                    try {
                        title = jsonObject.getString("title");

                        overview = jsonObject.getString("overview");
                        overview = overview.replaceAll("<br />", "\n");
                        overview = overview.replaceAll("<[^>]*>", " ");
                    } catch (Exception e) {
                        e.toString();
                    }

                    TextView text_title = findViewById(R.id.text_title);
                    TextView text_description_content = findViewById(R.id.text_description_content);

                    text_title.setText(title);
                    text_description_content.setText(overview);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }

    // 이미지 가져오기
    public void getImage(String url) {
        // 이미지 슬라이드
        ImageSlider imageSlider = findViewById(R.id.img_slider);
        List<SlideModel> slideModels = new ArrayList<>();

        // Json 파싱
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String originimgurl = "";

                    try {
                        originimgurl = jsonObject.getString("originimgurl");
                        slideModels.add(new SlideModel(originimgurl, null));
                    } catch (Exception e) {
                        e.toString();
                    }
                }
                imageSlider.setImageList(slideModels, true);

                if (jsonArray.length() == 0) {
                    TextView text_image_title = findViewById(R.id.text_image_title);
                    text_image_title.setVisibility(View.GONE);
                    imageSlider.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }

    // 지도 설정
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // 위도와 경도 받아오기
        String mapx = getIntent().getStringExtra("mapx");
        String mapy = getIntent().getStringExtra("mapy");

        // 지도에 핀 추가
        mMap = googleMap;
        LatLng position = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(position);
        markerOptions.title(title);

        // 커스텀 지도 핀
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker);
        Bitmap bitmap = bitmapDrawable.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }

    // 크게보기
    public void onBigger(View view) {
        // 위도와 경도 받아오기
        String mapx = getIntent().getStringExtra("mapx");
        String mapy = getIntent().getStringExtra("mapy");

        // 액티비티 이동
        Intent intent = new Intent(this, MapActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("mapx", mapx);
        intent.putExtra("mapy", mapy);
        startActivity(intent);
    }
}
