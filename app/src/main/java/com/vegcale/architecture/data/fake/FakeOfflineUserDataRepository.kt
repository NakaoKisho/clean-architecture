package com.vegcale.architecture.data.fake

import com.vegcale.architecture.data.UserDataRepository
import com.vegcale.architecture.data.model.UserData
import kotlinx.coroutines.flow.flow

class FakeOfflineUserDataRepository(
    private var userdata: UserData = UserData(
        isNotificationOn = false,
        places = listOf(),
        minIntensityLevelIndex = 0
    ),
    private var datetime: String = "2023/10/09 08:23:00",
    private var latitude: Double = 0.0,
    private var longitude: Double = 0.0,
    private var magnitude: Double = 0.0,
): UserDataRepository {
    override val userData = flow { emit(userdata) }

    override suspend fun setIsNotificationOn(isOn: Boolean) {
        userdata = userdata.copy(isNotificationOn = isOn)
    }

    override suspend fun addPlace(place: String) {
        val previousList = userdata.places
        val newList = previousList.toMutableList()
        newList.add(place)

        userdata = userdata.copy(places = newList)
    }

    override suspend fun clearPlaces() {
        userdata = userdata.copy(places = listOf())
    }

    override suspend fun deletePlace(place: String) {
        val previousList = userdata.places
        val newList = previousList.toMutableList()
        newList.remove(place)

        userdata = userdata.copy(places = newList)
    }

    override suspend fun addPlaces(places: List<String>) {
        val newList =  userdata.places + places

        userdata = userdata.copy(places = newList)
    }

    override suspend fun setMinIntensityLevelIndex(index: Int) {
        userdata = userdata.copy(minIntensityLevelIndex = index)
    }

    override val latestEarthquakeDatetime = flow { emit(datetime) }
    override val latestEarthquakeLatitude = flow { emit(latitude) }
    override val latestEarthquakeLongitude = flow { emit(longitude) }
    override val latestEarthquakeMagnitude = flow { emit(magnitude) }

    override suspend fun setLatestEarthquakeDatetime(datetime: String) {
        this.datetime = datetime
    }

    override suspend fun setLatestEarthquakeLatitude(latitude: Double) {
        this.latitude = latitude
    }

    override suspend fun setLatestEarthquakeLongitude(longitude: Double) {
        this.longitude = longitude
    }

    override suspend fun setLatestEarthquakeMagnitude(magnitude: Double) {
        this.magnitude = magnitude
    }
}