package com.nakiha.event.Factory;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nakiha.event.Module.EventModule;
import com.nakiha.event.Utils.KEYS;

import java.util.List;

public class ItemFactory {
    /* 查询数据库, 解析Json, 得到 List<EventModule> modules */
    private static List<EventModule> getItemsFromSP(Context from){
        SharedPreferences sp = from.getSharedPreferences(KEYS.GET_LIST_SP_KEY, Context.MODE_PRIVATE);
        String gsonString = sp.getString(KEYS.IO_LIST_SP_KEY, null);
        if (gsonString == null){
            return null;
        }
        return new Gson().fromJson(gsonString, new TypeToken<List<EventModule>>(){}.getType());
    }
}
