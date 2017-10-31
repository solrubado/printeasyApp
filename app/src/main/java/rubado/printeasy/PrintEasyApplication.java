package rubado.printeasy;

import android.app.Application;
import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rubado.printeasy.services.PlatformAPI;

/**
 * Created by Sol Rubado on 10/04/2017.
 */

public class PrintEasyApplication extends Application {

    private PlatformAPI mPlatformAPI;


    public PlatformAPI getAPI() {

        if (mPlatformAPI == null) {
            OkHttpClient httpClient = new OkHttpClient();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.0.225:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
            mPlatformAPI = retrofit.create(PlatformAPI.class);
        }

        return mPlatformAPI;
    }
}
