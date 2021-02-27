package com.jihong.footprint.activity;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jihong.footprint.R;

public class BiggerMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Google 지도
    private GoogleMap mMap;

    // DetailActivity에서 받아올 데이터
    private String title = "";
    private String mapx = "";
    private String mapy = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bigger_map);

        // 툴바 뒤로가기 버튼
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // title 받아오기
        title = getIntent().getStringExtra("title");

        // 위도와 경도 받아오기
        mapx = getIntent().getStringExtra("mapx");
        mapy = getIntent().getStringExtra("mapy");

        // 지도 설정
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    // 지도 설정
    @Override
    public void onMapReady(final GoogleMap googleMap) {
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

        // 마커 추가
        mMap.addMarker(markerOptions);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 16));
    }
}
