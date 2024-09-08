package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {

    private List<JSONObject> data;

    public ListViewAdapter(List<JSONObject> data) {
        this.data = data;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView id;
        public TextView listId;
        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.id);
            listId = itemView.findViewById(R.id.listId);
            name = itemView.findViewById(R.id.name);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JSONObject obj = data.get(position);
        try {
            holder.id.setText(obj.getString("id"));
            holder.listId.setText(obj.getString("listId"));
            holder.name.setText(obj.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getItemCount() {
        return data.size();
    }
}
