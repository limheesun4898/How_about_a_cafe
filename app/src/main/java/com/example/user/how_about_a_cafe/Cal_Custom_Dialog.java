package com.example.user.how_about_a_cafe;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Cal_Custom_Dialog {
    public static FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    public static DatabaseReference databaseReference = firebaseDatabase.getReference();
    private Context context;
    private ListView listView = null;
    private int total = 0;

    public Cal_Custom_Dialog(Context context) {
        this.context = context;
    }

    public void callFunction() {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.cal_custom_dialog);
        // 커스텀 다이얼로그를 노출한다.
        dlg.show();
        final ArrayList<Cal_List_Item> oData = new ArrayList<>();

        final TextView cal_total = (TextView) dlg.findViewById(R.id.cal_total);
        final ImageView cal_image = (ImageView) dlg.findViewById(R.id.cal_image);

        cal_image.setImageResource(R.drawable.cal_background);

        databaseReference.child("user_menu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Cal_Menu_Item menu_item = dataSnapshot.getValue(Cal_Menu_Item.class);
                if (menu_item == null) {
                    return;
                }
                else {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Cal_List_Item cal_item = new Cal_List_Item();

                        cal_item.iMenu = String.valueOf(snapshot.child("menu_name").getValue());
                        cal_item.iPrice = String.valueOf(snapshot.child("price").getValue());
                        cal_item.iSize = String.valueOf(snapshot.child("size").getValue());

                        total += Integer.parseInt(String.valueOf(snapshot.child("price").getValue()));
                        cal_total.setText(String.valueOf(total) + "원");
                        Log.d("total", String.valueOf(total));
                        if (menu_item.getTemper())
                            cal_item.iTemper = "ICE";
                        else
                            cal_item.iTemper = "HOT";

                        Log.d("menu", String.valueOf(menu_item.getMenu_name()));
                        oData.add(cal_item);

                        listView = (ListView) dlg.findViewById(R.id.cal_listview);
                        Cal_List_Adapter cal_list_adapter = new Cal_List_Adapter(oData);
                        listView.setAdapter(cal_list_adapter);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
