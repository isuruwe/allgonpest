package mud.per.iw.techapp;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class login extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    private String name;
    private String svcid;
    String[] menuArray;
    private String responseString;
    private String initials;
    private String Rnkname;
    private Menu menu;
    private String inBedMenuTitle = "Login";
    private String outOfBedMenuTitle = "Logout";
    private boolean inBed = false;
    private String smonth;
    private String yrval;
    @InjectView(R.id.input_email) EditText _emailText;
    @InjectView(R.id.input_password) EditText _passwordText;
    @InjectView(R.id.btn_login) Button _loginButton;
    @InjectView(R.id.link_signup) TextView _signupLink;
    private static MessageDigest md;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.inject(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    login();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void login() throws Exception {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

       // _loginButton.setEnabled(false);

//        final ProgressDialog progressDialog = new ProgressDialog(login.this,
//                R.style.AppTheme);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        loaduser(email,generateSHA1Hash(password));
//        Intent intent = new Intent(login.this, MainActivity.class);
//        startActivity(intent);
        // TODO: Implement your own authentication logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed
                        //onLoginSuccess();
                        // onLoginFailed();
                       // progressDialog.dismiss();
                    }
                }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);

        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() ) {
            _emailText.setError("enter a Username");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
    public void  setdata(){

        try {
            JSONObject jsonObject = null;

            jsonObject = new JSONObject(responseString);

            JSONArray we = null;

            we = jsonObject.getJSONArray("Table");


            JSONObject we1 = we.getJSONObject(0);

            String s1 = we1.get("UserID").toString();
            String t1 = we1.get("Description").toString();
            String u1 = we1.get("LName").toString();
            String v1 = we1.get("FName").toString();

            SharedPreferences.Editor editor = getSharedPreferences("userinfo", MODE_PRIVATE).edit();
            editor.putString("UserID", s1);
            editor.putString("Description", t1);
            editor.putString("LName", u1);
            editor.putString("FName", v1);
            editor.apply();
            SharedPreferences prefs = getSharedPreferences("userinfo", MODE_PRIVATE);
            String restoredText = prefs.getString("UserID", null);
            if (restoredText != null) {
                name = prefs.getString("LName", "no");//"No name defined" is the default value.
                svcid = prefs.getString("UserID", "no"); //0 is the default value.
                initials = prefs.getString("FName", "no");
                Rnkname = prefs.getString("Description", "no");


               // Log.wtf("testpass",cryptWithMD5("isuru"));
                inBed=true;

                //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Toast.makeText(login.this, "Login Successful!",
                        Toast.LENGTH_LONG).show();
                Intent intentl = new Intent(this, MainActivity.class);
                startActivity(intentl);
            }else {
                Toast.makeText(login.this, "Incorrect Username or Password!",
                        Toast.LENGTH_LONG).show();
            }



        }catch (Exception ex){
             Log.wtf("testpass",ex.toString());

            Toast.makeText(login.this, "Incorrect Username or Password!",
                    Toast.LENGTH_LONG).show();
        }


    }


    public static String generateSHA1Hash(String input) throws Exception {

        MessageDigest messagedigest = MessageDigest.getInstance("SHA");
        messagedigest.update(input.getBytes("UTF-8"));
        byte digest[] = messagedigest.digest();

        StringBuffer s = new StringBuffer(digest.length);
        int length = digest.length;
        for (int n = 0; n < length; n++) {
            int number = digest[n];
            number = (number < 0) ? (number + 256) : number;
            s.append(Integer.toString(number));
        }
        Log.wtf("testpass",s.toString());
        return s.toString();
    }

    public String loaduser(String  svcid,String  pass){

        String url = this.getApplicationContext().getResources().getString( R.string.weburl2)+"?pass="+pass+"&svcid="+svcid+"";
        Log.wtf("eee", url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        responseString = response.toString();
                        setdata();


                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.wtf("eee", "ww2");

                        // TODO: Handle error

                    }
                });

// Access the RequestQueue through your singleton class.
        singletongm.getInstance(this).addToRequestQueue(jsonObjectRequest);
        return  responseString;
    }



}
