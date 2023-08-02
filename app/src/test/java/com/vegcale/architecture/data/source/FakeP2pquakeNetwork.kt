package com.vegcale.architecture.data.source

import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.network.P2pquakeApi

class FakeP2pquakeNetwork(private val earthquakeInfo: List<P2pquakeInfo> = listOf()): P2pquakeApi {
    override suspend fun getInfo(limit: Int, offset: Int): List<P2pquakeInfo> {
        return earthquakeInfo
    }
}