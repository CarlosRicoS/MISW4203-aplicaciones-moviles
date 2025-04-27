package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import co.edu.uniandes.miso.vinilos.BuildConfig

class VinylsServiceAdapter {

    private val baseUrl = BuildConfig.API_HOST

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