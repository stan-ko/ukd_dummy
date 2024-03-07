package ua.ukd.dummy.net;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {

    @GET("users?page=1") // Endpoint for retrieving users
    Call<UserResponse> getUsers(@Query("per_page") int perPage);

}
