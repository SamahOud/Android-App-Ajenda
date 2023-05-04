package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DatabaseHelper.DatabaseHelper;

public class PasswordActivity extends AppCompatActivity {
    private EditText username;
    private Button reset;
    // **************************************
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        username = (EditText) findViewById(R.id.etUsername_reset);
        // **************************************
        reset = (Button) findViewById(R.id.btnReset);
        // **************************************
        databaseHelper = new DatabaseHelper(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View layout = getLayoutInflater().inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                TextView text = (TextView) layout.findViewById(R.id.text);
                CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                final Toast toast = new Toast(getApplicationContext());

                String user = username.getText().toString();
                Boolean checkuser = databaseHelper.checkUsername(user);

                if (checkuser == true) {
                    Intent intent = new Intent(getApplicationContext(), ResetActivity.class);
                    intent.putExtra("username", user);
                    startActivity(intent);
                    overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
                }
                else {
                    text.setText(R.string.user_does_not_exists);
                    lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
            }
        });
    }
}