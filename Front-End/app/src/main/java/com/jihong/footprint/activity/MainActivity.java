package com.jihong.footprint.activity;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.jihong.footprint.R;
import com.jihong.footprint.adapter.FootAdapter;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // requestQueue
        requestQueue = Volley.newRequestQueue(this);

        // recyclerView
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new FootAdapter(this, footList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // 여행지 정보를 받아올 url
        String mapX = "126.981";
        String mapY = "37.568";
        String url = getString(R.string.rest_url) + "/get-around.php?mapX=" + mapX + "&mapY=" + mapY;

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
    }
}
