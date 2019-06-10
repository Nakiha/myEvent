package com.nakiha.event.Module;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.nakiha.event.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventModule implements Serializable {
    private static final long serialVersionUID = 1L;
    /*  */

    /* 用于记录这个事项的开始时间 */
    private int YEAR;
    private int DAY_OF_YEAR;
    private int MINS_OF_DAY;

    /* 两个计数量 */
    // private int itemWholeNum;
    // private int itemNeedDoneNum;

    /* 一些标记量 */
    public boolean isRow = true;
    public boolean isPrime = false;

    /* 一些会用到的字符串, 例如标题, 日期, 详细内容 */
    private String eventTitle;
    private String eventDate;
    private String eventDetail;
    /* 保存的图片 */
    private int ImageID = R.mipmap.bg_test_1;

    /* Getter & Setter */
    public void setEventTitle(String eventTitle) { this.eventTitle = eventTitle; }
    public String getEventTitle() {
        return eventTitle;
    }
    public String getEventDate() {
        return eventDate;
    }
    public int getImageID() {
        return ImageID;
    }
    public void setImageID(int imageID) {
        ImageID = imageID;
    }
    public String getEventDetail() {
        return eventDetail;
    }
    public void setEventDetail(String eventDetail) {
        this.eventDetail = eventDetail;
    }
    public int getItemWholeNum() {
        return eventItems.size();
    }
    public int getItemNeedDoneNum() {
        int needDonNum = 0;
        for (ItemModule item: eventItems) {
            if (!item.isDone) needDonNum++;
        }
        return needDonNum;
    }

    /* 子项List */
    private List<ItemModule> eventItems = new ArrayList<>();
    /* List中的item */
    public class ItemModule {
        /* 一天24小时, 对应1440分钟, 每个日程只需要精确到分钟 */
        /* short 2个字节 最大值为32767 绝对够用 */
        /* 不想塞一堆getter和setter了直接给public */
        public short startYear;
        public short startMonth;
        public short startMonthDay;
        public short startHour;
        public short startMinute;

        public boolean isDone = false;

        public short durationMinute;

        public String itemTitle;
        @Nullable
        public String itemDetail;

        public ItemModule(Calendar startTime, short durationMinute, String itemTitle, @Nullable String itemDetail) {
            startYear = (short)startTime.get(Calendar.YEAR);
            startMonth = (short)(startTime.get(Calendar.MONTH)+1);
            startMonthDay = (short)startTime.get(Calendar.DAY_OF_MONTH);
            startHour = (short)startTime.get(Calendar.HOUR_OF_DAY);
            startMinute = (short)startTime.get(Calendar.MINUTE);
            this.durationMinute = durationMinute;
            this.itemTitle = itemTitle;
            this.itemDetail = itemDetail;
        }
        /* test */
        public ItemModule(){};
    }

    /* 测试用构造器 */
    public EventModule(String title ) {
        this.eventTitle = title;
        Calendar createTime = Calendar.getInstance();
        eventDate = (createTime.get(Calendar.MONTH)+1) + "/" + createTime.get(Calendar.DAY_OF_MONTH);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    public boolean addItem(ItemModule itemModule) {
        return eventItems.add(itemModule);
    }


}
