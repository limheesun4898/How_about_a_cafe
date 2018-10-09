package com.example.user.how_about_a_cafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.user.how_about_a_cafe.MyReview.databaseReference;

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ItemViewHolder> {
    private List<MyReviewItem> mItems;
    private Context context;
    public MyReviewAdapter(List<MyReviewItem> mItems, Context context) {
        this.mItems = mItems;
        this.context = context;
    }


    // 새로운 뷰 홀더 생성
    @Override
    public MyReviewAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_review_item, null);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        v.setLayoutParams(lp);
        return new ItemViewHolder(v);
    }


    // View 의 내용을 해당 포지션의 데이터로 바꿉니다.
    @Override
    public void onBindViewHolder(final MyReviewAdapter.ItemViewHolder holder, int position) {
        final MyReviewItem item = mItems.get(position);

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
            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.menu);
                    popupMenu.inflate(R.menu.menu_del_edit);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_del:
                                    deleteAlert(holder, item, true);
                                    break;
                                case R.id.menu_edit:
                                    Intent intent = new Intent(context, EditActivity.class);
                                    intent.putExtra("date", item.getFormatDate());
                                    context.startActivity(intent);
                                    Toast.makeText(context, "수저엉", Toast.LENGTH_SHORT).show();
                                    break;

                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
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

            holder.menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popupMenu = new PopupMenu(context, holder.menu);
                    popupMenu.inflate(R.menu.menu_del_edit);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_del:
                                    deleteAlert(holder, item, false);
                                    break;
                                case R.id.menu_edit:
                                    Intent intent = new Intent(context, EditActivity.class);
                                    intent.putExtra("date", item.getFormatDate());
                                    context.startActivity(intent);
                                    Toast.makeText(context, "수저엉", Toast.LENGTH_SHORT).show();
                                    ((Activity)context).finish();
                                    break;
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
        }
    }

    public void deleteAlert(final RecyclerView.ViewHolder holder, final MyReviewItem item, final boolean hasImg) {

        AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
        alert_confirm.setMessage("삭제하시겠습니까?").setCancelable(false).setPositiveButton("삭제",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (hasImg) {
                            MyReview.mStorage.child("images/").child(item.getFormatDate()).delete();
                        }
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        String uid = user.getUid();
                        databaseReference.child("UserReview").child(uid).child(item.getFormatDate()).removeValue();
                        databaseReference.child("Review").child(item.getCafe_name()).child(item.getMenu()).removeValue();
                        mItems.remove(holder.getAdapterPosition());
                        notifyDataSetChanged();
                    }
                }).setNegativeButton("취소",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alert_confirm.create();
        alert.show();
    }

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
        ImageView menu;

        public ItemViewHolder(final View itemView) {
            super(itemView);
            review = (TextView) itemView.findViewById(R.id.my_review_review);
            user_rating = (RatingBar) itemView.findViewById(R.id.my_review_user_rating);
            image = (ImageView) itemView.findViewById(R.id.my_review_image);
            date = (TextView) itemView.findViewById(R.id.my_review_date);
            line1 = (View) itemView.findViewById(R.id.my_review_line1);
            line2 = (View) itemView.findViewById(R.id.my_review_line2);
            profile_image = (CircleImageView) itemView.findViewById(R.id.my_review_user_profile);
            name = (TextView) itemView.findViewById(R.id.my_review_user_name);
            menu = (ImageView) itemView.findViewById(R.id.my_review_menu_btn);

        }
    }
}
