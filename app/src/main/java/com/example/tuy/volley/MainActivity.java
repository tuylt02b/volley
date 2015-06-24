package com.example.tuy.volley;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.example.tuy.volley.apis.Api;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.tuy.volley.apis.Api;
import com.example.tuy.volley.apis.ApiError;
import com.example.tuy.volley.apis.ApiErrorListener;
import com.google.gson.JsonObject;




public class MainActivity extends ActionBarActivity {
    public static final String TAG = "MainActivity";

ArrayList<Contacs> mDatas = new ArrayList<>();
    ContacsAdapter contacsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Api.init(getApplicationContext());
        setContentView(R.layout.activity_main);
        ListView listView = (ListView)findViewById(R.id.listview);
        Log.d(TAG, "before - Data ArrayList: " + mDatas.size());
        getContact();
        Log.d(TAG, "after - Data ArrayList: " + mDatas.size());
        contacsAdapter = new ContacsAdapter(this,mDatas);
        listView.setAdapter(contacsAdapter);
    }

    private void getContact() {
        HashMap<String, String> param = new HashMap<>();

        Api.getInstance().getUserInfo(param, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                final Gson gson = new Gson();
                try {
                    String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
                    JsonElement je = gson.fromJson(json, JsonElement.class);
                    JsonArray jsonArray =je.getAsJsonObject().getAsJsonArray("contacts");
                    for(int i=0;i<jsonArray.size();i++){
                        JsonObject jsonObjectId = jsonArray.get(i).getAsJsonObject();
                        String id = jsonObjectId.get("id").getAsString();
                        String name = jsonObjectId.get("name").getAsString();
                        String email = jsonObjectId.get("email").getAsString();
                        String address = jsonObjectId.get("address").getAsString();
                        String gender =jsonObjectId.get("gender").getAsString();
                        Log.d("-------", id);
                        Contacs contacs = new Contacs();
                        contacs.setId(id);
                        contacs.setName(name);
                        contacs.setEmail(email);
                        contacs.setAddress(address);
                        contacs.setGender(gender);
                        mDatas.add(contacs);
                        contacsAdapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "Response: " + je.toString());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new ApiErrorListener() {
            @Override
            public void onErrorResponse(ApiError error) {
                Log.d(TAG, "Error: "+error.getMessage());
            }
        });

    }

}
