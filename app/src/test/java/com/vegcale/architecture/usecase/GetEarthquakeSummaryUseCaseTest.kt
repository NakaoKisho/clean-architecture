package com.vegcale.architecture.usecase

import com.vegcale.architecture.data.P2pquakeRepository2
import com.vegcale.architecture.data.YahooGeocodeRepository2
import com.vegcale.architecture.data.fake.FakeOfflineUserDataRepository
import com.vegcale.architecture.data.model.P2pquakeInfo
import com.vegcale.architecture.data.model.P2pquakeInfoEarthquake
import com.vegcale.architecture.data.model.P2pquakeInfoHypocenter
import com.vegcale.architecture.data.model.P2pquakeInfoIssue
import com.vegcale.architecture.data.model.P2pquakeInfoPoint
import com.vegcale.architecture.data.network.retrofit.fake.FakeRetrofitP2pquakeNetwork
import com.vegcale.architecture.data.network.retrofit.fake.FakeRetrofitYahooGeocodeNetwork
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import java.text.SimpleDateFormat
import java.util.Date

class GetEarthquakeSummaryUseCaseTest {

    private lateinit var retrofitP2pquakeNetwork: FakeRetrofitP2pquakeNetwork
    private lateinit var p2pquakeRepository: P2pquakeRepository2
    private lateinit var yahooGeocodeRepository: YahooGeocodeRepository2
    private lateinit var fakeOfflineUserDataRepository: FakeOfflineUserDataRepository
    private lateinit var subject: GetEarthquakeSummaryUseCase
    private lateinit var placeNames: MutableList<String>
    private lateinit var p2pquakeInfoHasAllPoints: P2pquakeInfo

    @Before
    fun setup() {
        retrofitP2pquakeNetwork = FakeRetrofitP2pquakeNetwork()
        p2pquakeRepository = P2pquakeRepository2(network = retrofitP2pquakeNetwork)
        yahooGeocodeRepository = YahooGeocodeRepository2(network = FakeRetrofitYahooGeocodeNetwork())
        fakeOfflineUserDataRepository = FakeOfflineUserDataRepository()
        subject = GetEarthquakeSummaryUseCase(
            p2pquakeRepository = p2pquakeRepository,
            yahooGeocodeRepository = yahooGeocodeRepository,
            offlineUserDataRepository = fakeOfflineUserDataRepository,
        )

        placeNames = mutableListOf(
            "北海道",
            "青森県",
            "岩手県",
            "宮城県",
            "秋田県",
            "山形県",
            "福島県",
            "茨城県",
            "栃木県",
            "群馬県",
            "埼玉県",
            "千葉県",
            "東京都",
            "神奈川県",
            "新潟県",
            "富山県",
            "石川県",
            "福井県",
            "山梨県",
            "長野県",
            "岐阜県",
            "静岡県",
            "愛知県",
            "三重県",
            "滋賀県",
            "京都府",
            "大阪府",
            "兵庫県",
            "奈良県",
            "和歌山県",
            "鳥取県",
            "島根県",
            "岡山県",
            "広島県",
            "山口県",
            "徳島県",
            "香川県",
            "愛媛県",
            "高知県",
            "福岡県",
            "佐賀県",
            "長崎県",
            "熊本県",
            "大分県",
            "宮崎県",
            "鹿児島県",
            "沖縄県",
        )

        val currentDatetimeText = SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(Date())
        p2pquakeInfoHasAllPoints = P2pquakeInfo(
            id = "652339ee51d38bb17b2e873e",
            code = 551,
            time = currentDatetimeText,
            issue = P2pquakeInfoIssue(
                source = "気象庁",
                time = currentDatetimeText,
                type = "DetailScale",
                correct = "None",
            ),
            earthquake = P2pquakeInfoEarthquake(
                time = currentDatetimeText,
                hypocenter = P2pquakeInfoHypocenter(
                    name = "和歌山県北部",
                    latitude = 34.1,
                    longitude = 135.5,
                    depth = 70,
                    magnitude = 3.5
                ),
                maxScale = 20,
                domesticTsunami = "None",
                foreignTsunami = "UnKnown"
            ),
            points = listOf(
                P2pquakeInfoPoint(
                    pref = "北海道",
                    addr = "北海道",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "青森県",
                    addr = "青森県",
                    isArea = false,
                    scale = 20,
                ),
                P2pquakeInfoPoint(
                    pref = "岩手県",
                    addr = "岩手県",
                    isArea = false,
                    scale = 30,
                ),
                P2pquakeInfoPoint(
                    pref = "宮城県",
                    addr = "宮城県",
                    isArea = false,
                    scale = 40,
                ),
                P2pquakeInfoPoint(
                    pref = "秋田県",
                    addr = "秋田県",
                    isArea = false,
                    scale = 45,
                ),
                P2pquakeInfoPoint(
                    pref = "山形県",
                    addr = "山形県",
                    isArea = false,
                    scale = 46,
                ),
                P2pquakeInfoPoint(
                    pref = "福島県",
                    addr = "福島県",
                    isArea = false,
                    scale = 50,
                ),
                P2pquakeInfoPoint(
                    pref = "茨城県",
                    addr = "茨城県",
                    isArea = false,
                    scale = 55,
                ),
                P2pquakeInfoPoint(
                    pref = "栃木県",
                    addr = "栃木県",
                    isArea = false,
                    scale = 60,
                ),
                P2pquakeInfoPoint(
                    pref = "群馬県",
                    addr = "群馬県",
                    isArea = false,
                    scale = 70,
                ),
                P2pquakeInfoPoint(
                    pref = "埼玉県",
                    addr = "埼玉県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "千葉県",
                    addr = "千葉県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "東京都",
                    addr = "東京都",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "神奈川県",
                    addr = "神奈川県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "新潟県",
                    addr = "新潟県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "富山県",
                    addr = "富山県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "石川県",
                    addr = "石川県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "福井県",
                    addr = "福井県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "山梨県",
                    addr = "山梨県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "長野県",
                    addr = "長野県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "岐阜県",
                    addr = "岐阜県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "静岡県",
                    addr = "静岡県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "愛知県",
                    addr = "愛知県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "三重県",
                    addr = "三重県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "滋賀県",
                    addr = "滋賀県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "京都府",
                    addr = "京都府",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "大阪府",
                    addr = "大阪府",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "兵庫県",
                    addr = "兵庫県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "奈良県",
                    addr = "奈良県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "和歌山県",
                    addr = "和歌山県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "鳥取県",
                    addr = "鳥取県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "島根県",
                    addr = "島根県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "岡山県",
                    addr = "岡山県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "広島県",
                    addr = "広島県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "山口県",
                    addr = "山口県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "徳島県",
                    addr = "徳島県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "香川県",
                    addr = "香川県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "愛媛県",
                    addr = "愛媛県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "高知県",
                    addr = "高知県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "福岡県",
                    addr = "福岡県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "佐賀県",
                    addr = "佐賀県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "長崎県",
                    addr = "長崎県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "熊本県",
                    addr = "熊本県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "大分県",
                    addr = "大分県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "宮崎県",
                    addr = "宮崎県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "鹿児島県",
                    addr = "鹿児島県",
                    isArea = false,
                    scale = 10,
                ),
                P2pquakeInfoPoint(
                    pref = "沖縄県",
                    addr = "沖縄県",
                    isArea = false,
                    scale = 10,
                ),
            )
        )
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_all_values() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = 0,
            ).collect {
                assert(it[0].points.size == placeNames.size)
            }
        }
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_values_except_for_hokkaido() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        val hokkaido = "北海道"
        placeNames.remove(hokkaido)
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = 0,
            ).collect {
                val listHasHokkaido = it[0].points.map { pointDetail ->
                    pointDetail.place == hokkaido
                }
                val isAnyItemHasHokkaido = listHasHokkaido.any { result -> result }

                assert(
                    !isAnyItemHasHokkaido && (it[0].points.size == placeNames.size)
                )
            }
        }
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_values_except_for_hokkaido_and_okinawa() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        val hokkaido = "北海道"
        placeNames.remove(hokkaido)
        val okinawa = "沖縄"
        placeNames.remove(okinawa)
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = 0,
            ).collect {
                val listHasMultiplePlaces = it[0].points.map { pointDetail ->
                    pointDetail.place == hokkaido ||
                            pointDetail.place == okinawa
                }
                val isAnyItemHasTargetPlace = listHasMultiplePlaces.any { result -> result }

                assert(
                    !isAnyItemHasTargetPlace && (it[0].points.size == placeNames.size)
                )
            }
        }
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_values_has_intensity_level_upper_than_two() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        val minIntensityLevel: Byte = 20
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = minIntensityLevel,
            ).collect {
                val isAllItemUpperThanMinIntensityLevel = it[0].points.all { pointDetail ->
                    pointDetail.scale != null && pointDetail.scale!! >= minIntensityLevel
                }

                assert(
                    isAllItemUpperThanMinIntensityLevel && (it[0].points.size == 9)
                )
            }
        }
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_values_has_intensity_level_upper_than_seven() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        val minIntensityLevel: Byte = 70
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = minIntensityLevel,
            ).collect {
                val isAllItemUpperThanMinIntensityLevel = it[0].points.all { pointDetail ->
                    pointDetail.scale != null && pointDetail.scale!! >= minIntensityLevel
                }

                assert(
                    isAllItemUpperThanMinIntensityLevel && (it[0].points.size == 1)
                )
            }
        }
    }

    @Test
    fun getEarthquakeSummaryUseCaseTest_completed_return_values_has_intensity_level_upper_than_two_and_except_for_hokkaido_and_okinawa() {
        retrofitP2pquakeNetwork.addEarthquakeInfo(p2pquakeInfoHasAllPoints)
        val hokkaido = "北海道"
        placeNames.remove(hokkaido)
        val okinawa = "沖縄"
        placeNames.remove(okinawa)
        val minIntensityLevel: Byte = 20
        runTest {
            subject(
                placeNames = placeNames,
                minIntensityLevel = minIntensityLevel,
            ).collect {
                val isAllItemUpperThanMinIntensityLevel = it[0].points.all { pointDetail ->
                    pointDetail.scale != null && pointDetail.scale!! >= minIntensityLevel
                }
                val isAllItemHasNoRemovedPlaces = it[0].points.all { pointDetail ->
                    pointDetail.place != hokkaido &&
                            pointDetail.place != okinawa
                }

                assert(
                    isAllItemUpperThanMinIntensityLevel &&
                            isAllItemHasNoRemovedPlaces &&
                            (it[0].points.size == 9)
                )
            }
        }
    }
}