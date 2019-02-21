package com.themehedi.appinionassignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Toolbar toolbar;
    private String URLstring = "https://raw.githubusercontent.com/appinion-dev/intern-dcr-data/master/data.json";
    private static ProgressDialog mProgressDialog;
    private ArrayList<GoodModel> goodModelArrayList;
    private ArrayList<String> array1 = new ArrayList<String>();
    private ArrayList<String> array2 = new ArrayList<String>();
    private ArrayList<String> array3 = new ArrayList<String>();
    private ArrayList<String> array4 = new ArrayList<String>();
    private Spinner product_group;
    private Spinner literature;
    private Spinner physician_sample;
    private Spinner gift;
    private Button submitBtn;

    private String text;

    String pg_list = "product_group_list";
    String pg = "product_group";

    String l_list = "literature_list";
    String l = "literature";

    String ps_list = "physician_sample_list";
    String ps = "sample";

    String g_list = "gift_list";
    String g = "gift";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = findViewById(R.id.main_page_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Intern DCR");
        toolbar.setTitleTextColor(Color.BLACK);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        submitBtn = findViewById(R.id.submit_btn);
        
        product_group = findViewById(R.id.product_group_spinner);
        literature = findViewById(R.id.literature_spinner);
        physician_sample = findViewById(R.id.physician_sample_spinner);
        gift = findViewById(R.id.gift_spinner);

        retrieveJSON(pg_list, pg, product_group, array1);
        retrieveJSON(l_list, l, literature, array2);
        retrieveJSON(ps_list, ps, physician_sample, array3);
        retrieveJSON(g_list, g, gift, array4);
        
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "done", Toast.LENGTH_SHORT).show();
                
            }
        });

    }


    private void retrieveJSON(final String list, final String name, final Spinner spinner_name, final ArrayList<String> names) {

        showSimpleProgressDialog(this, "Loading...","Fetching Json",false);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLstring,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("strrrrr", ">>" + response);

                        try {


                            //parsing for product group

                            JSONObject obj = new JSONObject(response);
                            if(obj.optString(list) !=null){

                                goodModelArrayList = new ArrayList<>();
                                JSONArray dataArray  = obj.getJSONArray(list);

                                for (int i = 0; i < dataArray.length(); i++) {

                                    GoodModel Model = new GoodModel();
                                    JSONObject dataobj = dataArray.getJSONObject(i);

                                    Model.setName(dataobj.getString(name));

                                    goodModelArrayList.add(Model);

                                }



                                for (int i = -1; i < goodModelArrayList.size(); i++){

                                    if(i<0){

                                        names.add("Choose");

                                    }
                                    else{

                                        names.add(goodModelArrayList.get(i).getName().toString());
                                    }
                                }

                                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.nothing_selected_text, names);
                                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
                                spinner_name.setAdapter(spinnerArrayAdapter);
                                spinner_name.setOnItemSelectedListener(MainActivity.this);
                                removeSimpleProgressDialog();

                            }




                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //displaying the error in toast if occurrs
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        // request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        requestQueue.add(stringRequest);


    }



    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(Context context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context, title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
