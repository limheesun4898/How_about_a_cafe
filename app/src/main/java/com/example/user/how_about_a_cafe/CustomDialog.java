package com.example.user.how_about_a_cafe;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CustomDialog {
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private Context context;
    private int psn_cnt = 1;
    private String s_price;
    private int price;
    private int mnu_cnt = 1;

    public CustomDialog(Context context) {
        this.context = context;
    }

    public void callFunction(final String menu, final String cafe, final String category) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.custom_dialog);

        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

        final TextView menu_name = (TextView) dlg.findViewById(R.id.menu_on_click_name);
        final TextView menu_price = (TextView) dlg.findViewById(R.id.menu_on_click_price);
        final Button cancel = (Button) dlg.findViewById(R.id.menu_on_click_cancle);
        final Button person_minus = (Button) dlg.findViewById(R.id.menu_on_click_person_minus);
        final Button person_plus = (Button) dlg.findViewById(R.id.menu_on_click_person_plus);
        final TextView person_cnt = (TextView) dlg.findViewById(R.id.menu_on_click_person_cnt);
        final Button menu_minus = (Button) dlg.findViewById(R.id.menu_on_click_menu_minus);
        final TextView menu_cnt = (TextView) dlg.findViewById(R.id.menu_on_click_menu_cnt);
        final Button menu_plus = (Button) dlg.findViewById(R.id.menu_on_click_menu_plus);

        menu_name.setText(menu);

        firebaseDatabase.child(cafe).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (category.equals("0")) {
                    firebaseDatabase.child(cafe).child("사이드메뉴").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            s_price = dataSnapshot.getValue().toString();
                            menu_price.setText(s_price);
//                            price = Integer.parseInt(s_price);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    firebaseDatabase.child(cafe).child("음료").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            s_price = dataSnapshot.getValue().toString();
                            menu_price.setText(s_price);
//                            price = Integer.parseInt(s_price);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        person_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = Integer.parseInt(menu_price.getText().toString());
                menu_price.setText(String.valueOf(price * psn_cnt));
                person_minus.setEnabled(true);
                psn_cnt -= 1;
//                price = price/psn_cnt;
                person_cnt.setText(String.valueOf(psn_cnt) + " / N");
                if (person_cnt.getText().equals("1 / N"))
                    person_minus.setEnabled(false);


            }
        });
        person_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = Integer.parseInt(menu_price.getText().toString());
                psn_cnt += 1;
//                price = price/psn_cnt;
                person_minus.setEnabled(true);
                menu_price.setText(String.valueOf(price / psn_cnt));
                person_cnt.setText(String.valueOf(psn_cnt) + " / N");
            }
        });
        menu_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = Integer.parseInt(menu_price.getText().toString());
                menu_price.setText(String.valueOf(price / mnu_cnt));
                menu_minus.setEnabled(true);
                mnu_cnt -= 1;
//                price = price * mnu_cnt;
                menu_cnt.setText(String.valueOf(mnu_cnt));
                if (menu_cnt.getText().equals("1"))
                    menu_minus.setEnabled(false);


            }
        });
        menu_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                price = Integer.parseInt(menu_price.getText().toString());
                menu_minus.setEnabled(true);
                mnu_cnt += 1;
                menu_cnt.setText(String.valueOf(mnu_cnt));
                menu_price.setText(String.valueOf(price * mnu_cnt));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.dismiss();
            }
        });
    }
}
