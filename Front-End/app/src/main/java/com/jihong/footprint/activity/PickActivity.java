package com.jihong.footprint.activity;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jihong.footprint.R;
import com.jihong.footprint.helper.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PickActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Google 지도
    private GoogleMap mMap;

    // 프리퍼런스
    PreferenceHelper preferenceHelper;

    // 저장된 좌표
    private String mapx = "";
    private String mapy = "";

    // 바뀐 좌표
    private String changeMapX = "";
    private String changeMapY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);

        // 툴바 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // 지도 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // 프리퍼런스 가져오기
        preferenceHelper = new PreferenceHelper();
        mapx = preferenceHelper.preferenceRead(this, "mapx");
        mapy = preferenceHelper.preferenceRead(this, "mapy");

        // 만약 비어있으면
        if (mapx.equals("") || mapy.equals("")) {
            // 기본 위치 서울시청으로 이동한다.
            mapx = "126.9757564";
            mapy = "37.5662952";
        }
    }

    // 지도 설정
    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        LatLng position = new LatLng(Double.parseDouble(mapy), Double.parseDouble(mapx));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));

        // 카메라 이동 이벤트
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                changeMapX = String.valueOf(cameraPosition.target.longitude);
                changeMapY = String.valueOf(cameraPosition.target.latitude);
            }
        });
    }

    // 버튼 클릭 이벤트
    public void onPick(View view) {
        preferenceHelper.preferenceWrite(this, "mapx", changeMapX);
        preferenceHelper.preferenceWrite(this, "mapy", changeMapY);
    }
}
