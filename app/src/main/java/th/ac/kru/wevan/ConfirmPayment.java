package th.ac.kru.wevan;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ConfirmPayment extends AppCompatActivity {
    private Button btnChoose, btnConfirm;
    private ImageView imgPayment;

    private static int IMAGE_PICKER = 1;

    private String re_id;

    private Uri fullPhotoUri;

    Uri uriImage;
    InputStream inputStream;
    Bitmap imageMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        intialValue();
    }

    public void intialValue() {
        imgPayment = (ImageView) findViewById(R.id.img_view_payment);
        btnChoose = (Button) findViewById(R.id.btn_choose_img);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        re_id = getIntent().getExtras().getString("re_id");

        //Toast.makeText(this, re_id, Toast.LENGTH_LONG).show();

        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("onClick: ", imageMap.toString());
                //saveIMGPayment(re_id);
            }
        });
    }
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), IMAGE_PICKER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICKER  && resultCode == RESULT_OK && null != data) {
                Uri selectedImageURI = data.getData();
                Picasso.with(ConfirmPayment.this).load(selectedImageURI).into(imgPayment);
            Log.d("onActivityResult: ", selectedImageURI.toString());
        }
    }

    public void saveIMGPayment(final String re_id) {
        final String url = "http://wssathit.codehansa.com/manage_reserv.php";

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest request = new StringRequest(com.android.volley.Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("onResponse", response);
                AlertDialog.Builder builder = new AlertDialog.Builder(ConfirmPayment.this);
                builder.setIcon(R.drawable.van1);
                builder.setTitle("Information!");
                builder.setMessage("ยืนยันการชำระเงินเรียบร้อย");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        /*Intent openApp = new Intent(MainActivity3.this, MainActivity.class);
                        startActivity(openApp);*/
                        dialogInterface.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
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
                params.put("cmd", "insert");
                params.put("phone", re_id);
                params.put("name", fullPhotoUri.toString());

                return params;
            }
        };
        requestQueue.add(request);
    }
}
