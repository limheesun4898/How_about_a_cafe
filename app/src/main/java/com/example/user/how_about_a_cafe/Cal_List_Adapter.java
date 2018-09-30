package com.example.user.how_about_a_cafe;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Cal_List_Adapter extends BaseAdapter{

    LayoutInflater inflater = null;
    private ArrayList<Cal_List_Item> m_oData = null;
    private int nListCnt = 0;

    public Cal_List_Adapter(ArrayList<Cal_List_Item> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.cal_custom_listview, parent, false);
        }

        TextView cal_menu = (TextView) convertView.findViewById(R.id.cal_menu);
        TextView cal_temper = (TextView) convertView.findViewById(R.id.cal_temper);
        TextView cal_size = (TextView) convertView.findViewById(R.id.cal_size);
        TextView cal_price = (TextView) convertView.findViewById(R.id.cal_price);
        TextView cal_cnt = (TextView) convertView.findViewById(R.id.cal_cnt);

        cal_menu.setText(m_oData.get(position).iMenu);
        cal_temper.setText(m_oData.get(position).iTemper);
        cal_size.setText(m_oData.get(position).iSize);
        cal_price.setText(m_oData.get(position).iPrice);
        cal_cnt.setText(m_oData.get(position).iCnt);
        return convertView;
    }
}
