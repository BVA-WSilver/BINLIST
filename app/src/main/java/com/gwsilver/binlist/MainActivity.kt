package com.gwsilver.binlist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.gwsilver.binlist.databinding.ActivityMainBinding
import org.json.JSONObject
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btOk.setOnClickListener {
            getResult()
        }
    }
    private fun getResult(){
        val binCard = binding.editTextNumber.text.toString()
        if (binCard.length > 5) {
            val url = "https://lookup.binlist.net/$binCard"
            Log.d("MyLog", url)
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                Request.Method.GET,
                url, { response ->
                    val obj = JSONObject(response)
                    val number = obj.getJSONObject("number")
                    val country = obj.getJSONObject("country")
                    val bank = obj.optJSONObject("bank")
                    binding.tvScheme.text = obj.optString("scheme").capitalize()
                    binding.tvBrand.text = obj.optString("brand")
                    binding.tvLength.text = number.optInt("length").toString()
                    binding.tvLuhn.text = if (number.optBoolean("luhn")) {
                        "Yes"
                    } else "No"

                    binding.tvType.text = obj.optString("type").capitalize()
                    binding.tvPrepaid.text = if (obj.optBoolean("prepaid")) {
                        "Yes"
                    } else "No"

                    binding.tvEmoji.text = country.optString("emoji")
                    binding.tvCountryName.text = country.optString("name")
                    binding.tvLatitude.text = country.optInt("latitude").toString()
                    binding.tvLongitude.text = country.optInt("longitude").toString()

                    if (bank != null) {
                        val bank_city = "${bank.optString("name")}, ${bank.optString("city")}"
                        binding.tvBank.text = bank_city

                        binding.tvUrl.text = bank.optString("url")
                        binding.tvPhone.text = bank.optString("phone")
                    } else {
                        binding.tvBank.text = "-"
                        binding.tvUrl.text = ""
                        binding.tvPhone.text = ""
                    }

                    Log.d("MyLog", "Json: $obj")

                }, {
                    error()
                    Log.d("MyLog", "Error: $it")
                })
            queue.add(stringRequest)
        } else error()
    }

    private fun error(){
        binding.tvScheme.text = "-"
        binding.tvBrand.text = "-"
        binding.tvLength.text = "-"
        binding.tvLuhn.text = "-"
        binding.tvType.text = "-"
        binding.tvPrepaid.text = "-"
        binding.tvEmoji.text = "-"
        binding.tvCountryName.text = ""
        binding.tvLatitude.text = " "
        binding.tvLongitude.text = " "
        binding.tvBank.text = "-"
        binding.tvUrl.text = ""
        binding.tvPhone.text = ""
    }
}