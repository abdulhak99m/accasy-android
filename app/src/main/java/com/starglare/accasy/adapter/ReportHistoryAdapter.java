package com.starglare.accasy.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.starglare.accasy.R;
import com.starglare.accasy.core.Helper;
import com.starglare.accasy.models.ReportModel;

import java.util.List;

public class ReportHistoryAdapter extends RecyclerView.Adapter<ReportHistoryAdapter.ViewHolder> {

    private final List<ReportModel> mValues;

    public ReportHistoryAdapter(List<ReportModel> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        TextDrawable drawable = Helper.getDrawable(mValues.get(position).getCategory().substring(0,1).toUpperCase());
        holder.category.setImageDrawable(drawable);
        holder.subcategory.setText(mValues.get(position).getSubCategory());
        holder.coordinates.setText(mValues.get(position).getCoordinates());
        holder.comment.setText(mValues.get(position).getComment());
        if(mValues.get(position).getPosted() == 1)
            holder.check.setChecked(true);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView category;
        public final TextView subcategory;
        public final TextView coordinates;
        public final TextView comment;
        public final CheckedTextView check;

        public ReportModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            category = (ImageView) view.findViewById(R.id.category);
            subcategory = (TextView) view.findViewById(R.id.subcategory);
            coordinates = (TextView) view.findViewById(R.id.coordinates);
            comment = (TextView) view.findViewById(R.id.comment);
            check = (CheckedTextView) view.findViewById(R.id.check);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + comment.getText() + "'";
        }
    }
}
