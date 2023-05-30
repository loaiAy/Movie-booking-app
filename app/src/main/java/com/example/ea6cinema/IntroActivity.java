package com.example.ea6cinema;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalTime;

public class IntroActivity extends AppCompatActivity {

    Button projectsBtn, ManagerBttn;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getSupportActionBar().hide();

        LocalTime time = LocalTime.now();

        // This block resetting the "reservation" and "seats_reserved" tables when the time is 10AM.
        if(time.getHour() == 10){
            new Task1(2).execute();

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            new Task1(1).execute();
            new Task1(3).execute();
        }

        projectsBtn = findViewById(R.id.projectBttn);
        ManagerBttn = findViewById(R.id.ManagerBttn);

        projectsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(IntroActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        /* On clicking the manager button, we check if the admin already logged in, if yes we move directly to
         admins activity, otherwise we move to the login page.
        */

        ManagerBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AdministratorActivity.active == true){
                    Intent intent = new Intent(IntroActivity.this, AdministratorActivity.class);
                    startActivity(intent);

                }
                else{
                    Intent intent = new Intent(IntroActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    /*When time is 10AM, This class will delete all the existed reservation and reserved_seats
    * and screenings from the Past day.*/
    class Task1 extends AsyncTask<Void, Void, Void> {

        String error = "";
        int mode = 0;

        public Task1(int mode) {
            this.mode = mode;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mode == 1){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                    Statement statement1 = conn.createStatement();
                    statement1.executeUpdate("DELETE FROM `ea6cinema_db`.`reservation`");

                    statement1.close();
                    conn.close();
                }
                catch(Exception e) {
                    error = e.toString();
                }
            }
            else if(mode == 2){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                    Statement statement1 = conn.createStatement();
                    statement1.executeUpdate("DELETE FROM `ea6cinema_db`.`seat_reserved`");

                    statement1.close();
                    conn.close();
                }
                catch(Exception e) {
                    error = e.toString();
                }
            }
            else if(mode == 3){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                    Statement statement1 = conn.createStatement();
                    statement1.executeUpdate("DELETE FROM `ea6cinema_db`.`screening`");

                    statement1.close();
                    conn.close();
                }
                catch(Exception e) {
                    error = e.toString();
                }
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
