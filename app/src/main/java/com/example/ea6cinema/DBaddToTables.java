package com.example.ea6cinema;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBaddToTables extends AsyncTask<Void, Void, Void> {

    private ResultSet resultSet = null, resultSet1 = null;
    private String user, pass, audiName = "", time = "";
    int mode = 0, audiSeats = 0, audiId = 0, movieID=0, audiID = 0;
    static String errorAdmin = "", errorAudi = "", errorSeats = "", errorScreening = "";
    Context context1 , context2, context3;


    public DBaddToTables(int mode,String user, String pass, Context context) {
        this.mode = mode;
        this.user = user;
        this.pass = pass;
        this.context1 = context;
    }

    public DBaddToTables(int mode, int audiId, String audiName, int audiSeats, Context context) {
        this.mode = mode;
        this.audiId = audiId;
        this.audiName = audiName;
        this.audiSeats = audiSeats;
        this.context2 = context;
    }

    public DBaddToTables(int mode, int movieID, int audiId, String time, Context context) {
        this.mode = mode;
        this.audiID = audiId;
        this.movieID = movieID;
        this.time = time;
        this.context3 = context;
    }

    //---------------------------------------------------------------
    /*(mode=0 add new admin to the table of admins).*/
    /*(mode=1 add new auditorium and seats to the DB).*/
    /*(mode=2 add new screening to the table of screenings).*/
    @Override
    protected Void doInBackground(Void... voids) {
        if(mode == 0){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                Statement statement1 = conn.createStatement();

                statement1.executeUpdate("INSERT INTO `admins` (`username`, `password`) VALUES " +
                                        "('"+user+"','"+pass+"')");

                statement1.close();
                conn.close();
            }
            catch(Exception e) {
                errorAdmin = e.toString();
                System.out.println(e.toString());
            }
        }

        else if(mode == 1){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                Statement statement1 = conn.createStatement();

                statement1.executeUpdate("INSERT INTO `auditorium` (`id`, `name`, `seats_no`) VALUES " +
                                        "('"+audiId+"','"+audiName+"','"+audiSeats+"')");

                statement1.close();
                conn.close();
            }
            catch(Exception e) {
                errorAudi = e.toString();
            }
            finally {
                if(errorAudi.isEmpty()){
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                        Statement statement1 = conn.createStatement();
                        Statement statement2 = conn.createStatement();

                        ResultSet resultSet = statement2.executeQuery("SELECT * FROM ea6cinema_db.seat");
                        resultSet.last();

                        int start = resultSet.getInt("id"), i = 1, row = 1, curr = 1;
                        while(i <= audiSeats){
                            if(curr > 10){
                                curr = 1;
                                row++;
                            }
                            start = start + 1;
                            statement1.executeUpdate("INSERT INTO `seat` (`id`, `row`, `number`, `auditorium_id`) VALUES " +
                                                    "('"+start+"','"+row+"','"+start+"','"+audiId+"')");

                            curr++;
                            i++;
                        }

                        statement1.close();
                        statement2.close();
                        resultSet.close();
                        conn.close();
                    }
                    catch(Exception e) {
                        errorSeats = e.toString();
                    }
                }
            }
        }

        else if(mode == 2){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                Statement statement1 = conn.createStatement();
                Statement statement2 = conn.createStatement();

                resultSet1 = statement2.executeQuery("SELECT * FROM ea6cinema_db.screening ");
                int size = 0;
                while(resultSet1.next()){
                    size++;
                }
                resultSet1.close();

                resultSet = statement1.executeQuery("SELECT * FROM ea6cinema_db.screening " +
                                                            "where screening_time = '"+time+"' AND audit_id = '"+audiID+"' ");

                if(!resultSet.next()){
                    statement2.executeUpdate("INSERT INTO `ea6cinema_db`.`screening` (`id`, `movie_id`," +
                            " `audit_id`, `screening_time`) VALUES ('"+(size+1)+"','"+movieID+"','"+audiID+"', '"+time+"')");
                }
                else{
                    errorScreening = "This screening is invalid!";
                }

                statement1.close();
                conn.close();
            }
            catch(Exception e) {
                errorScreening = e.toString();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        errorAdmin = "";
        errorAudi = "";
        errorSeats = "";
        errorScreening = "";
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(mode == 0){
            if(!errorAdmin.equals("")) {
                if(errorAdmin.contains("Duplicate")){
                    Toast.makeText(context1, "Admin existed! ", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(context1, "New admin added succefully! ", Toast.LENGTH_LONG).show();
            }
        }

        else if(mode == 1){
            if(!errorAudi.equals("")) {
                if(errorAudi.contains("Duplicate")){
                    Toast.makeText(context2, "Auditorium existed! ", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(context2, "New auditorium added succefully! ", Toast.LENGTH_LONG).show();
            }
            if(!errorSeats.equals("")){
                Toast.makeText(context2, "Error with adding seats!. please contact the manager. ", Toast.LENGTH_LONG).show();
            }
        }

        else if(mode == 2){
            if(errorScreening.isEmpty()){
                Toast.makeText(context3, "A new screening added successfully! ", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(context3, errorScreening, Toast.LENGTH_LONG).show();
            }
        }
    }
}