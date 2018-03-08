package com.theatre.soundless.soundless_theatre;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.view.View.*;


public class SoundlessTheatre extends AppCompatActivity implements OnClickListener{

    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;
    private Button btn5;
    private Button btn6;
    private Button btn7;
    private Button btn8;

    private ImageView w1;
    private ImageView w2;
    private ImageView w3;
    private ImageView w4;
    private ImageView w5;
    private ImageView w6;
    private ImageView w7;
    private ImageView w8;

    private TextView info;

    //http://192.168.から/~までの間を記入
    private String url ="1.78:5000";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wifi_setting);

        info = findViewById(R.id.info);

        //get
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String result = null;
                // リクエストオブジェクトを作って
                Request request = new Request.Builder()
                        .url("http://192.168."+url+"/list")
                        .get()
                        .build();
                // クライアントオブジェクトを作って
                OkHttpClient client = new OkHttpClient();
                // リクエストして結果を受け取って
                try {
                    Response response = client.newCall(request).execute();
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // 返す
                return result;
            }
            @Override
            protected void onPostExecute(String result) {
                if (result == null) {
                    //警告文
                    new AlertDialog.Builder(SoundlessTheatre.this)
                            .setTitle("警告")
                            .setMessage("接続できませんでした。\n[SoundlessTheatreSetting]に\n接続されているか確認してください。")
                            .setPositiveButton("Help", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(getApplication(),help_activity.class);
                                    startActivity(intent);


                                }
                            })
                            .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            })
                            .show();
                }else {
                    List<TextView> list = new ArrayList<>();
                    try {
                        JSONObject datas = new JSONObject(result);
                        LinearLayout layout = (LinearLayout) findViewById(R.id.wifilayout);
                         for (int count = 0; count < datas.length(); count++) {
                             String power = datas.getJSONObject(String.valueOf(count)).getString("POWER");
                             String ssid = datas.getJSONObject(String.valueOf(count)).getString("SSID");
                             View view = getLayoutInflater().inflate(R.layout.wifi,null);
                             layout.addView(view);
                             Button but = (Button) view.findViewById(R.id.buttoncon);
                             view.findViewById(R.id.buttoncon).setOnClickListener(SoundlessTheatre.this);
                             but.setText(ssid);
                             switch (power) {
                                 case "1":
                                     ((ImageView) view.findViewById(R.id.imagewifi)).setImageResource(R.drawable.wifi1);
                                     break;
                                 case "2":
                                     ((ImageView) view.findViewById(R.id.imagewifi)).setImageResource(R.drawable.wifi2);
                                     break;
                                 case "3":
                                     ((ImageView) view.findViewById(R.id.imagewifi)).setImageResource(R.drawable.wifi3);
                                     break;
                                 case "4":
                                     ((ImageView) view.findViewById(R.id.imagewifi)).setImageResource(R.drawable.wifi4);
                             }
                             // list.get(count).setText(ssid);
                          }
                    } catch (JSONException e) {
                            e.printStackTrace();
                    }
                        //message.setText("Result: " + result);
                }
            }
        }.execute();
    }

    @Override
    public void onClick(View view) {

        String id=((Button)view.findViewById(R.id.buttoncon)).getText().toString();

        LayoutInflater inflater = LayoutInflater.from(SoundlessTheatre.this);
        View view1 = inflater.inflate(R.layout.dialog_sub,null);
        final EditText etsb1 = (EditText)view1.findViewById(R.id.etsb1);

        final String finalId = id;
        new AlertDialog.Builder(SoundlessTheatre.this)
                .setTitle("パスワードを入力してください")
                .setView(view1)
                .setPositiveButton("connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        final String gettext = etsb1.getText().toString();

                        //post

                        new AsyncTask<Void, Void, String>() {
                            @Override
                            protected String doInBackground(Void... params) {
                                String result = null;

                                MediaType JSON = MediaType.parse("text/json;charset=utf-8");

                                String message = finalId + " " + gettext;


                                RequestBody requestBody = RequestBody.create(JSON,message);

                                // リクエストオブジェクトを作って
                                Request request = new Request.Builder()
                                        .url("http://192.168."+url+"/connect_app")
                                        .post(requestBody)
                                        .build();

                                // クライアントオブジェクトを作って
                                OkHttpClient client = new OkHttpClient();

                                // リクエストして結果を受け取って
                                try {
                                    Response response = client.newCall(request).execute();
                                    result = response.body().string();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                // 返す
                                return result;
                            }
                        }.execute();
                    }
                })
                .setNegativeButton("back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .show();

    }

}