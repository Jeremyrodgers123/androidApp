package com.main.project1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import java.util.List;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.ViewHolder> {

    private OnClickListener mOnClickListener;
    private List<DashboardData.Item> mItems;

    public DashboardAdapter(List<DashboardData.Item> items) {
        mItems = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected View itemLayout;
        protected ImageView itemImageView;
        protected MaterialTextView itemTextView;

        public ViewHolder(View view){
            super(view);
            itemLayout = view;
            itemTextView = (MaterialTextView) view.findViewById(R.id.dashboard_tv_data);
            itemImageView = (ImageView) view.findViewById(R.id.dashboard_iv_data);
        }
    }

    @NonNull
    @Override
    public DashboardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mOnClickListener = (OnClickListener) parent.getContext();

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_dashboard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.OnClick(position);
            }
        });
        DashboardData.Item item = mItems.get(position);
        holder.itemImageView.setImageResource(item.ImageId);
        holder.itemTextView.setText(item.Text);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public interface OnClickListener{
        void OnClick(int position);
    }
}
