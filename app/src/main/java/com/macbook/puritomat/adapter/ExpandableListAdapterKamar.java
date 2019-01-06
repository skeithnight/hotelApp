package com.macbook.puritomat.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.macbook.puritomat.R;
import com.macbook.puritomat.model.Kamar;
import com.macbook.puritomat.model.TipeKamar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapterKamar extends BaseExpandableListAdapter {
    private static String TAG = "Testing";

    private Context _context;
    private ArrayList<TipeKamar> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, ArrayList<Kamar>> _listDataChild;
    private String level;
    private SparseBooleanArray mSelectedItemsIds;

    public ExpandableListAdapterKamar(Context context, ArrayList<TipeKamar> listDataHeader,
                                 HashMap<String, ArrayList<Kamar>> listChildData, String level) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
        this.level = level;
        this.mSelectedItemsIds = new SparseBooleanArray();
    }

    @Override
    public Kamar getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getId())
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String nomorKamar = (String) String.valueOf(getChild(groupPosition, childPosition).getNomor());
        final String statusKamar;
        if (getChild(groupPosition, childPosition).getStatus()) {
            statusKamar = "Kosong";
        }else {
            statusKamar = "Terisi";
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (level == "regis") {
                convertView = infalInflater.inflate(R.layout.list_kamar_registrasi, null);
                CheckBox checkBox = (CheckBox) convertView.findViewById(R.id.cb_pilih_kamar);
                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = Integer.parseInt(String.format("3%d%d",groupPosition,childPosition));
                        checkCheckBox(position, !mSelectedItemsIds.get(position));
                    }
                });
            }else {
                convertView = infalInflater.inflate(R.layout.list_item_kamar, null);
                TextView txtStatusKamar = (TextView) convertView
                        .findViewById(R.id.tx_status_kamar);
                txtStatusKamar.setText(statusKamar);
            }
        }

        TextView txtNomorKamar = (TextView) convertView
                .findViewById(R.id.tx_nomor_kamar);
        txtNomorKamar.setText("Kamar "+nomorKamar);
        return convertView;
    }

    /**
     * Check the Checkbox if not checked
     **/
    public void checkCheckBox(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, true);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    /**
     * Return the selected Checkbox IDs
     **/
    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition).getId())
                .size();
    }

    @Override
    public TipeKamar getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerNamaTipeKamar = (String) getGroup(groupPosition).getNama();
        String headerHargaTipeKamar = (String) String.valueOf(getGroup(groupPosition).getHarga());
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group_kamar, null);
        }

        TextView lbNamaTipeKamar = (TextView) convertView
                .findViewById(R.id.tx_nama_tipe_kamar);
        lbNamaTipeKamar.setTypeface(null, Typeface.BOLD);
        lbNamaTipeKamar.setText(headerNamaTipeKamar);

        TextView lbHargaTipeKamar = (TextView) convertView
                .findViewById(R.id.tx_harga_tipe_kamar);
        lbHargaTipeKamar.setText("Rp. "+headerHargaTipeKamar);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
