package co.edu.uniandes.miso.vinilos.model.data.rest.serviceadapter

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

class VinylsServiceAdapter {
//    TODO: Change this URL when backend is deployed in the cloud
    private val baseUrl = "https://backvynils-q6yc.onrender.com/"

    // Create a trust manager that does not validate certificate chains
    private val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
        override fun checkClientTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun checkServerTrusted(chain: Array<out X509Certificate>?, authType: String?) {}
        override fun getAcceptedIssuers(): Array<X509Certificate> = arrayOf()
    })

    // Install the all-trusting trust manager
    private val sslContext = SSLContext.getInstance("SSL").apply {
        init(null, trustAllCerts, SecureRandom())
    }

    // Create an ssl socket factory with our all-trusting manager
    private val okHttpClient = OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustAllCerts[0] as X509TrustManager)
        .hostnameVerifier { _, _ -> true }
        .build()

    private val retrofitClient: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .build()

    internal val vinylsService: VinylsApiService by lazy {
        retrofitClient.create(VinylsApiService::class.java)
    }

    companion object {
        val instance = VinylsServiceAdapter()
    }
}