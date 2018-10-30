package com.a1c.team.picaditos_ma;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListAdapter extends BaseAdapter {

    private ArrayList<?> entries;
    private int R_layout_IdView;
    private Context context;

    public ListAdapter(Context context, int R_layout_IdView, ArrayList<?> entries) {
        super();
        this.context = context;
        this.entries = entries;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int pos, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntry (entries.get(pos), view);
        return view;
    }

    @Override
    public int getCount() {
        return entries.size();
    }

    @Override
    public Object getItem(int pos) {
        return entries.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }
    /** Devuelve cada una de las entries con cada una de las vistas a la que debe de ser asociada
     * @param entry La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
     * @param view View particular que contendrá los datos del paquete/handler
     */
    public abstract void onEntry (Object entry, View view);

}