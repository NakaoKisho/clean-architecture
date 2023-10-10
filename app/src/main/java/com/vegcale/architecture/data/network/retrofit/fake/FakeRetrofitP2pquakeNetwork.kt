package com.vegcale.architecture.data.network.retrofit.fake

import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.network.P2pquakeApi

class FakeRetrofitP2pquakeNetwork(
    private val p2pquakeInfoList: MutableList<P2pquakeInfo> = mutableListOf()
): P2pquakeApi {
    override suspend fun getInfo(limit: Int, offset: Int): List<P2pquakeInfo> {
        return p2pquakeInfoList
    }

    fun addEarthquakeInfo(item: P2pquakeInfo) {
        p2pquakeInfoList.add(item)
    }
}