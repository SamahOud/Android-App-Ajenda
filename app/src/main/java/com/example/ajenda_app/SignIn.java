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

public class SignIn extends AppCompatActivity {
    private EditText usernameET, passwordET, repeatPassET;
    private TextView tvLogIn;
    private Button btnSignIn;
    private String user, pass, repass;
    // **************************************
    private DatabaseHelper databaseHelper;
    // **************************************
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        usernameET = (EditText) findViewById(R.id.etPersonName);
        passwordET = (EditText) findViewById(R.id.etPassword);
        repeatPassET = (EditText) findViewById(R.id.etConfirmPass);
        // **************************************
        tvLogIn = (TextView) findViewById(R.id.tvLogIn);
        // **************************************
        btnSignIn =(Button) findViewById(R.id.btnSignIn);
        // **************************************
        databaseHelper = new DatabaseHelper(this);
        // **************************************

        tvLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                overridePendingTransition(R.anim.right_slide_in, R.anim.left_slide_out);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
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
                repass = repeatPassET.getText().toString();

                if (user.equals("") || pass.equals("") || repass.equals("")) {
                    text.setText(R.string.please_fill_the_empty_fields);
                    lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
                }
                else {
                    if (pass.equals(repass)) {
                        Boolean checkuser = databaseHelper.checkUsername(user);
                        if (checkuser == false) {
                            Boolean insert = databaseHelper.insertData(user, pass);
                            if (insert == true) {
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("user:", user);
                                editor.apply();

                                text.setText(R.string.registered_successfully);
                                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();

                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                text.setText(R.string.registration_failed);
                                lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                                toast.setDuration(Toast.LENGTH_SHORT);
                                toast.setView(layout);
                                toast.show();
                            }
                        }
                        else {
                            text.setText(R.string.user_already_exists_please_try_another_username);
                            lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_teal_500));
                            toast.setDuration(Toast.LENGTH_SHORT);
                            toast.setView(layout);
                            toast.show();
                        }
                    }
                    else {
                        text.setText(R.string.password_not_matching);
                        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.md_blue_grey_500));
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                }
            }
        });
    }
}