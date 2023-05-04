package com.example.ajenda_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DatabaseHelper.DatabaseHelper;

public class MainActivity extends AppCompatActivity {
    private EditText usernameET, passwordET;
    private TextView tvSignIn, tvForgot;
    private Button btnLogIn;
    private String user, pass;
    // **************************************
    private DatabaseHelper databaseHelper;
    // **************************************
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usernameET = (EditText) findViewById(R.id.etPersonName);
        passwordET = (EditText) findViewById(R.id.etPassword);
        // **************************************
        tvSignIn = (TextView) findViewById(R.id.tvSignIn);
        tvForgot = (TextView) findViewById(R.id.tvForgot);
        // **************************************
        btnLogIn = (Button) findViewById(R.id.btnLogIn);
        // **************************************
        databaseHelper = new DatabaseHelper(this);

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View layout = getLayoutInflater().inflate(R.layout.custom_toast,
                        (ViewGroup) findViewById(R.id.custom_toast_layout_id));
                TextView text = (TextView) layout.findViewById(R.id.text);
                CardView lyt_card = (CardView) layout.findViewById(R.id.lyt_card);
                final Toast toast = new Toast(getApplicationContext());

                sharedPreferences = getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                user = usernameET.getText().toString();
                pass = passwordET.getText().toString();

                if (user.equals("") || pass.equals("")) {
                    text.setText(R.string.please_fill_the_empty_fields);
                    lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else  {
                    Boolean checkuserpass = databaseHelper.checkUsernamePassword(user, pass);
                    if (checkuserpass == true) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("user:", user);
                        editor.apply();

                        text.setText(R.string.sign_in_successful);
                        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();

                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        startActivity(intent);
                    }
                    else  {
                        text.setText(R.string.invalid_credential);
                        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PasswordActivity.class));
                overridePendingTransition(R.anim.left_slide_in, R.anim.right_slide_out);
            }
        });
    }

}