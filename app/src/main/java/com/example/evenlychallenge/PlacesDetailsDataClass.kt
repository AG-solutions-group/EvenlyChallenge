package com.example.evenlychallenge

data class PlacesDetailsDataClass(
    val meta: MetaDetails,
    val response: ResponseDetails
)

data class MetaDetails (
    val code: Long,
    val requestId: String
)

data class ResponseDetails (
    val venue: Venue
)

data class Venue (
    val id: String,
    val name: String,
    val contact: Contact,
    val location: Location,
    val canonicalUrl: String,
    val categories: List<Category>,
    val verified: Boolean,
    val stats: Stats,
    val url: String,
    val likes: Likes,
    val rating: Double,
    val ratingColor: String,
    val ratingSignals: Long,
    val beenHere: BeenHere,
    val photos: Listed,
    val description: String,
    val storeId: String,
    val page: Page,
    val hereNow: HereNow,
    val createdAt: Long,
    val tips: Listed,
    val shortUrl: String,
    val timeZone: String,
    val listed: Listed,
    val phrases: List<Phrase>,
    val hours: Hours,
    val popular: Hours,
    val pageUpdates: Inbox,
    val inbox: Inbox,
    val venueChains: List<Any?>,
    val attributes: Attributes,
    val bestPhoto: Photo
)

data class Attributes (
    val groups: List<Group>
)

data class HereNow (
    val count: Long,
    val groups: List<Group>,
    val summary: String
)

data class GroupItem (
    val displayName: String? = null,
    val displayValue: String? = null,
    val id: String? = null,
    val name: String? = null,
    val description: String? = null,
    val type: String? = null,
    val user: User? = null,
    val editable: Boolean? = null,
    val public: Boolean? = null,
    val collaborative: Boolean? = null,
    val url: String? = null,
    val canonicalUrl: String? = null,
    val createdAt: Long? = null,
    val updatedAt: Long? = null,
    val photo: Photo? = null,
    val followers: Followers? = null,
    val listItems: Inbox? = null,
    val source: Source? = null,
    val prefix: String? = null,
    val suffix: String? = null,
    val width: Long? = null,
    val height: Long? = null,
    val visibility: String? = null,
    val text: String? = null,
    val photourl: String? = null,
    val lang: String? = null,
    val likes: HereNow? = null,
    val logView: Boolean? = null,
    val agreeCount: Long? = null,
    val disagreeCount: Long? = null,
    val todo: Followers? = null
)

data class Group (
    val type: String,
    val name: String? = null,
    val summary: String? = null,
    val count: Long,
    val items: List<GroupItem>
)

data class Followers (
    val count: Long
)

data class Inbox (
    val count: Long,
    val items: List<InboxItem>
)

data class InboxItem (
    val id: String? = null,
    val createdAt: Long? = null,
    val photo: Photo? = null,
    val url: String? = null
)

data class Photo (
    val id: String,
    val createdAt: Long,
    val source: Source? = null,
    val prefix: String,
    val suffix: String,
    val width: Long,
    val height: Long,
    val visibility: String,
    val user: User? = null
)

data class Source (
    val name: String,
    val url: String
)

data class User (
    val firstName: String,
    val lastName: String,
    val countryCode: String
)

data class BeenHere (
    val count: Long,
    val unconfirmedCount: Long,
    val marked: Boolean,
    val lastCheckinExpiredAt: Long
)

data class Category (
    val id: String,
    val name: String,
    val pluralName: String,
    val shortName: String,
    val icon: Icon,
    val primary: Boolean
)

data class Icon (
    val prefix: String,
    val suffix: String
)

data class Contact (
    val phone: String,
    val formattedPhone: String,
    val twitter: String,
    val instagram: String,
    val facebook: String,
    val facebookUsername: String,
    val facebookName: String
)

data class Hours (
    val status: String,
    val isOpen: Boolean,
    val isLocalHoliday: Boolean,
    val timeframes: List<Timeframe>
)

data class Timeframe (
    val days: String,
    val includesToday: Boolean? = null,
    val open: List<Open>,
    val segments: List<Any?>
)

data class Open (
    val renderedTime: String
)

data class Likes (
    val count: Long,
    val summary: String
)

data class Listed (
    val count: Long,
    val groups: List<Group>
)

data class Location (
    val address: String,
    val crossStreet: String,
    val lat: Double,
    val lng: Double,
    val postalCode: String,
    val cc: String,
    val city: String,
    val state: String,
    val country: String,
    val formattedAddress: List<String>
)

data class Page (
    val pageInfo: PageInfo,
    val user: User
)

data class PageInfo (
    val description: String,
    val banner: String,
    val links: Inbox
)

data class Phrase (
    val phrase: String,
    val sample: Sample,
    val count: Long
)

data class Sample (
    val entities: List<Entity>,
    val text: String
)

data class Entity (
    val indices: List<Long>,
    val type: String
)

data class Stats (
    val checkinsCount: Long,
    val usersCount: Long,
    val tipCount: Long,
    val visitsCount: Long
)
