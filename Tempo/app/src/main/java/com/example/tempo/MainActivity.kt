package com.example.tempo

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.tempo.constantes.Const
import com.example.tempo.databinding.ActivityMainBinding
import com.example.tempo.model.Main
import com.example.tempo.services.Api
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val corOpacityComOpacidade = ContextCompat.getColor(this, R.color.opacity)


        binding.trocarTema.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked){
                binding.conteinerPrincipal.setBackgroundColor(Color.parseColor("#53C6D5"))
            }else{
                binding.conteinerPrincipal.setBackgroundColor(Color.parseColor("#53C6D5"))
            }
        }


        binding.btBuscar.setOnClickListener {
            val cidade = if( binding.editBuscarCidade.text.toString().isNotEmpty()) binding.editBuscarCidade.text.toString() else "São Paulo"

            binding.preogressBar.visibility = View.VISIBLE

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .build()
                .create(Api::class.java)

            retrofit.weatherMap(cidade, Const.API_KEY).enqueue(object : Callback<Main>{
                override fun onResponse(call: Call<Main>, response: Response<Main>) {
                    if (response.isSuccessful){
                        respostaServidor(response)
                    }else{
                        Toast.makeText(applicationContext, "Digite novamente!", Toast.LENGTH_SHORT).show()
                        binding.preogressBar.visibility = View.GONE

                    }
                }

                override fun onFailure(p0: Call<Main>, p1: Throwable) {
                    Toast.makeText(applicationContext, "Erro no Servidor", Toast.LENGTH_SHORT).show()
                    binding.preogressBar.visibility = View.GONE

                }

            })
                }

        }

    override fun onResume() {
        super.onResume()

    }
    @SuppressLint("SetTextI18n")
    private fun respostaServidor(response: Response<Main>){
        val main = response.body()!!.main

        val temp     = main.get("temp").toString()
        val tempMin  = main.get("temp_min").toString()
        val tempMax  = main.get("temp_max").toString()
        val humidity = main.get("humidity").toString()

        val sys = response.body()!!.sys
        val country = sys.get("country").asString
        var pais = sys.get("country").asString

        val weather = response.body()!!.weather
        val main_weather  = weather[0].main
        val description   = weather[0].description

        val name = response.body()!!.name

        val K_C = 273.15

        val temp_c = (temp.toDouble() - K_C)
        val tempMin_c = (tempMin.toDouble() - K_C)
        val tempMax_c = (tempMax.toDouble() - K_C)
        val decimalFormat = DecimalFormat("00")

        if (country.equals("BR")){
            pais = "Brasil"
        }else if (country.equals("US")){
            pais = "E.U.S"
        }
        if(main_weather.equals("Clouds") && description.equals("few clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.flewclouds)
        }else if(main_weather.equals("Clouds") && description.equals("scattered clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.clouds)
        }else if(main_weather.equals("Clouds") && description.equals("sbroken clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.brokenclouds)
        }else if(main_weather.equals("Clouds") && description.equals("overcast clouds")){
            binding.imgClima.setBackgroundResource(R.drawable.brokenclouds)
        }else if(main_weather.equals("Clear") && description.equals("clear sky")){
            binding.imgClima.setBackgroundResource(R.drawable.sol)
        }else if(main_weather.equals("Snow")){
            binding.imgClima.setBackgroundResource(R.drawable.snow)
        }else if(main_weather.equals("Rain")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(main_weather.equals("Drizzle")){
            binding.imgClima.setBackgroundResource(R.drawable.rain)
        }else if(main_weather.equals("Thunderstorm")){
            binding.imgClima.setBackgroundResource(R.drawable.trunderstorm)
        }

        val descricaoClima = when(description){
            "clear sky" -> {
                "Céu Limpo"
            }
            "few clouds" -> {
                "Poucas nuvens"
            }
            "scattered clouds" -> {
                "Nuvens dispersas"
            }
            else ->{
                "tratando"
            }

        }

        binding.txtTemperatura.setText("${decimalFormat.format(temp_c)} ºC")

        binding.txtPaisCidade.setText("$pais - $name")

        binding.txtTituloInfo01.setText("Clima \n $descricaoClima \n\n Umidade \n $humidity %")
        binding.txtTituloInfo02.setText("Temp. Min \n ${decimalFormat.format(tempMin_c)} ºC \n\n Temp. Max \n ${decimalFormat.format(tempMax_c)} ºC ")

        binding.preogressBar.visibility = View.GONE

    }
}