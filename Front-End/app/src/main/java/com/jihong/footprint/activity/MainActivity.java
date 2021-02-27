package com.jihong.footprint.activity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jihong.footprint.R;
import com.jihong.footprint.adapter.FootAdapter;
import com.jihong.footprint.helper.GetMyLocation;
import com.jihong.footprint.helper.PreferenceHelper;
import com.jihong.footprint.model.Foot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RequestQueue requestQueue;

    // 리스트뷰 파싱
    private ArrayList<Foot> footList = new ArrayList<Foot>();
    private RecyclerView recyclerView;
    private FootAdapter adapter;
    ProgressBar progress;

    // 프리퍼런스
    PreferenceHelper preferenceHelper;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // requestQueue
        requestQueue = Volley.newRequestQueue(this);

        // recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        progress = findViewById(R.id.progress);
        adapter = new FootAdapter(this, footList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 탭 설정
        ImageView homeImage = findViewById(R.id.homeImage);
        TextView lineHome = findViewById(R.id.lineHome);

        homeImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7CB3F9")));
        lineHome.setBackgroundColor(Color.parseColor("#7CB3F9"));
    }

    @Override
    public void onResume() {
        super.onResume();

        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        // 프리퍼런스 가져오기
        preferenceHelper = new PreferenceHelper();
        String mapx = preferenceHelper.preferenceRead(this, "mapx");
        String mapy = preferenceHelper.preferenceRead(this, "mapy");

        // 만약 비어있으면
        if (mapx.equals("") || mapy.equals("")) {
            // 기본 위치 서울시청으로 이동한다.
            mapx = "126.9757564";
            mapy = "37.5662952";
        }

        // 여행지 정보를 받아올 url
        String url = getString(R.string.rest_url) + "/get-around.php?mapX=" + mapx + "&mapY=" + mapy;

        // data 파싱
        getData(url);
    }

    // 데이터 파싱
    public void getData(String url) {
        footList.clear();

        // Json 파싱
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                url, null, response -> {
            try {
                JSONArray jsonArray = response.getJSONArray("data");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String addr1 = "";
                    String contentid = "";
                    String contenttypeid = "";
                    String dist = "";
                    String firstimage = "";
                    String mapx = "";
                    String mapy = "";
                    String title = "";

                    try {
                        addr1 = jsonObject.getString("addr1");
                        contentid = jsonObject.getString("contentid");
                        contenttypeid = jsonObject.getString("contenttypeid");
                        dist = jsonObject.getString("dist");
                        firstimage = jsonObject.getString("firstimage");
                        mapx = jsonObject.getString("mapx");
                        mapy = jsonObject.getString("mapy");
                        title = jsonObject.getString("title");
                    } catch (Exception e) {
                        e.toString();
                    }

                    if (!(firstimage.equals(""))) {
                        // ArrayList에 추가
                        Foot foot = new Foot();
                        foot.setAddr1(addr1);
                        foot.setContentid(contentid);
                        foot.setContenttypeid(contenttypeid);
                        foot.setDist(dist);
                        foot.setFirstimage(firstimage);
                        foot.setMapx(mapx);
                        foot.setMapy(mapy);
                        foot.setTitle(title);
                        footList.add(foot);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            // 파싱한 데이터 업데이트
            adapter.notifyDataSetChanged();
        }, error -> Log.e("Error", error.toString()));
        requestQueue.add(jsonObjectRequest);


        new Handler().postDelayed(() -> {
            recyclerView.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
        }, 700);
    }

    // 내 위치 가져오기
    public void onMyLocation(View view) {
        recyclerView.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);

        // 내 위치 좌표 가져오기
        GetMyLocation getMyLocation = new GetMyLocation(this);
        String mapx = String.valueOf(getMyLocation.getLongitude());
        String mapy = String.valueOf(getMyLocation.getLatitude());

        // 프리퍼런스 저장
        preferenceHelper.preferenceWrite(this, "mapx", mapx);
        preferenceHelper.preferenceWrite(this, "mapy", mapy);
        getMyLocation.stopUsingGPS();

        // 여행지 정보를 받아올 url
        String url = getString(R.string.rest_url) + "/get-around.php?mapX=" + mapx + "&mapY=" + mapy;

        // data 파싱
        getData(url);
    }

    // 지도 액티비티 이동
    public void tabMap(View view) {
        startActivity(new Intent(MainActivity.this, MapActivity.class));
        overridePendingTransition(0, 0);
        finish();
    }

    // 두번 눌러 종료
    private long time = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            finish();
        }
    }
}
