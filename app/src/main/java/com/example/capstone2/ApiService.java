package com.example.capstone2;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
public interface ApiService {
    @GET("api/users/email")
    Call<List<String>> getEmails();
}
