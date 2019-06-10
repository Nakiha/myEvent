package com.nakiha.event;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nakiha.event.Adapter.ItemAdapter;
import com.nakiha.event.Module.EventModule;
import com.nakiha.event.UIWidgets.MyRecyclerView;
import com.nakiha.event.Utils.AppBarStateChangeListener;
import com.nakiha.event.Utils.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.nakiha.event.Utils.KEYS.EVENT_GSON_KEY;
import static com.nakiha.event.Utils.KEYS.ITEM_GSON_KEY;

public class ItemPageActivity extends AppCompatActivity {


    public final static int EDIT_ITEM_CODE = 0;

    private ImageView bgImg;
    private TextView title;
    private TextView eventDetail;
    private TextView eventDate;
    private ImageButton backButton;
    private ImageButton changeBgButton;
    private FloatingActionButton addItemButton;
    private ConstraintLayout card;

    private MyRecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;

    private List<EventModule.ItemModule> mItemModuleList;

    EventModule event;
    AppBarLayout appBar;
    Toolbar mToolbar;

    public static void actionStart(Activity from, @Nullable Bundle dataNeedDeliver, int KEY){
        Intent intent = new Intent(from, ItemPageActivity.class);
        if (dataNeedDeliver !=null)
            intent.putExtras(dataNeedDeliver);
        from.startActivityForResult(intent , KEY);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_page);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

        initData();
        initView();
        setView();
        setListener();
    }

    private void initData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null) {
            String data = bundle.getString(EVENT_GSON_KEY);
            event = new Gson().fromJson(data, new TypeToken<EventModule>(){}.getType());
        } else finish();
    }

    private void initView(){
        bgImg = findViewById(R.id.itpg_bg_img);
        appBar = findViewById(R.id.itpg_appbar);
        mToolbar = findViewById(R.id.itpg_toolbar);
        title = findViewById(R.id.itpg_event_title);
        eventDetail = findViewById(R.id.itpg_event_detail);
        eventDate = findViewById(R.id.itpg_event_date);
        backButton = findViewById(R.id.itpg_back_button);
        changeBgButton = findViewById(R.id.itpg_change_bg_button);
        addItemButton = findViewById(R.id.itpg_add_item);
        card = findViewById(R.id.itpg_card_up);
        mRecyclerView = findViewById(R.id.itpg_recyclerView);
    }
    private void setView(){
        /* 加载图片 */
        Glide.with(this).load(event.getImageID()).into(bgImg);
        setSupportActionBar(mToolbar);
        /* 隐藏默认标题 */
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        /* 设置文本 */
        refreshEventCard();
        initRecyclerList();
    }
    private void refreshEventCard(){
        title.setText(event.getEventTitle());
        eventDetail.setText(event.getEventDetail());
        eventDate.setText(event.getEventDate());
    }
    private void initRecyclerList(){
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(24));

        mItemModuleList = new ArrayList<>();
        mItemModuleList.add(event.new ItemModule());
        mItemModuleList.add(event.new ItemModule());
        mItemModuleList.add(event.new ItemModule());
        mItemModuleList.add(event.new ItemModule());
        mItemAdapter = new ItemAdapter(mItemModuleList, this);
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setItemViewCacheSize(10);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy>0){ addItemButton.hide(); }
                else addItemButton.show();
            }
        });
        mRecyclerView.onSizeSmall(()->
                mRecyclerView.scrollToPosition(mItemAdapter.getItemCount()-1));
    }

    private void setListener() {
        /* 监听折叠和展开事件, 更改状态栏字体颜色 */
        appBar.addOnOffsetChangedListener(new AppBarStateChangeListener((appBarLayout, state) -> {
            View decor = this.getWindow().getDecorView();
            if (state == AppBarStateChangeListener.State.EXPANDED) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            } else if (state == AppBarStateChangeListener.State.COLLAPSED) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        }));

        /* 设置编辑添加页面的跳转 */
        addItemButton.setOnClickListener(v ->
                mItemAdapter.insertData(mItemAdapter.getItemCount(), event.new ItemModule())
        );
/*        addItemButton.setOnClickListener(v ->
                EditItemActivity.actionStart(this, null, EDIT_ITEM_CODE));*/

        backButton.setOnClickListener(v -> finish());
        changeBgButton.setOnClickListener(v -> Toast.makeText(this, "fuck", Toast.LENGTH_SHORT).show());

        /* 监听event卡片点击, 为用户提供编辑detail的服务 */
        card.setOnLongClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            final AlertDialog dialog = builder.create();
            View dialogView = View.inflate(this, R.layout.dialog_edit_event, null);
            //设置对话框布局
            dialog.setView(dialogView);
            dialog.show();
            EditText detailEditText = dialog.findViewById(R.id.detail_edit_text);
            EditText titleEditText = dialog.findViewById(R.id.title_edit_text);
            detailEditText.setVisibility(View.VISIBLE);
            detailEditText.setText(event.getEventDetail());
            titleEditText.setText(event.getEventTitle());

            TextView cancelText = dialog.findViewById(R.id.cancel_text);
            cancelText.setOnClickListener(view -> dialog.dismiss());
            TextView okText = dialog.findViewById(R.id.ok_text);
            okText.setOnClickListener(view -> {
                event.setEventTitle(titleEditText.getText().toString());
                event.setEventDetail(detailEditText.getText().toString());
                refreshEventCard();
                dialog.dismiss();
            });
            return true;
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_ITEM_CODE && resultCode == RESULT_OK){
            Bundle bundle = data != null ? data.getExtras() : null;
            if (bundle != null){
                String dataReturn = bundle.getString(ITEM_GSON_KEY);
                EventModule.ItemModule item;
                item = new Gson().fromJson(dataReturn, new TypeToken<EventModule.ItemModule>(){}.getType());
                event.addItem(item);
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("保存更改");
        builder.setMessage("要保存编辑的内容吗？");
        builder.setPositiveButton("保存", (dialog, which) -> {
            setResult(RESULT_OK, new Intent().putExtras(getEventBundle()));
            finish();
        });
        builder.setNegativeButton("不保存", (dialog, which) ->{
            setResult(RESULT_CANCELED, new Intent());
            finish();
        });
        builder.setNeutralButton("取消",(dialog, which) -> {/*do nothing*/} );
        builder.show();
    }

    private Bundle getEventBundle(){
        String eventGson = new Gson().toJson(event);
        Bundle bundle = new Bundle();
        bundle.putString(EVENT_GSON_KEY,eventGson);
        return bundle;
    }
}
