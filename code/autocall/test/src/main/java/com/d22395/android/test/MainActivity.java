package com.d22395.android.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.call_recycler_view);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        recyclerView.setLayoutManager(layoutManager);
//        FruitAdapter adapter = new FruitAdapter();
//        recyclerView.setAdapter(adapter);

    }

    class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {

            ViewHolder(View view){
                super(view);
            }
        }

        FruitAdapter() {
        }

        @Override
        public FruitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_main, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(FruitAdapter.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

}
