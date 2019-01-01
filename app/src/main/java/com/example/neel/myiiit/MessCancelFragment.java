package com.example.neel.myiiit;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.MalformedURLException;

import okhttp3.Credentials;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MessCancelFragment extends Fragment {
    String username, pswd, date1, month1, year1, date2, month2, year2 ;
    Spinner date_select1, month_select1, year_select1, date_select2, month_select2, year_select2;
    CheckBox breakfast_box, lunch_box, dinner_box, uncancel_box;
    Button submit_btn;
    TextView cancel_msg;
    SharedPreferences preferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_mess_cancel, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        username = preferences.getString("username", null);
        pswd = preferences.getString("password", null);

        date_select1 = rootView.findViewById(R.id.date_select1);
        month_select1 = rootView.findViewById(R.id.month_select1);
        year_select1 = rootView.findViewById(R.id.year_select1);
        date_select2 = rootView.findViewById(R.id.date_select2);
        month_select2 = rootView.findViewById(R.id.month_select2);
        year_select2 = rootView.findViewById(R.id.year_select2);
        cancel_msg = rootView.findViewById(R.id.cancel_msg);

        breakfast_box = rootView.findViewById(R.id.breakfast_box);
        lunch_box = rootView.findViewById(R.id.lunch_box);
        dinner_box = rootView.findViewById(R.id.dinner_box);
        uncancel_box = rootView.findViewById(R.id.uncancel_box);

        submit_btn = rootView.findViewById(R.id.submit_btn);

        String[] dates=new String[31];
        for(int x=1;x<=31;x++){
            dates[x-1]=String.valueOf(x);
        }
        ArrayAdapter<String> dateAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item, dates);
        date_select1.setAdapter(dateAdapter);
        date_select2.setAdapter(dateAdapter);
        date_select1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date1 = (String) parent.getItemAtPosition(position);
                Log.d("date1", date1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        date_select2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date2 = (String) parent.getItemAtPosition(position);
                Log.d("date2", date2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        String[] months={"JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC"};
        ArrayAdapter<String> monthAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,months);
        month_select1.setAdapter(monthAdapter);
        month_select2.setAdapter(monthAdapter);
        month_select1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month1 = (String)parent.getItemAtPosition(position);
                Log.d("month1", month1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        month_select2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                month2 = (String)parent.getItemAtPosition(position);
                Log.d("month2", month2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        String[] years=new String[10];
        for(int x=2018;x<=2027;x++){
            years[x-2018]=String.valueOf(x);
        }
        ArrayAdapter<String> yearAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,years);
        year_select1.setAdapter(yearAdapter);
        year_select2.setAdapter(yearAdapter);
        year_select1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year1 = (String)parent.getItemAtPosition(position);
                Log.d("year1", year1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        year_select2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                year2 = (String)parent.getItemAtPosition(position);
                Log.d("year2", year2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessCancelTask messCancelTask = new MessCancelTask();
                messCancelTask.execute();
            }
        });

        Log.d("Mess Cancel Fragment", "Created");
        return rootView;
    }

    private class MessCancelTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            String result = "";
            try{
                String credentials = Credentials.basic(username, pswd);
                OkHttpClient client = Client.getClient(getContext());
                String url = "https://reverseproxy.iiit.ac.in/browse.php?u=https%3A%2F%2Fmess.iiit.ac.in%2Fmess%2Fweb%2Fstudent_cancel_process.php&b=4";

                if(date1.length() == 1) {
                    date1 = "0" + date1;
                }
                if(date2.length() == 1) {
                    date2 = "0" + date2;
                }

                String startdate = date1 + "-" + month1 + "-" + year1;
                String enddate = date2 + "-" + month2 + "-" + year2;

                Log.d("start date", startdate);
                Log.d("end date", enddate);

                RequestBody body = new FormBody.Builder()
                        .add("startdate", startdate)
                        .add("enddate", enddate)
                        .add("breakfast[]", (breakfast_box.isChecked())?"1":"0")
                        .add("lunch[]", (lunch_box.isChecked())?"1":"0")
                        .add("dinner[]", (dinner_box.isChecked())?"1":"0")
                        .add("uncancel[]",(uncancel_box.isChecked())?"1":"0")
                        .build();

                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .header("Authorization", credentials)
                        .build();

                Response response = client.newCall(request).execute();

                Document cancel_soup = Jsoup.parse(response.body().string());

                if (cancel_soup.title().equals(getString(R.string.cas_title)) || cancel_soup.title().equals(getString(R.string.rev_title)) ) {
                    String r =  LoginActivity.Login(getContext());
                    Log.d("login result", r);

                    client = Client.getClient(getContext());
                    response = client.newCall(request).execute();
                    cancel_soup = Jsoup.parse(response.body().string());
                }

                result = cancel_soup.getElementsByClass("post").get(1).getElementsByTag("font").get(0).text();

            }catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("message", result);
            cancel_msg.setText(result);
        }
    }
}