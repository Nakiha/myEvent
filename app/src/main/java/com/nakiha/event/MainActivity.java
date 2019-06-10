package com.nakiha.event;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nakiha.event.Adapter.EventAdapter;
import com.nakiha.event.Module.EventModule;
import com.nakiha.event.UIWidgets.MyRecyclerView;
import com.nakiha.event.Utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.nakiha.event.Utils.KEYS.EVENT_GSON_KEY;

public class MainActivity extends AppCompatActivity {

    /* Test TAG*/
    private static final String TAG = "MainActivity";
    private static final int START_ITEM_PAGE = 0;
    private static final int START_EDIT_ITEM_PAGE = 1;

    private ImageButton openNavigationButton;
    private ImageButton searchButton;
    private FloatingActionButton sacnQRCodeButton;
    private ImageButton addItemButton;
    private EditText addItemEditText;

    private List<EventModule> mEventModuleList;

    private MyRecyclerView eventItemsRecyclerView;
    private EventAdapter mEventAdapter;

    private DrawerLayout mDrawerLayout;
    private NavigationView navView;
    private CircleImageView nav_avatar;

    private static int clikedItemIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR|View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        getWindow().setStatusBarColor(Color.WHITE);

        initUIWidgets();
    }

    private void initUIWidgets(){
        /* 找到实例 */
        addItemButton = findViewById(R.id.add_item_button);
        openNavigationButton = findViewById(R.id.navigation_button);
        sacnQRCodeButton = findViewById(R.id.fab);
        searchButton = findViewById(R.id.bar_search_button);
        addItemEditText = findViewById(R.id.add_item_edit_text);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        navView = findViewById(R.id.nav_view);
        //nav_avatar = findViewById(R.id.nav_avatar);

        //Glide.with(this).load(R.mipmap.test_profile_image).into(nav_avatar);

        /* 测试用lambda */
        View.OnClickListener do_nothing = (v) -> Toast.makeText(this, "do nothing", Toast.LENGTH_SHORT).show();;

        /* 设置各种监听器 */
        sacnQRCodeButton.setOnClickListener(do_nothing);
        searchButton.setOnClickListener(do_nothing);
        addItemButton.setOnClickListener(v -> addItem());
        openNavigationButton.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        addItemEditText.setOnKeyListener((v, keyCode, event) -> {
            InputMethodManager manager;
            manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                //先隐藏键盘
                if (manager.isActive()) {
                    manager.hideSoftInputFromWindow(addItemEditText.getApplicationWindowToken(), 0);
                }
                addItem();
                addItemEditText.clearFocus();
                addItemEditText.setText("");
            }
            return false;
        });


        /* 初始化列表流 */
        initRecyclerList();
    }

    private void addItem(){
        mEventAdapter.insertData(mEventAdapter.getItemCount(), new EventModule(getEditTextString()));
        eventItemsRecyclerView.scrollToPosition(mEventAdapter.getItemCount()-1);}
    private String getEditTextString(){
        try {
            String string = addItemEditText.getText().toString();
            return string.equals("") ? "无标题" : string;
        }finally {
            addItemEditText.setText("");
        }
    }

    private void initRecyclerList(){
        mEventModuleList = new ArrayList<>();
        eventItemsRecyclerView = findViewById(R.id.event_items_recycler_view);
        eventItemsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventItemsRecyclerView.addItemDecoration(new SpaceItemDecoration(24));
        mEventAdapter = new EventAdapter(mEventModuleList, this);

        navView.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()){
                case R.id.nav_delete_all:
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("全部删除");
                    builder.setMessage("确定列表中全部内容吗？");
                    builder.setPositiveButton("确定", (dialog, which) -> {
                        mEventModuleList.clear();
                        mEventAdapter.notifyDataSetChanged();
                        mDrawerLayout.closeDrawers();
                        Toast.makeText(this, "列表已清除", Toast.LENGTH_SHORT).show();
                    });
                    builder.setNegativeButton("取消", (dialog, which) -> {});
                    builder.show();
            }
            return true;
        });
        /* 定义列表中子项的点击事件 */
        mEventAdapter.setOnItemClick(i -> {
            clikedItemIndex = i;
            ItemPageActivity.actionStart(this, getEventBundle(i),START_ITEM_PAGE );
        });
        eventItemsRecyclerView.setAdapter(mEventAdapter);
        eventItemsRecyclerView.setItemViewCacheSize(10);
        eventItemsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                FloatingActionButton fab = findViewById(R.id.fab);
                if (dy>0){ fab.hide(); }
                else { fab.show(); }
            }
        });
        eventItemsRecyclerView.onSizeSmall(()->{
            eventItemsRecyclerView.scrollToPosition(mEventAdapter.getItemCount()-1);
        });
    }

    private Bundle getEventBundle(int i) {
        String eventGson = new Gson().toJson(mEventModuleList.get(i));
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_GSON_KEY,eventGson);
        return bundle;
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "onPause: ");
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == START_ITEM_PAGE && resultCode == RESULT_OK){
            Bundle resultData = data != null ? data.getExtras() : null;
            if (resultData != null) {
                String resultEventGson = resultData.getString(EVENT_GSON_KEY);
                EventModule resultEventModule = new Gson().fromJson(resultEventGson, new TypeToken<EventModule>(){}.getType());
                mEventModuleList.set(clikedItemIndex, resultEventModule);
                mEventAdapter.notifyDataSetChanged();
                Toast.makeText(this,"对 " + (resultEventModule != null ? resultEventModule.getEventTitle() : null) + " 的更改已保存", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == START_ITEM_PAGE && resultCode == RESULT_CANCELED){
            Toast.makeText(this,"对 " + mEventModuleList.get(clikedItemIndex).getEventTitle() + " 的更改未保存", Toast.LENGTH_SHORT).show();
        }
    }

    private long mPressedTime = 0;
    @Override
    public void onBackPressed() {
        //获取第一次按键时间
        long mNowTime = System.currentTimeMillis();
        //比较两次按键时间差
        if((mNowTime - mPressedTime) > 2000){
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            mPressedTime = mNowTime;
        }else{//退出程序
            this.finish();
            System.exit(0);
        }
    }
}
