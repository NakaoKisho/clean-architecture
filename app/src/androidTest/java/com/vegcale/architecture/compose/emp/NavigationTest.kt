package com.vegcale.architecture.compose.emp

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.vegcale.architecture.HiltComponentActivity
import com.vegcale.architecture.navigation.EmpNavHost
import com.vegcale.architecture.ui.EarthquakeMapApp
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

//@HiltAndroidTest
class NavigationTest {
//    TODO: Write test codes
//    @get:Rule(order = 0)
//    var hiltRule = HiltAndroidRule(this)
//
//    @get:Rule(order = 1)
//    val composeTestRule = createAndroidComposeRule<HiltComponentActivity>()
//    val composeTestRule = createComposeRule()
//    private lateinit var navController: TestNavHostController

//    @Before
//    fun setup() {
//        hiltRule.inject()
//    }

//    @Test
//    fun showsNavigationTopBar() {
//        composeTestRule.setContent {
//            EarthquakeMapApp()
//        }
//
//        composeTestRule.onNodeWithTag("EmpTopAppBar").assertIsDisplayed()
//    }
//    @Test
//    fun empNavHost_verifyMapStartDestination() {
//        composeTestRule.setContent {
//            // Creates a TestNavHostController
//            navController =
//                TestNavHostController(LocalContext.current)
//            // Sets a ComposeNavigator to the navController so it can navigate through composables
//            navController.navigatorProvider.addNavigator(
//                ComposeNavigator()
//            )
//            EmpNavHost(navController = navController)
//        }
//
//        composeTestRule
//            .onNodeWithContentDescription("Map Screen")
//            .assertIsDisplayed()
//    }
}