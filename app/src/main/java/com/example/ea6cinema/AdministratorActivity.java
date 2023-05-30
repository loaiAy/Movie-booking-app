package com.example.ea6cinema;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
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
import java.util.List;

public class AdministratorActivity extends AppCompatActivity {

    static boolean active = false; // to save the log in of the admin.

    /*These 4 lists is for saving the data of the available movies and auditoriums.*/
    List<String> movieNames = new ArrayList<>();
    List<String> audiNames = new ArrayList<>();
    List<Integer> movieIDs = new ArrayList<>();
    List<Integer> audiIDs = new ArrayList<>();

    Button sign, submitAudiBttn, submitScreeningBttn, showPass;
    EditText pass, user, id, name, seats_no;
    TextView movieMenu, audiMenu, timeMenu;

    /*When the instance of Administrator activity is created, the active variable set to be true.
    * On this activity, the admin can add a new admin, a new auditorium(the seats for the new
    * auditorium updated on the background alone), a new screening. */

    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Settings");
        active = true;

        new Task1().execute();

        sign = findViewById(R.id.SignBttn);
        submitAudiBttn = findViewById(R.id.SubmitaudiBttn);
        submitScreeningBttn = findViewById(R.id.SubmitScreeningBttn);
        showPass = findViewById(R.id.showPass);
        user = findViewById(R.id.username);
        pass = findViewById(R.id.pass);
        id = findViewById(R.id.idtext);
        name = findViewById(R.id.nametext);
        seats_no = findViewById(R.id.seatnumbertext);
        movieMenu = findViewById(R.id.moviemenu);
        audiMenu = findViewById(R.id.audimenu);
        timeMenu = findViewById(R.id.timemenu);

        showPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getInputType() == 129){
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                pass.setSelection(pass.getText().length());
            }
        });

        movieMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                PopupMenu popupMenu = new PopupMenu(AdministratorActivity.this, movieMenu);
                while(i < movieNames.size()){
                    popupMenu.getMenu().add(movieNames.get(i));
                    i++;
                }

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        movieMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        movieMenu.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        movieMenu.setText(" " + menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        audiMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i = 0;
                PopupMenu popupMenu = new PopupMenu(AdministratorActivity.this, audiMenu);
                while(i < audiNames.size()){
                    popupMenu.getMenu().add(audiNames.get(i));
                    i++;
                }
                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        audiMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        audiMenu.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        audiMenu.setText(" " + menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        timeMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(AdministratorActivity.this, timeMenu);
                popupMenu.getMenu().add("15:00");
                popupMenu.getMenu().add("18:00");
                popupMenu.getMenu().add("21:00");
                popupMenu.getMenu().add("00:00");

                // Inflating popup menu from popup_menu.xml file
                popupMenu.getMenuInflater().inflate(R.menu.popup_menu, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        timeMenu.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                        timeMenu.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
                        timeMenu.setText(" " + menuItem.getTitle());
                        return true;
                    }
                });
                // Showing the popup menu
                popupMenu.show();
            }
        });

        sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = user.getText().toString();
                String password = pass.getText().toString();

                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() || !email.isEmpty()){
                    if(password.length() < 5){
                        System.out.println("length short");
                        Toast.makeText(AdministratorActivity.this, "Password to short !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new DBaddToTables(0, email, password, getApplicationContext()).execute();
                    }
                }
                else{
                    Toast.makeText(AdministratorActivity.this, "Invalid email address or username!", Toast.LENGTH_LONG).show();
                }
                user.setText("");
                pass.setText("");
            }
        });

        submitScreeningBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = movieMenu.getText().toString().substring(1);
                String auditoriumName = audiMenu.getText().toString().substring(1);
                String time = timeMenu.getText().toString().substring(1);

                int movieId = 0, audiId = 0, i = 0;

                while(i < movieNames.size()){
                    if(movieNames.get(i).equals(movieName)){
                        movieId = movieIDs.get(i);
                        break;
                    }
                    else{
                        i++;
                    }
                }

                i = 0;
                while(i < audiNames.size()){
                    if(audiNames.get(i).equals(auditoriumName)){
                        audiId = audiIDs.get(i);
                        break;
                    }
                    else{
                        i++;
                    }
                }

                if(movieName.length() == 1 || auditoriumName.length() == 1 || time.length() == 1){
                    Toast.makeText(AdministratorActivity.this, "Missed info! please provide valid info.",
                                    Toast.LENGTH_LONG).show();
                }
                else {
                    new DBaddToTables(2, movieId, audiId,
                            time, getApplicationContext()).execute();
                }
            }
        });

        submitAudiBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String audiId = id.getText().toString();
                String audiName = name.getText().toString();
                String audiSeats = seats_no.getText().toString();

                if(audiId.isEmpty() || audiSeats.isEmpty() || audiName.isEmpty()){
                    Toast.makeText(AdministratorActivity.this, "Missing information! please provide valid data.",
                            Toast.LENGTH_LONG).show();
                }
                else if(!audiId.matches("[0-9]+")){
                    Toast.makeText(AdministratorActivity.this, "Id must contain only digits", Toast.LENGTH_LONG).show();
                }
                else if(!audiSeats.matches("[0-9]+")){
                    Toast.makeText(AdministratorActivity.this, "Seats no' must contain only digits", Toast.LENGTH_LONG).show();
                }
                else if(Integer.parseInt(audiSeats) < 10){
                    Toast.makeText(AdministratorActivity.this, "Invalid Seats no'!!", Toast.LENGTH_LONG).show();
                }
                else{
                    new DBaddToTables(1, Integer.parseInt(audiId), audiName,
                                        Integer.parseInt(audiSeats), getApplicationContext()).execute();

                    id.setText("");
                    name.setText("");
                    seats_no.setText("");
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


    /*This Task1 inner class is responsible for retrieving data from the DB of which movies
      is available and auditoriums at the cinema. Later they will be added to the movie popup-menu and
      the auditorium popup-menu for adding a new screening.*/
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

                ResultSet resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`movie`");

                int i = 0;
                while(resultSet.next()){
                    movieNames.add(resultSet.getString("title"));
                    movieIDs.add(resultSet.getInt("id"));
                }

                resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`auditorium`");

                while(resultSet.next()){
                    audiNames.add(resultSet.getString("name"));
                    audiIDs.add(resultSet.getInt("id"));
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
