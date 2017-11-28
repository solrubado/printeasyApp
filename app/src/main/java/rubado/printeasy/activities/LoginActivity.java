package rubado.printeasy.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubado.printeasy.PrintEasyApplication;
import rubado.printeasy.R;

public class LoginActivity extends AppCompatActivity {
    private PrintEasyApplication mApp;
    private EditText userEmail;
    private EditText userPassword;
    private ProgressBar mProgressBar;
    private RelativeLayout mContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mApp = (PrintEasyApplication) getApplication();

        mProgressBar = (ProgressBar) findViewById(R.id.progress_login);
        mContainer = (RelativeLayout) findViewById(R.id.loginContainer);
        userEmail = (EditText) findViewById(R.id.usernameET);
        userPassword = (EditText) findViewById(R.id.passwordET);

        Button login = (Button) findViewById(R.id.loginInBtn);
        assert login != null;
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                if (!userEmail.getText().toString().equals("")) {
                    if (!userPassword.getText().toString().equals("")) {
                        // final ProgressBar loginProgress = (ProgressBar) findViewById(R.id.login_progress);
                        // loginProgress.setVisibility(View.VISIBLE);

                        Call<Void> loginCall = mApp.getAPI().loginUser(userEmail.getText().toString().trim(), userPassword.getText().toString().trim());
                        Log.e("Call",loginCall.request().body().toString());
                        loginCall.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.code() >= 400 && response.code() <= 500) {
                                    hideProgressBar();
                                    Log.e("Login", response.code() + " " + response.message());
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(LoginActivity.this, getString(R.string.login_error_message), Toast.LENGTH_LONG).show();

                                        }
                                    });

                                } else if (response.isSuccessful()) {
                                    if(userEmail.getText().toString().equals("123456")){
                                        Intent i = new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(i);
                                        finish();
                                    }else{
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                    finish();
                                    }
                                } else {
                                    hideProgressBar();
                                    Log.e("LoginActivity", "Mensaje: " + response.code());
                                    LoginActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Snackbar.make(findViewById(R.id.containerLogin), getString(R.string.login_error_message), Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                                }

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                hideProgressBar();
                                Log.e("Login Activity", t.getMessage());
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    public void run() {
                                        Snackbar.make(findViewById(R.id.containerLogin), getString(R.string.login_error_message), Snackbar.LENGTH_LONG)
                                                .show();
                                    }
                                });
                            }


                        });


                    } else {
                        Snackbar.make(findViewById(R.id.containerLogin), "Contraseña es un campo obligatorio", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    Snackbar.make(findViewById(R.id.containerLogin), "Usuario es un campo obligatorio", Snackbar.LENGTH_LONG).show();
                }

            }
        });



    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        mContainer.setVisibility(View.GONE);
    }

    private void hideProgressBar(){
        mProgressBar.setVisibility(View.GONE);
        mContainer.setVisibility(View.VISIBLE);
    }
}
