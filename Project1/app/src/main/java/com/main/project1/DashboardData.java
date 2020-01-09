package com.main.project1;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.List;

public class DashboardData implements Parcelable {

    private List<Item> DashboardItems;

    public DashboardData() {
    }

    public DashboardData(List<Item> items) {
        DashboardItems = items;
    }

    protected DashboardData(Parcel in) {
    }

    public static final Creator<DashboardData> CREATOR = new Creator<DashboardData>() {
        @Override
        public DashboardData createFromParcel(Parcel in) {
            return new DashboardData(in);
        }

        @Override
        public DashboardData[] newArray(int size) {
            return new DashboardData[size];
        }
    };

    public void Add(String text, int imageId) {
        DashboardItems.add(new Item(text, imageId));
    }

    public List<Item> GetItems() {
        return DashboardItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public class Item {
        public String Text;
        public int ImageId;

        public Item(String text, int imageId) {
            Text = text;
            ImageId = imageId;
        }
    }
}
