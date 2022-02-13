package com.example.evenlychallenge

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.evenlychallenge.BuildConfig.CLIENT_ID
import com.example.evenlychallenge.BuildConfig.CLIENT_SECRET
import com.example.evenlychallenge.databinding.PlacesGridBinding
import com.squareup.picasso.Picasso
import okhttp3.Interceptor
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

class PlacesAdapter(
    private val listener: OnClickListener
) : RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder>() {

    // implement onClick
    inner class PlacesViewHolder(val binding: PlacesGridBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onClick(position)
            }
        }
    }
    interface OnClickListener {
        fun onClick(position: Int)
    }


    private val diffCallback = object : DiffUtil.ItemCallback<Venues>() {
        override fun areItemsTheSame(oldItem: Venues, newItem: Venues): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Venues, newItem: Venues): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var places: List<Venues>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount() = places.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlacesViewHolder {
        return PlacesViewHolder(PlacesGridBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun onBindViewHolder(holder: PlacesViewHolder, position: Int) {
        holder.binding.apply {
            val place = places[position]

            // add the API data to the recycler
            placeNameTV.text = place.name
            markerNumberTV.text = (position+1).toString()
            if (place.categories.isNotEmpty()) {
                Picasso.get()
                    .load(place.categories[0].icon.prefix + "64" + place.categories[0].icon.suffix)
                    .resize(50, 50)
                    .centerCrop()
                    .into(placeLogoIV)
            }
        }
    }
}

interface PlacesApiSearch {

    // place the GET request to foursquare to get the place overview
    @GET("/v2/venues/search")
    suspend fun getPlaces(
        @Query("query") query: String = "coffee",
        @Query("ll") latitudeLongitude : String = "52.500342,13.425170",
       // @Query("near") near : String = "Chicago",
        @Query("radius") radius : Int = 2000,
        @Query("limit") limit: Int = 25,
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET ,
        @Query("v") version: Int = 20150101
        ): Response <PlacesDataClass>

    // @GET("/v2/venues/search?query=coffee&ll=52.500342%2C13.425170&radius=5000&limit=5&client_id=1YTJGDPEFM0U5M3IPRIJAIGHISGHDT3IMKXIFJMYPWXB3ICQ&client_secret=VUIORTGVXQL2EGLKYJ10YRXJPDAIZSC3TXWFRQIYNGW10ZA3&v=20150101")
}

interface PlacesApiDetails {

    // place the GET request to foursquare to get the place details
    @GET("/v2/venues/{venueId}")
    suspend fun getPlacesDetails(
        @Path("venueId") venueId : String,
        @Query("client_id") clientId: String = CLIENT_ID,
        @Query("client_secret") clientSecret: String = CLIENT_SECRET,
        @Query("v") version: Int = 20150101
    ): Response <PlacesDetailsDataClass>
}

   // @GET("/v2/venues/5a187743ccad6b307315e6fe/?client_id=1YTJGDPEFM0U5M3IPRIJAIGHISGHDT3IMKXIFJMYPWXB3ICQ&client_secret=VUIORTGVXQL2EGLKYJ10YRXJPDAIZSC3TXWFRQIYNGW10ZA3&v=20150101")

class AuthenticationInterceptor : Interceptor {

    // add the header to the request

    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {

        val newRequest = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .build()

        return chain.proceed(newRequest)
    }
}
