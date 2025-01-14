package com.example.madcamp_wk3.network


import com.example.madcamp_wk3.util.Section
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
data class TokenRequest(val id_token: String)
data class NewsResponse(val sections: List<Section>)
data class LoginResponse(val email: String, val name: String, val picture: String?, val message: String)
interface ApiService {
    @GET("/news")  // Replace with the correct API endpoint if different
    fun getNews(): Call<NewsResponse>
    @POST("login/google")
    fun googleLogin(@Body token: TokenRequest): Call<LoginResponse>

}