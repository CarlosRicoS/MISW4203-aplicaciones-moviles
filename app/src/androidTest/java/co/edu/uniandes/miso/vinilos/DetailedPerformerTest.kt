package co.edu.uniandes.miso.vinilos

import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailedPerformerTest {
    
    private val timeToWait = 2000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailedPerformerTest() {
        // Given
        navigateToPerformersTab()
        
        // When
        onView(withId(R.id.performerRv))
            .perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.performer_name))
            .check(matches(isDisplayed()))
        
        onView(withId(R.id.performer_image))
            .check(matches(isDisplayed()))
              
        onView(withId(R.id.description))
            .check(matches(isDisplayed()))
    }
    
    @Test
    fun detailedPerformerTest2() {
        // Given
        navigateToPerformersTab()
        
        // When
        onView(withId(R.id.performerRv))
            .perform(actionOnItemAtPosition<ViewHolder>(1, click()))
        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.performer_name))
            .check(matches(isDisplayed()))
              
        onView(withId(R.id.performer_image))
            .check(matches(isDisplayed()))
              
        onView(withId(R.id.description))
            .check(matches(isDisplayed()))
    }
    
    private fun navigateToPerformersTab() {
        Thread.sleep(timeToWait)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(1000)
        onView(withText("Artistas")).perform(click())
        Thread.sleep(timeToWait * 2)
    }
}