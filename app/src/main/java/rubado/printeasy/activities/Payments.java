package rubado.printeasy.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rubado.printeasy.Pojos.PaymentsPojo;
import rubado.printeasy.PrintEasyApplication;
import rubado.printeasy.R;
import rubado.printeasy.adapters.PaymentAdapter;

public class Payments extends AppCompatActivity {

    private PrintEasyApplication mApp;
    private ListView mPaymentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_printing_settings);

        mApp = (PrintEasyApplication) getApplication();

        mPaymentList = (ListView) findViewById(R.id.listView);

        getPayments();
    }


    public void getPayments() {
        Call<PaymentsPojo> userCall = mApp.getAPI().paymentCall();

        userCall.enqueue(new Callback<PaymentsPojo>() {
            @Override
            public void onResponse(Call<PaymentsPojo> call, Response<PaymentsPojo> response) {
                if (response.code() >= 400 && response.code() <= 500) {
                    Log.e("Login", response.code() + " " + response.message());
                    Payments.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Payments.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();

                        }
                    });
                } else if (response.isSuccessful()) {
                    PaymentsPojo payments = response.body();
                    PaymentAdapter sessionsAdapter = new PaymentAdapter(getApplicationContext(), getApplication(), payments.getPayments());
                    mPaymentList.setAdapter(sessionsAdapter);


                } else {
                    Log.e("LoginActivity", "Mensaje: " + response.code());
                    Payments.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(Payments.this, getString(R.string.user_error_message), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<PaymentsPojo> call, Throwable t) {
                Log.e("Login Activity", t.getMessage());

            }

        });
    }

}
