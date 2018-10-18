package th.ac.kru.wevan;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

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

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity3 extends AppCompatActivity {

    private ImageView back, signIn;
    private EditText edtPhone, edtFirstName, edtLastName, edtEmail;

    private String phone, first_name, last_name, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        initialValue();
    }

    public void initialValue(){
        back = (ImageView)findViewById(R.id.back);
        signIn = (ImageView)findViewById(R.id.img_sign_in);
        edtPhone = (EditText)findViewById(R.id.edtPhone);
        edtFirstName = (EditText)findViewById(R.id.edtFirstName);
        edtLastName = (EditText)findViewById(R.id.edtLastName);
        edtEmail = (EditText)findViewById(R.id.edtEmail);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openBack = new Intent(MainActivity3.this, LoginActivity.class);
                startActivity(openBack);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });
    }

    public void checkInput() {
        phone = edtPhone.getText().toString();
        first_name = edtFirstName.getText().toString();
        last_name = edtLastName.getText().toString();
        email = edtEmail.getText().toString();

        Boolean status = true;

        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("กรุณากรอกเบอร์โทรศัพท์");
            status = false;
        } else if (phone.length() != 10) {
            edtPhone.setError("กรุณากรอกเบอร์โทรศัพท์ให้ครบ");
            status = false;
        } else if (TextUtils.isEmpty(first_name)) {
            edtFirstName.setError("กรุณากรอกชื่อ");
            status = false;
        } else if (TextUtils.isDigitsOnly(first_name)) {
            edtFirstName.setError("กรุณากรอกชื่อเป็นตัวอักษร");
            edtFirstName.setText(null);
            status = false;
        } else if (TextUtils.isEmpty(last_name)) {
            edtLastName.setError("กรุณากรอกนามสกุล");
            status = false;
        } else if (TextUtils.isDigitsOnly(last_name)) {
            edtLastName.setError("กรุณากรอกนามสกุลเป็นตัวอักษร");
            status = false;
        } /*else if(TextUtils.isEmpty(email)){
            //isEmailValid(email);
            edtEmail.setError("กรุณากรอกอีเมล์ให้ถูกต้อง");
            edtEmail.setText(null);
            status = false;
        } */

        if (status){
            saveRegister();
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public void saveRegister(){
        final String url = "http://wssathit.codehansa.com/manage_user.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(final String response) {
                 String msg_alert = "";
                Log.d("onResponse", response);
                if(response.equals("success")) {
                    msg_alert = "บันทึกข้อมูลเรียบร้อย";
                } else {
                    msg_alert = "มีข้อมูลผู้ใช้อยู่แล้ว กรุณาเข้าสู่ระบบ";
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                builder.setIcon(R.drawable.ic_info);
                builder.setTitle("Information!");
                builder.setMessage(msg_alert);
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent openApp = new Intent(MainActivity3.this, MainActivity.class);
                        startActivity(openApp);*/
                        if(!response.equals("success")) {
                            Intent intent = new Intent(MainActivity3.this, LoginActivity.class);
                            startActivity(intent);
                        }
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity3.this);
                builder.setIcon(R.drawable.ic_info);
                builder.setTitle("Information!");
                builder.setMessage("มีข้อมูลผู้ใช้งานอยู่แล้ว กรุณาเเข้าสู่ระบบ");
                builder.setPositiveButton("ตกลง", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent openApp = new Intent(MainActivity3.this, MainActivity.class);
                        startActivity(openApp);*/
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
                params.put("cmd", "insert");
                params.put("phone", phone);
                params.put("name", first_name);
                params.put("lastname", last_name);
                params.put("email", email);

                return params;
            }
        };
        requestQueue.add(request);
    }
}
