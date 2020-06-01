package com.example.fingerspeedgame;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


//checking git commit
public class MainActivity extends AppCompatActivity {

    private TextView timerTextView;
    private TextView aThousandTextView;
    private Button tapTapButton;

    private CountDownTimer mCountDownTimer;

    private long initialCountdowninMillis = 60000;
    private int timeInterval = 1000;
    private int remainingTime = 60;
    private int aThousand = 1000;

    private final String REMAIN_TIME_KEY = "remaining time key";
    private final String A_THOUSAND_KEY = " a thousand key";

    @Override
    protected void onDestroy() {
        super.onDestroy();
        showToast("Destroy", Toast.LENGTH_SHORT);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        showToast("OnSaveInstanceState", Toast.LENGTH_SHORT);

        outState.putInt(REMAIN_TIME_KEY, remainingTime);
        outState.putInt(A_THOUSAND_KEY, aThousand);
        mCountDownTimer.cancel();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showToast("On Create Methods is called", Toast.LENGTH_SHORT);

        timerTextView = findViewById(R.id.textTime);
        aThousandTextView = findViewById(R.id.txtTimer);
        tapTapButton = findViewById(R.id.button);
        aThousandTextView.setText(aThousand + "");

        if(savedInstanceState != null) {
            remainingTime = savedInstanceState.getInt(REMAIN_TIME_KEY);
            aThousand = savedInstanceState.getInt(A_THOUSAND_KEY);
            restoreTheGame();
        }

        tapTapButton.setOnClickListener(new View.OnClickListener() {
             @Override
            public void onClick(View v) {
                aThousand--;
                aThousandTextView.setText(aThousand + "");

                if (remainingTime > 0 && aThousand <= 0) {
                    Toast.makeText(MainActivity.this, "Congratulation! you are a monster", Toast.LENGTH_SHORT).show();
                    showAlert("Congratulation!", "Please reset the game");
                }
            }
        });
        if (savedInstanceState == null ) {
            mCountDownTimer = new CountDownTimer(initialCountdowninMillis, timeInterval) {
                @Override
                public void onTick(long millisUntilFinished) {
                    remainingTime = (int) millisUntilFinished / 1000;
                    timerTextView.setText(remainingTime + "");

                }

                @Override
                public void onFinish() {
                    Toast.makeText(MainActivity.this, "CTDN is finished", Toast.LENGTH_SHORT).show();
                    showAlert("Not intersting", "Would you like to try again?");

                }
            };
            mCountDownTimer.start();
        }
    }

//    protected void onResume() {
//        super.onResume();
//        showToast("Resume", Toast.LENGTH_SHORT);
//        mCountDownTimer.start();
//    }

    private void restoreTheGame() {
        int restoreRemainingTime = remainingTime;
        int restoredAThousand = aThousand;

        timerTextView.setText(restoreRemainingTime + "");
        aThousandTextView.setText(restoredAThousand + "");


        mCountDownTimer = new CountDownTimer((long)remainingTime * 1000, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int)millisUntilFinished / 1000;
                timerTextView.setText(remainingTime + "");
            }

            @Override
            public void onFinish() {
                showAlert("Finished", "Would you like to try again?");
            }
        };
        mCountDownTimer.start();
    }

    private void resetTheGame() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
        aThousand = 1000;
        aThousandTextView.setText(aThousand + "");
        timerTextView.setText(remainingTime + "");
        mCountDownTimer = new CountDownTimer(initialCountdowninMillis, timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTime = (int) millisUntilFinished / 1000;
                timerTextView.setText(remainingTime + "");
            }

            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this, "CTDN is finished", Toast.LENGTH_SHORT).show();
                showAlert("Not intersting", "Would you like to try again?");

            }
        };
        mCountDownTimer.start();
    }
    private void showAlert(String title, String message) {
        AlertDialog alertDialog =  new AlertDialog.Builder(MainActivity.this)
                .setTitle(title)
                .setMessage(message)
                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {                                    // Continue with delete operation
                        resetTheGame();
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .show();
        alertDialog.setCancelable(false);
    }
    private void showToast(String message, int duration) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.info_item) {
            showToast("This si the current version of your game. Check google play nd make sure that your're planying the latest game:" + BuildConfig.VERSION_NAME, Toast.LENGTH_SHORT);
        }
        return true;
    };
}