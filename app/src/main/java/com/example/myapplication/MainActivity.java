package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetData.GetDataListener {
    private List<JSONObject> list;
    private ListViewAdapter adapter;
    private RecyclerView recyclerView;
    private int position = 0;
    private List<Integer> list_ids = new ArrayList<Integer>();
    private Button prev;
    private Button reset;
    private Button next;
    private TextView displayText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prev = findViewById(R.id.button5);
        reset = findViewById(R.id.button6);
        next = findViewById(R.id.button7);
        displayText = findViewById(R.id.currentPos);
        prev.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    position -= 1;
                    updateList();
                }
            }
        });
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                position = 0;
                updateList();
            }
        });
        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (position < list_ids.size()) {
                    position += 1;
                    updateList();
                }
            }
        });
        prev.setVisibility(View.INVISIBLE);
        displayText.setText("Display all Items");
        new GetData(this).execute("https://fetch-hiring.s3.amazonaws.com/hiring.json");
    }

    @Override
    public void onTaskCompleted(List<JSONObject> result) {
        try {
            list = result;
            for(int i = 0; i < result.size(); i++){
                if (list_ids.isEmpty()|| list_ids.get(list_ids.size() - 1) < Integer.parseInt(result.get(i).getString("listId"))) {
                    list_ids.add(Integer.parseInt(result.get(i).getString("listId")));
                }
            }
            updateList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateList() {
        try {
            if (position == 0) {
                prev.setVisibility(View.INVISIBLE);
                displayText.setText("Display all Items");
            } else {
                prev.setVisibility(View.VISIBLE);
                displayText.setText(String.format("Display ListID: %d items", position));
            }
            if (position == list_ids.size()) {
                next.setVisibility(View.INVISIBLE);
            } else {
                next.setVisibility(View.VISIBLE);
            }
            List<JSONObject> updateList = new ArrayList<JSONObject>();
            for (int i = 0; i < list.size(); i++) {
                int list_id = Integer.parseInt(list.get(i).getString("listId"));
                if (position == 0 || list_id == position) {
                    updateList.add(list.get(i));
                }
            }

            recyclerView = findViewById(R.id.list);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ListViewAdapter(updateList);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}