package com.example.screen_dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText phone_no;
    private Button call_btn;
    public static String customerNumber = "customer no";
    public static String accountSid = "meetuniversitycom1";
    public static String flow_id = "your-flow-id";
    public static String authId = "API-KEY";
    public static String authToken = "API-TOKEN";
    private String phone_no_string;
    public static String url = "https://api.exotel.com/v1/Accounts/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        phone_no = findViewById(R.id.editText2);
        phone_no_string = phone_no.getText().toString();


        call_btn = findViewById(R.id.button);
        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                connectCustomerToFlow();
            }
        });


    }

    public ExotelResponse connectCustomerToFlow() {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("From", customerNumber)
                .addFormDataPart("To",phone_no_string)
                .addFormDataPart("Url", url + accountSid + "/exoml/start_voice/" + flow_id).build();

        String credentials = Credentials.basic(authId, authToken);

        Request request = new Request.Builder()
                .url(String.format(ExotelStrings.CONNECT_CUSTOMER_TO_FLOW_URL, accountSid)).method("POST", body)
                .addHeader("Authorization", credentials).addHeader("Content-Type", "application/json").build();

        try {
            Response response = client.newCall(request).execute();
            Gson connect = new Gson();
            String res = null;
            try {
                res = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            ExotelResponse SuccessResponse = connect.fromJson(res, ExotelResponse.class);

            int status = response.code();

            if (status == 200) {
                ExotelSuccessResponse cust = new ExotelSuccessResponse(0);
                Toast.makeText(MainActivity.this,"yeah you call done",Toast.LENGTH_SHORT).show();
                return cust;
            } else {
                ExotelFailureResponse cust = new ExotelFailureResponse(0);
                return cust;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExotelFailureResponse cust = new ExotelFailureResponse(0);
        return cust;
    }
}
