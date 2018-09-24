package com.example.user.how_about_a_cafe;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class CustomDialog {
    public static DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    private Context context;
    private int price;
    private int total;
    private int total_2;
    private int ice = 0;
    private boolean ice_cnt = false;
    private int size = 0;
    private int size_cnt = 1;
    private int iMenu_cnt = 1;
    private int iPerson_cnt = 1;
    private int total_3;
    private int SIZECNT = 0;


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
        final Button hot_btn = (Button) dlg.findViewById(R.id.menu_on_click_hot);
        final Button ice_btn = (Button) dlg.findViewById(R.id.menu_on_click_ice);
        final Button size1 = (Button) dlg.findViewById(R.id.menu_on_click_size1);
        final Button size2 = (Button) dlg.findViewById(R.id.menu_on_click_size2);
        final Button size3 = (Button) dlg.findViewById(R.id.menu_on_click_size3);

        menu_name.setText(menu);
        hot_btn.setEnabled(false);
        size1.setEnabled(false);
        if (iMenu_cnt == 1)
            menu_minus.setEnabled(false);
        else
            menu_minus.setEnabled(true);
        if (iPerson_cnt == 1)
            person_minus.setEnabled(false);
        else
            person_minus.setEnabled(true);

        firebaseDatabase.child(cafe).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (category.equals("0")) {
                    firebaseDatabase.child(cafe).child("사이드메뉴").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String s_price = dataSnapshot.getValue().toString();
                            menu_price.setText(s_price);
                            price = Integer.parseInt(s_price);
                            total = price;
                            total_2 = price;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else {
                    firebaseDatabase.child(cafe).child("음료").child(menu).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String s_price = dataSnapshot.getValue().toString();
                            menu_price.setText(s_price);
                            price = Integer.parseInt(s_price);
                            total = price;
                            total_2 = price;
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                firebaseDatabase.child(cafe).child("음료").child("ICE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null) {
                            ice = 0;
                        } else
                            ice = Integer.parseInt(dataSnapshot.getValue().toString());

                        if (ice == 0) {
                            hot_btn.setBackgroundColor(Color.GRAY);
                            ice_btn.setBackgroundColor(Color.GRAY);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.child(cafe).child("음료").child("SIZE").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null)
                            size = 0;
                        else
                            size = Integer.parseInt(dataSnapshot.getValue().toString());
                        if (size == 0) {
                            size1.setEnabled(false);
                            size2.setEnabled(false);
                            size3.setEnabled(false);
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                firebaseDatabase.child(cafe).child("음료").child("SIZE_CNT").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() == null)
                            SIZECNT = 0;
                        else
                            SIZECNT = Integer.parseInt(dataSnapshot.getValue().toString());

                        if (SIZECNT == 2) {
                            size3.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        hot_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hot_btn.setEnabled(false);
                ice_btn.setEnabled(true);
                ice_cnt = false;
                menu_price.setText(String.valueOf(price));
                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        ice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hot_btn.setEnabled(true);
                ice_btn.setEnabled(false);
                ice_cnt = true;
                menu_price.setText(String.valueOf(price + ice));
                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        size1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(false);
                size2.setEnabled(true);
                size3.setEnabled(true);
                size_cnt = 1;

                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice));
                else
                    menu_price.setText(String.valueOf(price));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());

            }
        });

        size2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(true);
                size2.setEnabled(false);
                size3.setEnabled(true);
                size_cnt = 2;
                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice + size));
                else
                    menu_price.setText(String.valueOf(price + size));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        size3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size1.setEnabled(true);
                size2.setEnabled(true);
                size3.setEnabled(false);
                size_cnt = 3;
                if (iMenu_cnt > 1) {
                    iMenu_cnt = 1;
                    menu_cnt.setText(String.valueOf(iMenu_cnt));
                    menu_minus.setEnabled(false);
                }

                if (iPerson_cnt > 1) {
                    iPerson_cnt = 1;
                    person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                    person_minus.setEnabled(false);
                }

                if (ice_cnt)
                    menu_price.setText(String.valueOf(price + ice + size * 2));
                else
                    menu_price.setText(String.valueOf(price + size * 2));

                total = Integer.parseInt(menu_price.getText().toString());
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        person_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iPerson_cnt -= 1;
                if (iPerson_cnt == 1)
                    person_minus.setEnabled(false);
                person_plus.setEnabled(true);
                person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                menu_price.setText(String.valueOf(Math.round(total_2 / iPerson_cnt)));
                total = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        person_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iPerson_cnt > 10)
                    person_plus.setEnabled(false);
                person_minus.setEnabled(true);
                iPerson_cnt += 1;
                person_cnt.setText(String.valueOf(iPerson_cnt) + " / N");
                menu_price.setText(String.valueOf(Math.round(total_2 / iPerson_cnt)));
                total = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
            }
        });

        menu_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMenu_cnt -= 1;
                if (iMenu_cnt == 1)
                    menu_minus.setEnabled(false);
                menu_plus.setEnabled(true);
                menu_cnt.setText(String.valueOf(iMenu_cnt));
                menu_price.setText(String.valueOf(total * iMenu_cnt));
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());

            }
        });

        menu_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                iMenu_cnt += 1;
                if (iMenu_cnt > 10)
                    menu_plus.setEnabled(false);
                menu_minus.setEnabled(true);
                menu_cnt.setText(String.valueOf(iMenu_cnt));
                menu_price.setText(String.valueOf(total * iMenu_cnt));
                total_2 = Integer.parseInt(menu_price.getText().toString());
                total_3 = Integer.parseInt(menu_price.getText().toString());
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
