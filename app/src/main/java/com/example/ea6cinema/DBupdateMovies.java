package com.example.ea6cinema;

import android.os.AsyncTask;
import android.util.ArraySet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Set;

/*This class deletes unavailable movies and add a new movies if they exist.*/

public class DBupdateMovies extends AsyncTask<Void, Void, Void> {

    String error = "";
    private ResultSet resultSet = null;
    private ArrayList<Model> list;
    private Set<Integer> keys = new ArraySet<>();
    Statement statement1, statement2, statement3;

    public DBupdateMovies(ArrayList<Model> modelArrayList) {
        this.list = modelArrayList;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        int j = 0;
        while (j < list.size()) {
            keys.add(list.get(j).getId());
            j++;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
            statement1 = conn.createStatement();
            statement2 = conn.createStatement();
            statement3 = conn.createStatement();


            /*Block of code to check if there is unused rows in the table, if yes, we delete them !!*/
            resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`movie`");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                if (!keys.contains(id)) {
                    statement2.executeUpdate("DELETE FROM `ea6cinema_db`.`movie` WHERE (`id` =" + " '" + id + "')");
                }
            }


            /*Block of code to check whether the table contains each key, if not we add the new key to the table*/
            j = 0;
            while (j < list.size()) {
                int id = list.get(j).getId();
                String title = list.get(j).getTitle().replaceAll("'", "");
                String language = list.get(j).getOriginal_language();
                String Release_date = list.get(j).getRelease_date();

                statement3.executeUpdate("INSERT IGNORE INTO `ea6cinema_db`.`movie` (`id`, `title`, `language`, `Release_date`) VALUES " +
                                        "('" + id + "','" + title + "','" + language + "','" + Release_date + "')");

                j++;
            }
            conn.close();
        } catch (Exception e) {
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