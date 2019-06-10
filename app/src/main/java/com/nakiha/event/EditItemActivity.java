package com.nakiha.event;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nakiha.event.Module.EventModule;

import static com.nakiha.event.Utils.KEYS.EVENT_GSON_KEY;

public class EditItemActivity extends AppCompatActivity {

    public static void actionStart(Activity from, @Nullable Bundle dataNeedDeliver, int KEY){
        Intent intent = new Intent(from, EditItemActivity.class);
        if (dataNeedDeliver !=null)
            intent.putExtras(dataNeedDeliver);
        from.startActivityForResult(intent , KEY);
    }

    EventModule.ItemModule item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        initItem();

    }

    private void initItem(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null) {
            String data = bundle.getString(EVENT_GSON_KEY);
            item = new Gson().fromJson(data, new TypeToken<EventModule.ItemModule>(){}.getType());
        }
    }

}
