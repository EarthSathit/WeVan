package th.ac.kru.wevan;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private final int id = 10;
    private NotificationManager mNotify;

    private TextView signUp;
    private ImageView next;
    private EditText edtPhone;

    private String phone;

    private String shID, shName, shType, shEmail, shPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_main);

        checkConnection();
        initialValue();
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
    public void checkConnection(){
        if(isOnline()){
            // Toast.makeText(LoginActivity.this, "You are connected to Internet", Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
            builder.setIcon(R.drawable.ic_info);
            builder.setTitle("แจ้งเตือน");
            builder.setMessage("กรุณาเชื่อมต่ออินเทอร์เน็ต");
            builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent openApp = new Intent(MainActivity3.this, MainActivity.class);
                        startActivity(openApp);*/
                    startActivity(new Intent(
                            Settings.ACTION_SETTINGS));
                    dialogInterface.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void initialValue(){
        signUp =(TextView)findViewById(R.id.signUp);
        next = (ImageView)findViewById(R.id.next);
        edtPhone = (EditText)findViewById(R.id.edtPhone);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkConnection();
                phone = edtPhone.getText().toString();
                if (TextUtils.isEmpty(phone)){
                    edtPhone.setError("กรุณากรอกเบอร์โทรศัพท์");
                } else if (phone.length() != 10){
                    edtPhone.setError("กรุณากรอกเบอร์โทรศัพท์ให้ครบ");
                } else {
                    login(phone);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this,MainActivity3.class);
                startActivity(it);
            }
        });
    }

    public void login(final String phone){
        //wssathit.codehansa.com //call file web service
        //http://www.tonrukfarm.com/phpmyadmin/ //database
        final SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final String url = "http://wssathit.codehansa.com/login.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.d("onResponse", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("user");
                    if(jsonArray != null){
                        for(int i=0; i < jsonArray.length(); i++){
                            JSONObject json_data = jsonArray.getJSONObject(i);
                            editor.putString("SESSION_ID", json_data.getString("id_card"));
                            editor.putString("SESSION_NAME", json_data.getString("name"));
                            editor.putString("SESSION_USER_TYPE", json_data.getString("user_type_id"));
                            editor.putString("SESSION_EMAIL", json_data.getString("email"));
                            editor.putString("SESSION_PHONE", json_data.getString("phone"));
                            editor.commit();
                        }

                         shID = sharedPreferences.getString("SESSION_ID","");
                         shName = sharedPreferences.getString("SESSION_NAME", "");
                         shType = sharedPreferences.getString("SESSION_USER_TYPE", "");
                         shEmail = sharedPreferences.getString("SESSION_EMAIL", "");
                         shPhone = sharedPreferences.getString("SESSION_PHONE", "");
                         showToast("กำลังเข้าสู่ระบบ");
                        //showDialogSuccess("Information", "เข้าสู่ระบบสำเร็จ");
                    }
                } catch (JSONException e) {
                    showDialogFailed("แจ้งเตือน", "เข้าสู่ระบบไม่สำเร็จ กรุณาเข้าสู่ระบบอีกครั้ง");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String message = null;
                if (error instanceof NetworkError) {
                    message = "NetworkError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "AuthFailureError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof NoConnectionError) {
                    message = "NoConnectionError! Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Log.d("onError", message);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cmd", "select");
                params.put("phone", phone);

                return params;
            }
        };
        requestQueue.add(request);
    }

    public void showToast(String msg){
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        LinearLayout toastContentView = (LinearLayout) toast.getView();
        ImageView imageView = new ImageView(getApplicationContext());
        imageView.setImageResource(R.drawable.loader3);
        imageView.setMaxWidth(3);
        imageView.setMaxHeight(3);
        toastContentView.addView(imageView, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.show();
        Intent openApp = new Intent(LoginActivity.this, MainActivity.class);
        openApp.putExtra("id_card", shID);
        openApp.putExtra("name", shName);
        openApp.putExtra("user_type", shType);
        openApp.putExtra("email", shEmail);
        openApp.putExtra("phone", shPhone);
        startActivity(openApp);
    }

    public void showNotification(String title, String text){
        Uri cus_uri = Uri.parse("android.resource://" + this.getPackageName() + "/" + R.raw.open_notify);
        long[] vibrate = {0, 1000, 500, 1000, 500, 1000};

        Notification.Builder builder = new Notification.Builder(LoginActivity.this);
        builder
                .setSmallIcon(R.drawable.van1)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(false)
                .setVibrate(vibrate)
                .setSound(cus_uri);
        //Notification notification = builder.build();

        mNotify = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        //mNotify.notify(id, notification);
    }

    public void showDialogSuccess(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder
                .setIcon(R.drawable.van1)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent openApp = new Intent(LoginActivity.this, MainActivity.class);
                        openApp.putExtra("id_card", shID);
                        openApp.putExtra("name", shName);
                        openApp.putExtra("user_type", shType);
                        openApp.putExtra("email", shEmail);
                        openApp.putExtra("phone", shPhone);
                        startActivity(openApp);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void showDialogFailed(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder
                .setIcon(R.drawable.ic_info)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
