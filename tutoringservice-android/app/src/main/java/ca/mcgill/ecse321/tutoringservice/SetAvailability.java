package ca.mcgill.ecse321.tutoringservice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import ca.mcgill.ecse321.tutoringserivce.R;

public class SetAvailability extends AppCompatActivity {
    private String error = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_availability);

    }

    private Bundle getTimeFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split(":");
        int hour = 12;
        int minute = 0;

        if (comps.length == 2) {
            hour = Integer.parseInt(comps[0]);
            minute = Integer.parseInt(comps[1]);
        }

        rtn.putInt("hour", hour);
        rtn.putInt("minute", minute);

        return rtn;
    }

    private Bundle getDateFromLabel(String text) {
        Bundle rtn = new Bundle();
        String comps[] = text.toString().split("-");
        int day = 1;
        int month = 1;
        int year = 1;

        if (comps.length == 3) {
            day = Integer.parseInt(comps[0]);
            month = Integer.parseInt(comps[1]);
            year = Integer.parseInt(comps[2]);
        }

        rtn.putInt("day", day);
        rtn.putInt("month", month-1);
        rtn.putInt("year", year);

        return rtn;
    }

    public void showTimePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getTimeFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        TimePickerFragment newFragment = new TimePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        TextView tf = (TextView) v;
        Bundle args = getDateFromLabel(tf.getText().toString());
        args.putInt("id", v.getId());

        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.setArguments(args);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setTime(int id, int h, int m) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d:%02d", h, m));
    }

    public void setDate(int id, int d, int m, int y) {
        TextView tv = (TextView) findViewById(id);
        tv.setText(String.format("%02d-%02d-%04d", d, m + 1, y));
    }

    // AddEvent but to be changed to SetAvailability
    // TODO : Make this SetAvailability
    /*
    public void setAvailability(View v) {
        // start time
        TextView tv = (TextView) findViewById(R.id.startTime);
        String text = tv.getText().toString();
        String comps[] = text.split(":");

        int startHours = Integer.parseInt(comps[0]);
        int startMinutes = Integer.parseInt(comps[1]);

        // start date
        tv = (TextView) findViewById(R.id.startDate);
        text = tv.getText().toString();
        comps = text.split("-");

        int year = Integer.parseInt(comps[2]);
        int month = Integer.parseInt(comps[1]);
        int day = Integer.parseInt(comps[0]);

        // end time
        TextView tvEnd = (TextView) findViewById(R.id.endTime);
        String textEnd = tvEnd.getText().toString();
        String compsEnd[] = textEnd.split(":");

        int endHours = Integer.parseInt(compsEnd[0]);
        int endMinutes = Integer.parseInt(compsEnd[1]);

        // end date
        tvEnd = (TextView) findViewById(R.id.endDate);
        text = tvEnd.getText().toString();
        compsEnd = text.split("-");

        int yearEnd = Integer.parseInt(compsEnd[2]);
        int monthEnd = Integer.parseInt(compsEnd[1]);
        int dayEnd = Integer.parseInt(compsEnd[0]);

        // name -- TODO: Do I need an availability ID?
        tv = (TextView) findViewById(R.id.newevent_name);
        String name = tv.getText().toString();

        // TODO: supply the end time to the REST request as an additional parameter
        // Reminder: calling the service looks like this:
        // https://eventregistration-backend-123.herokuapp.com/events/testEvent?date=2013-10-23&startTime=00:00&endTime=23:59

        RequestParams rp = new RequestParams();

        NumberFormat formatter = new DecimalFormat("00");
        rp.add("date", year + "-" + formatter.format(month) + "-" + formatter.format(day));
        rp.add("startTime", formatter.format(startHours) + ":" + formatter.format(startMinutes));
        rp.add("endTime", formatter.format(endHours) + ":" + formatter.format(endMinutes));

        HttpUtils.post("events/" + name, rp, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                refreshErrorMessage();
                ((TextView) findViewById(R.id.newevent_name)).setText("");
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                try {
                    error += errorResponse.get("message").toString();
                } catch (JSONException e) {
                    error += e.getMessage();
                }
                refreshErrorMessage();
            }
        });
    } */


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
}