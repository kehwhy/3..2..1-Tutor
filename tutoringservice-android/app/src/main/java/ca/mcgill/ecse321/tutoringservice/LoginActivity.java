package ca.mcgill.ecse321.tutoringservice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AppCompatActivity;
import cz.msebera.android.httpclient.Header;

public class LoginActivity extends AppCompatActivity {

    //global static variable which will be accessed by other pages
    public static String tEmail;

    //initializing error message
    private String error = "";
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        refreshErrorMessage();
        intent = new Intent(this, MainActivity.class);
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

    //send message is called when the login button is clicked
    public void sendMessage(View view) {
        //clearing the error message
        error = "";

        //finding the edit text components for email and password
        final EditText tvEmail = (EditText) findViewById(R.id.email);
        final EditText tvPassword = (EditText) findViewById(R.id.password);

        //initailizing parameters and adding users text
        RequestParams params = new RequestParams();
        params.put("tutorEmail", tvEmail.getText().toString());
        params.put("password", tvPassword.getText().toString());

        //post method to interact with the backend and login the user
        HttpUtils.post("login/" + tvEmail.getText().toString(), params, new JsonHttpResponseHandler() {
            //our on success method is, for some reason, never called
            //instead a different type of failure method is called
            //so we treated that response as the success and left the onsuccess method empty
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
            }

            //on failure we update the global error message and refresh the error message
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }

            //this onfailure method is called after a successful login
            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable throwable) {
                //setting the global static variable which holds the currently logged on tutor's email
                tEmail = tvEmail.getText().toString();
                //launching to the dashboard page
                startActivity(intent);
                error += errorResponse;
                refreshErrorMessage();
            }

        });

    }

}