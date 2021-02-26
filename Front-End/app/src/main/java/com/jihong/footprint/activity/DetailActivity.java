package com.jihong.footprint.activity;

import android.os.Bundle;
import android.util.Log;
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
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jihong.footprint.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    RequestQueue requestQueue;

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
    }

    public void setToolbar() {
        // title 받아오기
        String title = getIntent().getStringExtra("title");

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
                        Log.e("?", originimgurl);
                        slideModels.add(new SlideModel(originimgurl, null));
                    } catch (Exception e) {
                        e.toString();
                    }
                }
                imageSlider.setImageList(slideModels, true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        requestQueue.add(jsonObjectRequest);
    }
}
