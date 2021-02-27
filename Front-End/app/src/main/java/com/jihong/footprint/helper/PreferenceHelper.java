package com.jihong.footprint.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceHelper {
    public PreferenceHelper() {
    }

    // 프리퍼런스 쓰기
    public void preferenceWrite(Context context, String writeName, String writeValue) {
        SharedPreferences pref = context.getSharedPreferences("com.jihong.footprint", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(writeName, writeValue);
        editor.commit();
    }

    // 프리퍼런스 읽기
    public String preferenceRead(Context context, String readWhat) {
        SharedPreferences pref = context.getSharedPreferences("com.jihong.footprint", Context.MODE_PRIVATE);
        return pref.getString(readWhat, "");
    }
}