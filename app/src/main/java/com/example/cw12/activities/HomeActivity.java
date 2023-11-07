package com.example.cw12.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cw12.R;
import com.example.cw12.database.MyDatabaseHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FloatingActionButton add_button;
    MyDatabaseHelper db;
    ArrayList<String> hike_id, hike_name, hike_destination, hike_date, hike_risk, hike_desc;
    HikeAdapter hikeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerViewHikes);
//        add_button = findViewById(R.id.add_button);

//        add_button.setOnClickListener(view -> {
//            Intent intent = new Intent(HomeActivity.this, AddActivity.class);
//            startActivityForResult(intent, 1);
//        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.action_home) {
                    // Trang chủ đã được chọn, không cần thực hiện gì cả
                } else if (id == R.id.action_add_hike) {
                    // Chuyển đến màn hình thêm chuyến đi (AddHikeActivity)
                    Intent addHikeIntent = new Intent(HomeActivity.this, AddActivity.class);
                    startActivity(addHikeIntent);
                } else if (id == R.id.action_search_hike) {
                    // Chuyển đến màn hình tìm kiếm chuyến đi (SearchHikeActivity)
                    Intent searchHikeIntent = new Intent(HomeActivity.this, SearchActivity.class);
                    startActivity(searchHikeIntent);
                }
                return true;
            }
        });






        db = new MyDatabaseHelper(HomeActivity.this);
        hike_id = new ArrayList<>();
        hike_name = new ArrayList<>();
        hike_destination = new ArrayList<>();
        hike_date = new ArrayList<>();
        hike_risk = new ArrayList<>();
        hike_desc = new ArrayList<>();

        storeDataInArrays();

        hikeAdapter = new HikeAdapter(HomeActivity.this,
                this,
                hike_id,
                hike_name,
                hike_destination,
                hike_date,
                hike_risk,
                hike_desc);

        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.delete_all_button){
            deleteAll();
        }
        if(item.getItemId() == R.id.search_button){
            Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1){
            recreate();
        }
    }

    void storeDataInArrays(){
        Cursor cursor = db.readAllData();
        if (cursor.getCount() != 0){
            while(cursor.moveToNext()){
                hike_id.add(cursor.getString(0));
                hike_name.add(cursor.getString(1));
                hike_destination.add(cursor.getString(2));
                hike_date.add(cursor.getString(3));
                hike_risk.add(cursor.getString(5));
                hike_desc.add(cursor.getString(5));
            }
        }
        else {
            Toast.makeText(this, "Không có dữ liệu.", Toast.LENGTH_SHORT).show();
        }
    }

    void deleteAll(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xóa tất cả?");
        builder.setMessage("Bạn có chắc chắn không?");

        builder.setPositiveButton("Có", (dialogInterface, i) -> {
            MyDatabaseHelper db = new MyDatabaseHelper(HomeActivity.this);
            db.deleteAllData();
            Intent intent = new Intent(HomeActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        });

        builder.setNegativeButton("Không", (dialogInterface, i) -> {
        });

        builder.create().show();
    }
    public void updateRecyclerViewData() {
        // Đối với Adapter của bạn, cập nhật danh sách dữ liệu (data) trong Adapter
        // Ví dụ: adapter.updateData(newData); // newData là danh sách mới
        // Sau đó gọi notifyDataSetChanged() để thông báo cập nhật giao diện
        hikeAdapter.notifyDataSetChanged();
    }
}
