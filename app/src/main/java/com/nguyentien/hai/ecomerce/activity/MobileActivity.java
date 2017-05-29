package com.nguyentien.hai.ecomerce.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nguyentien.hai.ecomerce.R;
import com.nguyentien.hai.ecomerce.adapter.MobileAdapter;
import com.nguyentien.hai.ecomerce.model.Product;
import com.nguyentien.hai.ecomerce.ultil.Server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MobileActivity extends AppCompatActivity {

    Toolbar toolbarMobile;
    ListView lvMobile;
    MobileAdapter mobileAdapter;
    ArrayList<Product> products;
    int idMobile = 0;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        addControl();
        getIdCate();
        addEvent();
    }

    private void addEvent() {
        actionToolbar();
        getData(page);
    }

    private void getData(int Page) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        String url = Server.urlMobile + String.valueOf(Page);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> param = new HashMap<String, String>();
                param.put("minID", String.valueOf(idMobile));
                return super.getParams();
            }
        };
        requestQueue.add(stringRequest);
    }


    private void actionToolbar() {
        setSupportActionBar(toolbarMobile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbarMobile.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void getIdCate() {
        idMobile = getIntent().getIntExtra("cateIdMobile", -1);
        Log.d("Cate mobile value: ", idMobile + "");
    }

    private void addControl() {
        toolbarMobile = (Toolbar) findViewById(R.id.toolbarMobile);
        lvMobile = (ListView) findViewById(R.id.lvMobile);
        products = new ArrayList<>();
        mobileAdapter = new MobileAdapter(getApplicationContext(), products);
        lvMobile.setAdapter(mobileAdapter);
    }
}
