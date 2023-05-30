package com.example.ea6cinema;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class purchaseActivity extends AppCompatActivity {

    private String name="", email="", chosenTime = "";
    private int movieId = 0, chosenSeats = 0, chosenAudi = 0;
    List<Integer> audiIds = new ArrayList<>();
    List<String> times = new ArrayList<>();
    EditText nameEdit, emailEdit, phoneEdit;
    TextView selectAudi, selectTime, selectNoSeats, title;
    Button goToChooseSeats;

    /* In this class, The available data such as(auditorium, time) for the requested movie
       is displayed for the user to choose from. Along with number of seats and the user
       contact info. After inserting all the data properly, The user transferred to seat selection activity. */

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Purchase");

        Intent intent = getIntent();
        movieId = intent.getIntExtra("movieId", 0);
        new Task1().execute();

        title = findViewById(R.id.movieTitle);
        title.setText(intent.getStringExtra("title"));
        nameEdit = findViewById(R.id.nameEditText);
        emailEdit = findViewById(R.id.emailEditText);
        selectAudi = findViewById(R.id.selectaudittextview);
        selectTime = findViewById(R.id.selectTimetextview);
        selectNoSeats = findViewById(R.id.selectseatstextview);
        goToChooseSeats = findViewById(R.id.goToChooseSeats);
        phoneEdit = findViewById(R.id.phoneEditText);
    }

    @Override
    protected void onStart() {
        super.onStart();

        selectAudi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(audiIds);
                PopupMenu popupMenu = new PopupMenu(purchaseActivity.this, selectAudi);
                for(int i = 0; i < audiIds.size(); i++){
                    popupMenu.getMenu().add(Integer.toString(audiIds.get(i)));
                }

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        selectAudi.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        selectAudi.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        selectAudi.setText(" " + menuItem.getTitle());
                        chosenAudi = Integer.parseInt((String)menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Collections.sort(times);
                PopupMenu popupMenu = new PopupMenu(purchaseActivity.this, selectTime);
                for(int i = 0; i < times.size(); i++){
                    popupMenu.getMenu().add(times.get(i));
                }

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        selectTime.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        selectTime.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        selectTime.setText(" " + menuItem.getTitle());
                        chosenTime = (String)menuItem.getTitle();
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        selectNoSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(purchaseActivity.this, selectNoSeats);
                for(int i = 1 ; i <= 10; i++){
                    popupMenu.getMenu().add(Integer.toString(i));
                }
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        selectNoSeats.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        selectNoSeats.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        selectNoSeats.setText(" " + menuItem.getTitle());
                        chosenSeats = Integer.parseInt((String)menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        /*After filling all the needed data, When clicking this button it checks if all the data filled properly.
        * If no, it alerts to the user, otherwise it starts a seatSelection activity with all the movie and user data.*/
        goToChooseSeats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenAudi != 0 && chosenSeats != 0 && !chosenTime.equals("")){
                    if(nameEdit.getText().toString().equals("") || emailEdit.getText().toString().equals("")
                    || phoneEdit.getText().toString().equals("")){
                        Toast.makeText(purchaseActivity.this, "Missing contact info! please provide valid info.", Toast.LENGTH_LONG).show();
                    }
                    else{
                        if(android.util.Patterns.EMAIL_ADDRESS.matcher(emailEdit.getText().toString()).matches()){
                            Intent intent = new Intent(purchaseActivity.this, seatSelectionActivity.class);
                            intent.putExtra("audiID", chosenAudi);
                            intent.putExtra("movieID", movieId);
                            intent.putExtra("time", chosenTime);
                            intent.putExtra("number of seats", chosenSeats);
                            intent.putExtra("name", nameEdit.getText().toString());
                            intent.putExtra("email", emailEdit.getText().toString());
                            intent.putExtra("number", phoneEdit.getText().toString());
                            intent.putExtra("title", title.getText().toString());
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(purchaseActivity.this, "Invalid email address!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
                else{
                    Toast.makeText(purchaseActivity.this, "Missing details! Please check your selection", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    /*The task1 class retrieve all the available auditoriums and times for the requested movie.*/
    class Task1 extends AsyncTask<Void, Void, Void> {

        String error = "";

        public Task1() {

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                Statement statement1 = conn.createStatement();

                ResultSet resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`screening` " +
                                                                "where movie_id = '"+movieId+"'");

                while(resultSet.next()){
                    if(!audiIds.contains(resultSet.getInt("audit_id"))){
                        audiIds.add(resultSet.getInt("audit_id"));
                    }
                    if(!times.contains(resultSet.getString("screening_time"))){
                        times.add(resultSet.getString("screening_time"));
                    }
                }
                statement1.close();
                conn.close();
            }
            catch(Exception e) {
                error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(error != "") {
                System.out.println("errors is : " + error);
            }
            super.onPostExecute(aVoid);
        }
    }
}
