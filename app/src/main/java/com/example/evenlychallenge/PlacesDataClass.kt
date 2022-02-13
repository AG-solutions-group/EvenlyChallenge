package com.example.evenlychallenge


data class PlacesDataClass(
    val meta: MetaOverview,
    val response: ResponseOverview
)

data class MetaOverview(
    val code: Int,
    val requestId: String
)

data class ResponseOverview(
    val venues: List<Venues>
)

data class Venues(
val categories: List<CategoryOverview>,
val id: String,
val location: LocationOverview,
val name: String,
val venuePage: VenuePage
)

data class CategoryOverview(
    val icon: IconOverview,
    val id: String,
    val name: String,
    val pluralName: String,
    val primary: Boolean,
    val shortName: String
)

data class IconOverview(
    val prefix: String,
    val suffix: String
)

data class LabeledLatLng(
    val label: String,
    val lat: Double,
    val lng: Double
)

data class LocationOverview(
    val address: String,
    val cc: String,
    val city: String,
    val country: String,
    val crossStreet: String,
    val distance: Int,
    val formattedAddress: List<String>,
    val labeledLatLngs: List<LabeledLatLng>,
    val lat: Double,
    val lng: Double,
    val postalCode: String,
    val state: String
)

data class VenuePage(
    val id: String
)