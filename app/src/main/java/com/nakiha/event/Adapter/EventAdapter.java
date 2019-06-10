package com.nakiha.event.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nakiha.event.Module.EventModule;
import com.nakiha.event.R;

import java.util.List;
import java.util.ListIterator;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.ItemHolder> {

/*    public interface OnItemClickListener {
        void onClick(View parent, int position);
    }
    public interface OnItemLongClickListener {
        boolean onLongClick(View parent, int position);
    }*/

    private final static int QR_CODE_SVG_RES = R.drawable.ic_qr_code_icon;
    private final static int LIGHT_EDIT_ITEM_SVG_RES = R.drawable.ic_edit_dark_icon;
    private final static int DARK_EDIT_ITEM_SVG_RES = R.drawable.ic_edit_dark_icon;

    private List<EventModule> mEventModuleList;
    private Activity from;
    /* 构造器 */
    public EventAdapter(List<EventModule> eventModuleList, Activity from) {
        mEventModuleList = eventModuleList;
        this.from = from;
    }
    /* 视图内部持有类 */
    static class ItemHolder extends RecyclerView.ViewHolder {
        TextView monthAndDayText;
        TextView weekdayText;

        TextView title;
        TextView needDoneNumText;
        TextView haveDoneNumText;
        TextView wholeNumText;
        ConstraintLayout clickView;

        ImageButton showQRCodeButton;
        ImageButton editEventButton;


        private ItemHolder(@NonNull View itemView, ViewGroup parentView) {
            super(itemView);
            this.parentView = parentView;
            /* 小标题绑定 */
            monthAndDayText = itemView.findViewById(R.id.month_and_day_text);
            weekdayText = itemView.findViewById(R.id.weekday_text);
            /* 文本绑定 */
            title = itemView.findViewById(R.id.event_item_title);
            needDoneNumText = itemView.findViewById(R.id.matter_need_done_text);
            haveDoneNumText = itemView.findViewById(R.id.matter_have_done_text);
            wholeNumText = itemView.findViewById(R.id.matter_whole_text);
            /* 按钮绑定 */
            showQRCodeButton = itemView.findViewById(R.id.show_qr_code_button);
            editEventButton = itemView.findViewById(R.id.edit_event_button);
            /* 可长按视图绑定 */
            clickView = itemView.findViewById(R.id.event_card_click_view);

            showQRCodeButton.setOnClickListener(mListener);
            editEventButton.setOnClickListener(mListener);
        }
        ViewGroup parentView;

        /* 在这里设置了按钮的监听器 */
        View.OnClickListener mListener = v -> {
            switch (v.getId()){/*
                case R.id.edit_event_button:
                    Toast.makeText(parentView.getContext(), "do nothing", Toast.LENGTH_SHORT).show();
                    break;*/
                case R.id.show_qr_code_button:
                    Toast.makeText(parentView.getContext(), "do nothing", Toast.LENGTH_SHORT).show();
                    break;
            }
        };
    }
    /* 一个接口, 定义了 Item 被点击时会被回调的方法 */
    public interface onItemClick{
        void exec(int i);
    }
    private onItemClick mOnItemClick = null;
    public void setOnItemClick(onItemClick onItemClick) {
        mOnItemClick = onItemClick;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View parentView = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.list_item_event, viewGroup, false);
        ItemHolder viewHolder = new ItemHolder(parentView, viewGroup);
        /* 加载SVG资源 */

        Glide.with(from).load(QR_CODE_SVG_RES).into(viewHolder.showQRCodeButton);
        Glide.with(from).load(DARK_EDIT_ITEM_SVG_RES).into(viewHolder.editEventButton);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder viewHolder, int i) {
        /* 分发 Item 点击事件 */
        if (mOnItemClick!=null)
            viewHolder.clickView.setOnClickListener(v -> {
                mOnItemClick.exec(i);
            });
        viewHolder.clickView.setOnLongClickListener(v ->{
            removeItem(i);
            return true;
        });
        viewHolder.editEventButton.setOnClickListener(v -> {
            editDialog(i);
        });

        /* 文本设置 */
        viewHolder.title.setText(mEventModuleList.get(i).getEventTitle());

        /* 标记上需要角标的item */
        for (EventModule e: mEventModuleList) {
            if (e.isRow){
                e.isPrime = true;
                break;
            }
        }

        /* 显示角标 */
        if (mEventModuleList.get(i).isPrime){
            viewHolder.weekdayText.setVisibility(View.VISIBLE);
            viewHolder.weekdayText.setText("待编辑事件");
        }

    }

    private void editDialog(int i){
        AlertDialog.Builder builder = new AlertDialog.Builder(from);
        final AlertDialog dialog = builder.create();
        View dialogView = View.inflate(from, R.layout.dialog_edit_event, null);
        //设置对话框布局
        dialog.setView(dialogView);
        dialog.show();
        TextView cancelText = dialog.findViewById(R.id.cancel_text);
        cancelText.setOnClickListener(v->dialog.dismiss());
        TextView okText = dialog.findViewById(R.id.ok_text);
        okText.setOnClickListener(v->{
            EditText editText = dialog.findViewById(R.id.title_edit_text);
            mEventModuleList.get(i).setEventTitle(editText.getText().toString());
            notifyItemChanged(i);
            dialog.dismiss();
        });
    }

    /* 方法提供给MainPage来添加Item */
    public void insertData(int position, EventModule data){
        mEventModuleList.add(data);
        //flushPrimeInWholeList();
        notifyItemInserted(position);
        if (position != getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }
    }
    @Override
    public int getItemCount() {
        return mEventModuleList.size();

    }

    private void removeItem(int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(from);
        builder.setTitle("删除");
        String title = mEventModuleList.get(i).getEventTitle();
        builder.setMessage("确定删除 "+ title +" 吗？");
        builder.setPositiveButton("确定", (dialog, which) -> {
            removeData(i);
            Toast.makeText(from, title +"已删除", Toast.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("取消", (dialog, which) -> {});
        builder.show();
    }
    private void removeData(int position){
        mEventModuleList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
        /*if (position != getItemCount()) {
            notifyItemRangeChanged(position, getItemCount());
        }*/
    }

    private void flushPrimeInWholeList() {
        ListIterator<EventModule> iterator = mEventModuleList.listIterator();
        while (iterator.hasNext()){
            iterator.next();
        }

        if (iterator.hasPrevious()){
            EventModule now = iterator.previous();
            EventModule next = null;
            while (iterator.hasPrevious()){
                next = iterator.previous();
                if (!now.getEventDate().equals(next.getEventDate())){
                    now.isRow = true;
                }
                now = next;
            }
            if (next!=null) {next.isRow = true;
            } else {
              now.isRow = true;
            }
        }
    }
}
