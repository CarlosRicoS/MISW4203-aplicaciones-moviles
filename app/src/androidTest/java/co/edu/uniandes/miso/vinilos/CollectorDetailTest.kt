package co.edu.uniandes.miso.vinilos

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isRoot
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import co.edu.uniandes.miso.vinilos.util.TestUtils
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CollectorDetailTest {
    
    private val timeToWait = 2000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun collectorDetailBasicInfoTest() {
        // Given
        navigateToCollectorsTab()
        
        // When
        onView(withId(R.id.collectorRv))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(isRoot()).perform(TestUtils.disableRecyclerViewAnimations())
        
        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.collector_photo))
            .check(matches(isDisplayed()))
              
        onView(withId(R.id.email))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.phone))
            .check(matches(isDisplayed()))

        pressBack()
        Thread.sleep(timeToWait)
    }
    
    @Test
    fun collectorDetailSectionsDisplayedTest() {
        // Given
        navigateToCollectorsTab()
        
        // When
        onView(withId(R.id.collectorRv))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))

        onView(isRoot()).perform(TestUtils.disableRecyclerViewAnimations())
        
        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.performersSection))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.collectionSection))
            .check(matches(isDisplayed()))
        
        pressBack()
        Thread.sleep(timeToWait)
    }
    
    @Test
    fun collectorDetailSecondItemTest() {
        // Given
        navigateToCollectorsTab()

        // When
        onView(withId(R.id.collectorRv))
            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))

        onView(isRoot()).perform(TestUtils.disableRecyclerViewAnimations())

        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))

        onView(withId(R.id.collector_photo))
            .check(matches(isDisplayed()))

        onView(withId(R.id.email))
            .check(matches(isDisplayed()))

        onView(withId(R.id.phone))
            .check(matches(isDisplayed()))

        pressBack()
        Thread.sleep(timeToWait)
    }
    
    private fun navigateToCollectorsTab() {
        Thread.sleep(timeToWait)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(1000)
        onView(withText("Coleccionistas")).perform(click())
        Thread.sleep(timeToWait * 2)
    }
} 