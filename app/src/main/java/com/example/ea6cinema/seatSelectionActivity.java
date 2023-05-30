package com.example.ea6cinema;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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

/*This class purpose is to build the seats viewGroup and for booking the seats.*/
public class seatSelectionActivity extends AppCompatActivity implements View.OnClickListener {

    ViewGroup groupLayout;
    Button bookTicketsBttn;
    boolean Success = true;
    static String bookingError = "";
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;

    /*example of seats string to define how the auditorium looks like. We will adjust it properly later.*/
    String seats = "_UUUUUAAAUU_/"
                    + "__________/"
                    + "UU_AAAUUU_UU/"
                    + "UU_UUAAAA_AA/"
                    + "AA_AAAAAA_AA/"
                    + "AA_AAUUUU_AA/"
                    + "UU_UUUUUU_AA/"
                    + "__________/";

    List<TextView> seatViewList = new ArrayList<>();
    List<Integer> reservedSeats = new ArrayList<>();
    int seatSize = 90, seatGaping = 10, chosenSeats = 0, count = 0;
    int STATUS_AVAILABLE = 1, STATUS_BOOKED = 2, audiID=0, movieID=0, screening_id;
    private String time = "", selectedIds = "", costumerName = "", costumerEmail = "", costumerPhone = "", movieTitle = "";

    /*When creating the instance of this class, we get the intent from the purchaseActivity with all
    * the extra data. This data will be saved on the DB tables after a successful booking.*/
    @SuppressLint({"MissingInflatedId"})
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        audiID = intent.getIntExtra("audiID", 0);
        movieID = intent.getIntExtra("movieID", 0);
        time = intent.getStringExtra("time");
        costumerName = intent.getStringExtra("name");
        costumerEmail = intent.getStringExtra("email");
        costumerPhone = intent.getStringExtra("number");
        movieTitle = intent.getStringExtra("title");
        chosenSeats = intent.getIntExtra("number of seats", 0);

        new Task1(1).execute();
        new Task1(2).execute();

        setContentView(R.layout.activity_seatselection);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Select seats");
        bookTicketsBttn = findViewById(R.id.bookTicketsBttn);
    }

    @Override
    protected void onStart() {
        super.onStart();

        groupLayout = findViewById(R.id.layoutSeat);
        seats = "/" + seats;

        LinearLayout layoutSeat = new LinearLayout(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutSeat.setOrientation(LinearLayout.VERTICAL);
        layoutSeat.setLayoutParams(params);
        layoutSeat.setPadding(5 * seatGaping, 5 * seatGaping, 5 * seatGaping, 5 * seatGaping);
        groupLayout.addView(layoutSeat);

        LinearLayout layout = null;

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        buildSeats(reservedSeats);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        /*For loop to build the seats groupView from the seats string which contains how the view of the seats
        * looks like, and set the margins, the id's, colors, etc..*/
        for (int index = 0; index < seats.length(); index++) {
            if (seats.charAt(index) == '/') {
                layout = new LinearLayout(this);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layoutSeat.addView(layout);
            } else if (seats.charAt(index) == 'U') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_booked);
                view.setTextColor(Color.WHITE);
                view.setTag(STATUS_BOOKED);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == 'A') {
                count++;
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setPadding(0, 0, 0, 2 * seatGaping);
                view.setId(count);
                view.setGravity(Gravity.CENTER);
                view.setBackgroundResource(R.drawable.ic_seats_book);
                view.setText(count + "");
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9);
                view.setTextColor(Color.BLACK);
                view.setTag(STATUS_AVAILABLE);
                layout.addView(view);
                seatViewList.add(view);
                view.setOnClickListener(this);
            } else if (seats.charAt(index) == '_') {
                TextView view = new TextView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(seatSize, seatSize);
                layoutParams.setMargins(seatGaping, seatGaping, seatGaping, seatGaping);
                view.setLayoutParams(layoutParams);
                view.setBackgroundColor(Color.TRANSPARENT);
                view.setText("");
                layout.addView(view);
            }
        }

        bookTicketsBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(chosenSeats < 0){
                    Toast.makeText(seatSelectionActivity.this,"More seats have been selected!!", Toast.LENGTH_LONG).show();
                }
                else if(chosenSeats > 0){
                    Toast.makeText(seatSelectionActivity.this,"Fewer seats have been selected!!", Toast.LENGTH_LONG).show();
                }
                else{
                    String[] seatsToBook = selectedIds.split(",");
                    new Task1(3, seatsToBook).execute();

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    if(bookingError.isEmpty()) {
                        /*A thread for sending a message to prevent running the sendMessage() method from the main thread.
                         * Also for displaying a message after a successful booking.*/
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                                        sendMessage();
                                        try {
                                            Thread.sleep(5000);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }

                                        alertDialog.dismiss();

                                        try {
                                            Thread.sleep(100);
                                        } catch (InterruptedException e) {
                                            throw new RuntimeException(e);
                                        }

                                        /*After booking the tickets and displaying the message
                                         * We finish this activity and back to the main activity.*/
                                        Intent intent = new Intent(seatSelectionActivity.this, IntroActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
                                    }
                                }
                            }
                        });
                        thread.start();
                    }
                    else{

                        /*After failing to book the tickets and displaying the error message
                         * We finish this activity and back to the main activity.*/
                        Intent intent = new Intent(seatSelectionActivity.this, IntroActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }

    /*---------------------------------------------------------------------------------------------------------------*/
    /*This method is called when a seat from the seat viewGroup is clicked.
    * It will display a message when a booked seat is clicked.
    * If available seat is clicked, it will add it to the selectedIds String and change its color to selected seat color.
    * If selected seat is clicked, it will remove it from the selectedIds String and change its color to empty seat.*/
    @Override
    public void onClick(View view) {
        if ((int) view.getTag() == STATUS_AVAILABLE) {
            if (selectedIds.contains(view.getId() + ",")) {
                selectedIds = selectedIds.replace(+view.getId() + ",", "");
                view.setBackgroundResource(R.drawable.ic_seats_book);
                chosenSeats++;
            } else {
                selectedIds = selectedIds + view.getId() + ",";
                view.setBackgroundResource(R.drawable.ic_seats_selected);
                chosenSeats--;
            }
        } else if ((int) view.getTag() == STATUS_BOOKED) {
            Toast.makeText(this, "Seat " + view.getId() + " is Booked", Toast.LENGTH_SHORT).show();
        }
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

    /*This method iterates on seats String, for each seat it checks if the seat is on reservedSeat list or not,
    * if yes we define the seat as a booked seat, otherwise we define the seat as a available seat.*/
    public void buildSeats(List<Integer> tmp) {
        int fac = audiID == 1 ? 0 : (audiID-1)*60;
        int cnt = 0;
        for(int i = 0; i < seats.length(); i++){
            if(seats.charAt(i) == 'U' || seats.charAt(i) == 'A'){
                cnt++;
                if(tmp.contains(cnt+fac)){
                    seats = seats.substring(0, i) + 'U' + seats.substring(i+1);
                }
                else{
                    seats = seats.substring(0, i) + 'A' + seats.substring(i+1);
                }
            }
        }
    }

    /*This procedure sends a message to the costumer after a successful booking.
    * And displays a message after successful booking and sending message.*/
    public void sendMessage() {
        String error = "";
        String message = "Dear " + costumerName + ",\nWe have booked your seats!." +
        " Please arrive before the screening time in" +
        " order to pay and issue the tickets.\nBest regards, EA6Cinema.";

        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(costumerPhone, null, message, null, null);
        } catch (Exception e) {
            Toast.makeText(seatSelectionActivity.this, "There is an error" +
            " messaging you, Please try again later!", Toast.LENGTH_LONG).show();
            error = e.toString();
            e.printStackTrace();
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (error.isEmpty()) {
            Success = true;
            seatSelectionActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    alertDialogBuilder = new AlertDialog.Builder(seatSelectionActivity.this);
                    alertDialogBuilder.setMessage("Hi " + costumerName + ".\nThank you for booking a movie tickets" +
                    ". We look forward to seeing you and your group!:)");
                    alertDialogBuilder.setTitle("Order status");
                    alertDialogBuilder.setIcon(R.drawable.lol);
                    alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });
        }
    }

    /*This inner class have three modes:
    * mode == 1, To retrieve the appropriate screening id.
    * mode == 2, To retrieve the reserved seats of the specific screening id.
    * mode == 3, To update the reservation and seats_reserved tables after a successful booking.*/
    class Task1 extends AsyncTask<Void, Void, Void> {

        String error = "";
        String[] seatsToBook;
        int mode = 0;

        public Task1(int mode) {
            this.mode = mode;
        }

        public Task1(int mode, String[] seatsToBook) {
            this.mode = mode;
            this.seatsToBook = seatsToBook;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if(mode == 1){
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ea6cinema_db", "andro", "andro");
                    Statement statement1 = conn.createStatement();

                    ResultSet resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`screening` " +
                            "where movie_id = '"+movieID+"' AND audit_id =" +
                            " '"+audiID+"' AND screening_time = '"+time+"'");

                    if(resultSet.next()){
                        screening_id = resultSet.getInt("id");
                    }
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

                    ResultSet resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`seat_reserved` " +
                            "where screening_id = '"+screening_id+"' ");

                    while(resultSet.next()){
                        reservedSeats.add(resultSet.getInt("seat_id"));
                    }

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
                    Statement statement2 = conn.createStatement();
                    ResultSet resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`reservation`");

                    int resID = 1;
                    while(resultSet.next()){
                        resID++;
                    }
                    statement2.executeUpdate("INSERT INTO `ea6cinema_db`.`reservation` " +
                            "(`id`, `screening_id`, `name`, `email`, `phone`) VALUES " +
                            "('" + resID + "','" + screening_id + "','" + costumerName + "','"
                            + costumerEmail + "', '" + costumerPhone + "')");


                    resultSet = statement1.executeQuery("SELECT * FROM `ea6cinema_db`.`seat_reserved`");
                    int id = 1;
                    while(resultSet.next()){
                        id++;
                    }
                    for(int i = 0; i < seatsToBook.length; i++){
                        int seat = Integer.parseInt(seatsToBook[i]);
                        seat = seat + ((audiID-1)*60);
                        statement2.executeUpdate("INSERT INTO `ea6cinema_db`.`seat_reserved` " +
                                "(`id`, `seat_id`, `reservation_id`, `screening_id`) VALUES " +
                                "('" + id + "','" + seat + "','" + resID + "','" + screening_id + "')");
                        id++;
                    }
                    statement1.close();
                    conn.close();
                }
                catch(Exception e) {
                    error = e.toString();
                    seatSelectionActivity.bookingError = error;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if(error != "") {
                Toast.makeText(getApplicationContext(),
                "There is error with the booking, please try again later!", Toast.LENGTH_LONG).show();
            }
            super.onPostExecute(aVoid);
        }
    }
}
