package tasos.grigoris.sharemyrecipe

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit


class RetrofitClient{

    companion object {

        var base_url = "https://simplebudget.eu/recipes/"

        fun createClient(): Retrofit {

            return Retrofit.Builder()
             //   .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .build()

        }

        fun createScalarClient(): Retrofit {

            return Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
               // .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(base_url)
                .client(getOKHTTP())
                .build()

        }

        private fun getOKHTTP() : OkHttpClient{

            return OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

        }

    }



}