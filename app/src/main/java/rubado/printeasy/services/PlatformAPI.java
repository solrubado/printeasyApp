package rubado.printeasy.services;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rubado.printeasy.Pojos.DocumentsPojo;
import rubado.printeasy.Pojos.LoginPojo;
import rubado.printeasy.Pojos.PaymentPojo;
import rubado.printeasy.Pojos.PaymentsPojo;

/**
 * Created by Sol Rubado on 10/04/2017.
 */

public interface PlatformAPI {

    @FormUrlEncoded
    @POST("loginAndroid/")
    Call<Void> loginUser(@Field("username") String username, @Field("password") String password);

    @Headers("Content-Type: application/json")
    @GET("historyAndroid/")
    Call<DocumentsPojo> historyCall();

    @Headers("Content-Type: application/json")
    @GET("documentsAndroid/")
    Call<DocumentsPojo> documentsCall();

    @Multipart
    @POST("upload")
    Call<Void> upload(@Part MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @GET("paymentAndroid/")
    Call<PaymentsPojo> paymentCall();
}
