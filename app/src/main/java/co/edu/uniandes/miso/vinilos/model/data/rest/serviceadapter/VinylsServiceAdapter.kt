package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class VinylsServiceAdapter {
//    TODO: Change this URL when backend is deployed in the cloud
    private val baseUrl = "https://backvynils-q6yc.onrender.com/"

    private val retrofitClient: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    internal val vinylsService: VinylsApiService by lazy {
        retrofitClient.create(VinylsApiService::class.java)
    }

    companion object {
        val instance = VinylsServiceAdapter()
    }
}