package ca.mcgill.ecse321.tutoringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/* This class is used to show the current availabilities of the logged in user.
*  In order to add availabilities, go back to dashboard and select "Set Availability."
*  Upon doing so, you will see it when you come back to this page.
*  This code is similar to SessionsActivity to have a mutual format. */
public class MyAvailability extends AppCompatActivity {


    private ArrayAdapter<String> availAdapter;
    private List<String> availNames = new ArrayList<>();

    private boolean createAvail = true;
    private String tutorEmail;

    private String selectedAvailID = "";

    private String error = null;

    //displays when created
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_availability);

        Spinner availSpinner = (Spinner) findViewById(R.id.availspinner);

        availAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, availNames);
        availAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        availSpinner.setAdapter(availAdapter);

        availSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // listener code is run twice - once at creation and once when selection is made
                // ensures that the selection is not requested upon creation
                if(createAvail == true){
                    createAvail = false;
                }
                else {
                    Object index = parentView.getSelectedItemPosition();
                    selectedAvailID = index.toString();
                    int index_int = Integer.parseInt(selectedAvailID)-1;
                    //refreshing table with session information using Index.
                    refreshAvailTable(index_int);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}

        });
        //refreshing sessions list
        /* The user "william.bouchard3@mail.mcgill.ca" is hard coded because we encountered a bug.
        *  More information can be found in our Project Wiki on GitHub: https://github.com/McGill-ECSE321-Fall2019/project-group-2/wiki */
        refreshList(availAdapter, availNames,
                "/availabilities/william.bouchard3@mail.mcgill.ca/", "id");
    }

    private void refreshList(final ArrayAdapter<String> adapter, final List<String> names,
                             final String restFunctionName, final String identifier0){

        String fcn = restFunctionName;
        HttpUtils.get(restFunctionName, new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                // iterate through the objects in the response to display the information in the
                // dropdown list
                names.clear();
                names.add("Please select...");
                for( int i = 0; i < response.length(); i++){
                    try {
                        //String option = response.getJSONObject(i).getString(identifier0);
                        names.add(Integer.toString(i+1));
                    } catch (Exception e) {
                        error += e.getMessage();
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
            }
        });


    }

    /* This is called when a session is selected and the table needs to be populated with it's information */
    public void refreshAvailTable(final int index){
        // get the text objects by id from the view so that they can be populated
        final TextView date = (TextView) findViewById(R.id.date);
        final TextView startTime = (TextView) findViewById(R.id.startTime);
        final TextView endTime = (TextView) findViewById(R.id.endTime);
        final TextView status = (TextView) findViewById(R.id.status);

        // send the HTTP request to get the currently logged in user => tutorEmail
        // This gets the correct logged in tutor but tutorEmail remains null (recurring bug, check wiki).
        HttpUtils.get("/user/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    tutorEmail = (response.getString("email"));
                } catch (Exception e) {
                    error += e.getMessage();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
            }
        });

        // send the HTTP request to get the sessions of currently logged in student
        /* The user "william.bouchard3@mail.mcgill.ca" is hard coded because we encountered a bug.
        *  The method, however, works. This http request is properly integrated. If print statements
        *  are added, you can see that we can get the correct user. */
        HttpUtils.get("/availabilities/william.bouchard3@mail.mcgill.ca/", new RequestParams(), new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {

                try {
                    //setting the values for table based on the data from JSON Array
                    date.setText(response.getJSONObject(index).getString("date"));
                    startTime.setText(response.getJSONObject(index).getString("startTime"));
                    endTime.setText(response.getJSONObject(index).getString("endTime"));
                    status.setText(response.getJSONObject(index).getString("isApproved"));

                } catch (Exception e) {
                    error += e.getMessage();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
            }
        });
    }
}