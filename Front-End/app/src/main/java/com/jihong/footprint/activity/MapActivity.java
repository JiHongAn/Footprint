package com.jihong.footprint.activity;

import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jihong.footprint.R;
import com.jihong.footprint.helper.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    RequestQueue requestQueue;

    // 프로그래스바
    ProgressBar progress;

    // 프리퍼런스
    PreferenceHelper preferenceHelper;

    // Google 지도
    private GoogleMap mMap;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        // requestQueue
        requestQueue = Volley.newRequestQueue(this);

        // 탭 설정
        ImageView mapImage = findViewById(R.id.mapImage);
        TextView lineMap = findViewById(R.id.lineMap);

        mapImage.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7CB3F9")));
        lineMap.setBackgroundColor(Color.parseColor("#7CB3F9"));

        // 지도 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // progress
        progress = findViewById(R.id.progress);
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
                        // 지도 핀으로 추가
                        LatLng position = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));
                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(position);
                        markerOptions.title(title);

                        // 커스텀 지도 핀
                        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.ic_marker);
                        Bitmap bitmap = bitmapDrawable.getBitmap();
                        Bitmap smallMarker = Bitmap.createScaledBitmap(bitmap, 120, 120, false);
                        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));

                        // 마커 추가
                        mMap.addMarker(markerOptions);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.e("Error", error.toString()));
        requestQueue.add(jsonObjectRequest);


        new Handler().postDelayed(() -> {
            progress.setVisibility(View.GONE);
        }, 700);
    }

    // 지도 설정
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        // 프리퍼런스 가져오기
        preferenceHelper = new PreferenceHelper();
        String mapx = preferenceHelper.preferenceRead(this, "mapx");
        String mapy = preferenceHelper.preferenceRead(this, "mapy");

        // 지도 설정
        mMap = googleMap;
        LatLng position = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
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
