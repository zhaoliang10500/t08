package t08.ecse321.passengerrideshare;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


public class LoginActivity extends AppCompatActivity {

    private String error = null;
    public final String ROLE = "Passenger";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        refreshErrorMessage();

    }

    private void refreshErrorMessage() {
        // set the error message
        TextView tvError = (TextView) findViewById(R.id.error);
        tvError.setText(error);

        if (error == null || error.length() == 0) {
            tvError.setVisibility(View.GONE);
        } else {
            tvError.setVisibility(View.VISIBLE);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //When click login button, will attempt to login
    //If login successful, will switch to MyTripsActivity.class
    public void loginButton(View view) {
        //Creates new intent and gets username and password from text view
       // final Intent intent = new Intent(this, MyTripsActivity.class);
        final EditText username_text = (EditText) findViewById(R.id.username_text);
        final EditText password_text = (EditText) findViewById(R.id.password_text);
        final String username = username_text.getText().toString();
        final String password = password_text.getText().toString();

        refreshErrorMessage();

        Bundle extras = new Bundle();
        extras.putString("EXTRA_USERNAME", username);
        extras.putString("EXTRA_PASSWORD", password);
        //intent.putExtras(extras);

        //Creates HTTP params to authorize user according to rest model
        error = "";
        RequestParams params = new RequestParams();
        params.add("username", username);
        params.add("password", password);
        params.add("role", ROLE);

        //Sends HTTP post method, if successful (response != -1, switches to MyTripsActivity view), else, displays error
        t08.ecse321.driverrideshare.HttpUtils.post("api/user/authorize", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
                username_text.setText("");
                password_text.setText("");

                try {
                    int result = response.getInt("data");
                    if(result == -1) {
                        error = "Username or password invalid.";
                    } else {
                        error = "SUCCESS"; //Temporary after replace with start Activity
                      //  startActivity(intent);
                    }
                } catch(Exception e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject json) {
                username_text.setText("");
                password_text.setText("");
                error = "Failure: ";
                Log.e("MyApp", "Caught error", throwable); //This helps us to log our errors
                try {
                    if(json.getInt("data") == -1) {
                        error += "Username or password invalid.";
                    } else if(!Integer.valueOf(json.getInt("data")).equals(-1)) {
                        error += "Status " + String.valueOf(statusCode); //This case should not happen, may occur if backend server does not create json correctly
                    }
                } catch (Exception e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });


    }

    //When click register button, switch to register activity
    public void registerButton(View view) {
      //  Intent intent = new Intent(this, RegisterActivity.class);
       // startActivity(intent);
    }
}
