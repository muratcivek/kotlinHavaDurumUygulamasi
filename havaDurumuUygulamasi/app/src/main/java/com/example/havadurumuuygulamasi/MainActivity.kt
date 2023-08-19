package com.example.havadurumuuygulamasi

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.PopupMenu
import androidx.core.net.toUri
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.example.havadurumuuygulamasi.databinding.ActivityMainBinding
import com.example.havadurumuuygulamasi.havaDurumuModel.havaDurumModel
import com.example.havadurumuuygulamasi.retrofitServis.apiUtils
import com.example.havadurumuuygulamasi.retrofitServis.retrofitClient
import com.example.havadurumuuygulamasi.sehirModel.sehirModel
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback

class MainActivity : AppCompatActivity() {
    private lateinit var tasarim:ActivityMainBinding
    private lateinit var spinnerAdaptor:ArrayAdapter<String>
    val iller = arrayOf(
        "Adana", "Adiyaman", "Afyonkarahisar", "Agri", "Amasya", "Ankara", "Antalya", "Artvin",
        "Aydin", "Balikesir", "Bilecik", "Bingol", "Bitlis", "Bolu", "Burdur", "Bursa",
        "Canakkale", "Cankiri", "Corum", "Denizli", "Diyarbakir", "Edirne", "Elazig", "Erzincan",
        "Erzurum", "Eskisehir", "Gaziantep", "Giresun", "Gumushane", "Hakkari", "Hatay", "Isparta",
        "Mersin", "Istanbul", "Izmir", "Kars", "Kastamonu", "Kayseri", "Kirklareli", "Kirsehir",
        "Kocaeli", "Konya", "Kutahya", "Malatya", "Manisa", "Kahramanmaras", "Mardin", "Mugla",
        "Mus", "Nevsehir", "Nigde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt",
        "Sinop", "Sivas", "Tekirdag", "Tokat", "Trabzon", "Tunceli", "Sanliurfa", "Usak",
        "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kirikkale", "Batman",
        "Sirnak", "Bartin", "Ardahan", "Igdir", "Yalova", "Karabuk", "Kilis", "Osmaniye","Duzce"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)
        spinnerYukle()
        sehirBilgiAl()



    }

    private fun havaDurumunuAl(lat:Double, lon:Double) {
        val apiCagri = apiUtils.getretrofitInterface()
        apiCagri.havaDurumuGetir(lat,lon,"6cd9ef1de75c5cba9b0b003ba6f8a3ba","tr")
            .enqueue(object : Callback<havaDurumModel>{
                override fun onResponse(
                    call: Call<havaDurumModel>,
                    response: retrofit2.Response<havaDurumModel>
                ) {
                    val data : havaDurumModel? =response.body()
                    if(data!=null){
                        //hava durumu icon alma
                        var iconId= data.weather.get(0).icon
                        var fotoUrl ="https://openweathermap.org/img/w/$iconId.png"
                        Picasso.get().load(fotoUrl).into(tasarim.havaDurumIcon)
                        //hava durumu sıcaklık bilgisini alıp dereceye dönüştürme
                        var sicaklik = data.main.temp.toInt()
                        Log.e(TAG, "basari: $sicaklik", )
                        var sicaklikGuncel = sicaklik - 273.15
                        tasarim.sicaklik.setText(sicaklikGuncel.toInt().toString() +"°C")
                        //hava durumu durum tanımlamasını alma
                        var tanim = data.weather.get(0).description
                        //apiden gelen kelimenin hepsi küçük harf, ilk harfini büyük yapıyoru
                        var tanimilkHarf= tanim[0].uppercase()
                        tasarim.tanim.setText(tanimilkHarf + tanim.substring(1))




                    }
                }

                override fun onFailure(call: Call<havaDurumModel>, t: Throwable) {

                }
            })
    }

    private fun sehirBilgiAl() {
        tasarim.bolgeSec.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                Log.e(TAG, "sehirBilgiAl: ${iller.get(p2)}")

                val apiCagri = apiUtils.getretrofitInterface()
                apiCagri.sehirGetir(iller.get(p2).toString(),"6cd9ef1de75c5cba9b0b003ba6f8a3ba")
                    .enqueue(object : Callback<sehirModel>{
                        override fun onResponse(
                            call: Call<sehirModel>,
                            response: retrofit2.Response<sehirModel>
                        ) {
                            val data : sehirModel? =response.body()
                            if(data!=null){
                                //lat bilgisi: data.get(0).lat
                                //lon bilgisi: data.get(0).lon
                                tasarim.sicaklik.setText("yükleniyor...")
                                tasarim.tanim.setText("yükleniyor...")


                                havaDurumunuAl(data.get(0).lat,data.get(0).lon)
                            }
                        }

                        override fun onFailure(call: Call<sehirModel>, t: Throwable) {

                        }
                    })

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }
        var secilenId=tasarim.bolgeSec.selectedItemPosition.toString()
        Log.e(TAG, "sehirBilgiAl: $secilenId")
    }

    private fun spinnerYukle() {
        spinnerAdaptor = ArrayAdapter(this,android.R.layout.simple_list_item_1,android.R.id.text1,iller)
        tasarim.bolgeSec.adapter = spinnerAdaptor

    }





}