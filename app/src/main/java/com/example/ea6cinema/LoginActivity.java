package com.example.ea6cinema;

import static android.widget.Toast.LENGTH_LONG;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

/*This class is responsible for the admins log in activity.*/
public class LoginActivity extends AppCompatActivity {

    Button showPassBttn, loginBttn;
    EditText passWord,userName;

    @SuppressLint("MissingInflatedId")
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Login");

        loginBttn = findViewById(R.id.LoginBttn);
        passWord = findViewById(R.id.editTextTextPassword);
        userName = findViewById(R.id.usernameEditText);
        showPassBttn = findViewById(R.id.showPass);

        showPassBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(passWord.getInputType() == 129){
                    passWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                }
                else{
                    passWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                passWord.setSelection(passWord.getText().length());
            }
        });

        loginBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = userName.getText().toString();
                String password = passWord.getText().toString();

                if(!user.isEmpty()){
                    if(password.length() < 5){
                        Toast.makeText(LoginActivity.this, "Password to short !", Toast.LENGTH_LONG).show();
                    }
                    else{
                        new Task(user,password).execute();
                    }
                }
                else{
                    Toast.makeText(LoginActivity.this, "Invalid username!", Toast.LENGTH_LONG).show();
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

    /*Inner class to check if the admin inserted data is authorized to enter the administrator activity.*/
    class Task extends AsyncTask<Void, Void, Void> {

        String error="";
        String user, pass;
        ResultSet resultSet;
        private HashMap<String, String> Keys = new HashMap<>();

        public Task(String user, String pass) {
            this.user = user;
            this.pass = pass;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                Statement statement1 = conn.createStatement();
                resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`admins`");

                /*If statement to check if there a permissions existed. if no it throws a message with denying the request.
                * Otherwise, on else block, it add all the permission details to the keys hashmap.*/
                if(!resultSet.next()){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(LoginActivity.this, "There is no permissions to access this section" , LENGTH_LONG);
                            toast.show();
                        }
                    });
                    userName.setText("");
                    passWord.setText("");
                }
                else {
                    resultSet.beforeFirst();
                    while (resultSet.next()) {
                        Keys.put(resultSet.getString("username"), resultSet.getString("password"));
                    }
                }

                /*If statement to check if the entered data by the user is authorized or not.*/
                if(!Keys.containsKey(user)){
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(LoginActivity.this, "wrong username! please try again" , LENGTH_LONG);
                            toast.show();
                        }
                    });
                }
                else{
                    if(!Keys.get(user).equals(pass)){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(LoginActivity.this, "invalid password! please try again" , LENGTH_LONG);
                                toast.show();
                            }
                        });
                    }
                    else{
                        Intent intent = new Intent(LoginActivity.this, AdministratorActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
                resultSet.close();
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
