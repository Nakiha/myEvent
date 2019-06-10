package com.nakiha.event.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nakiha.event.Module.EventModule;

import java.util.List;

public final class SP_Utils {



    public static void saveList( List<EventModule> list, Activity from){
        Gson gson = new Gson();
        String listGson = gson.toJson(list);
        SharedPreferences.Editor editor = from.getSharedPreferences(KEYS.GET_LIST_SP_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(KEYS.IO_LIST_SP_KEY, listGson);
        editor.apply();
    }

    public static List<EventModule> getList( Activity from){
        SharedPreferences SP = from.getSharedPreferences(KEYS.GET_LIST_SP_KEY, Context.MODE_PRIVATE);
        String data = SP.getString(KEYS.IO_LIST_SP_KEY, null);
        if (data == null){
            from.finish();
        }
        return new Gson().fromJson(data, new TypeToken<List<EventModule>>(){}.getType());
    }

    public static void saveItem( EventModule item, Activity from){
        Gson gson = new Gson();
        String itemGson = gson.toJson(item);
        SharedPreferences.Editor editor = from.getSharedPreferences(KEYS.GET_ITEM_SP_KEY, Context.MODE_PRIVATE).edit();
        editor.putString(KEYS.IO_ITEM_SP_KEY, itemGson);
        editor.apply();
    }
    public static EventModule getItem( Activity from){
        SharedPreferences SP = from.getSharedPreferences(KEYS.GET_ITEM_SP_KEY, Context.MODE_PRIVATE);
        String data = SP.getString(KEYS.IO_ITEM_SP_KEY, null);
        if (data == null){
            from.finish();
        }
        return new Gson().fromJson(data, new TypeToken<EventModule>(){}.getType());
    }
}
