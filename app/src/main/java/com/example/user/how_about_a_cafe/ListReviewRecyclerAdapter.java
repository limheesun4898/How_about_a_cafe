package com.example.user.how_about_a_cafe;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListReviewRecyclerAdapter extends RecyclerView.Adapter<ListReviewRecyclerAdapter.ItemViewHolder> {

    private List<ReviewItem> mItems;
    private Context context;

    public ListReviewRecyclerAdapter(List<ReviewItem> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }


    // 새로운 뷰 홀더 생성
    @Override
    public ListReviewRecyclerAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_list_review_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ItemViewHolder(v);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final ListReviewRecyclerAdapter.ItemViewHolder holder, int position) {
        final ReviewItem item = mItems.get(position);

        holder.review.setText(item.getReview());
        holder.date.setText(item.getFormatDate());
        holder.user_rating.setRating(Float.parseFloat(item.getRating()));
        holder.name.setText(item.getName());
        Glide.with(context).load(item.getProfile_image()).into(holder.profile_image);

        if (item.isIsimage()) {
            holder.image.setVisibility(View.VISIBLE);
            holder.line2.setVisibility(View.VISIBLE);
            Glide.with(context)
                    .load(item.getUrl())
                    .into(holder.image);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Onclick_ImageActivity.class);
                    intent.putExtra("date", item.getUrl());
                    context.startActivity(intent);
                }
            });
        } else {
            holder.line2.setVisibility(View.GONE);
            holder.image.setVisibility(View.GONE);
        }
    }

    // 데이터 셋의 크기를 리턴해줍니다.
    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView review;
        RatingBar user_rating;
        ImageView image;
        TextView date;
        View line1;
        View line2;
        CircleImageView profile_image;
        TextView name;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            review = (TextView) itemView.findViewById(R.id.list_review_review);
            user_rating = (RatingBar) itemView.findViewById(R.id.list_review_user_rating);
            image = (ImageView) itemView.findViewById(R.id.list_review_image);
            date = (TextView) itemView.findViewById(R.id.list_review_date);
            line1 = (View) itemView.findViewById(R.id.list_review_line1);
            line2 = (View) itemView.findViewById(R.id.list_review_line2);
            profile_image = (CircleImageView) itemView.findViewById(R.id.list_review_user_profile);
            name = (TextView) itemView.findViewById(R.id.list_review_user_name);

        }
    }

}
