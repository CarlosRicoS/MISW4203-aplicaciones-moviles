package co.edu.uniandes.miso.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
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
class ListPerformerTest {

    private val timeToWait = 1000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun aPerformerExistsInTheList() {
        // Given
        navigateToPerformersTab()
        
        // When
        val performerName = "Rubén Blades Bellido de Luna"
        
        // Then
        onView(withId(R.id.performerRv))
            .check(matches(hasDescendant(withText(performerName))))
    }

    @Test
    fun anotherPerformerExistsInTheList() {
        // Given
        navigateToPerformersTab()
        
        // When
        val performerName = "Queen"
        
        // Then
        onView(withId(R.id.performerRv))
            .check(matches(hasDescendant(withText(performerName))))
    }
    
    private fun navigateToPerformersTab() {
        Thread.sleep(timeToWait)
        
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(500)
        
        onView(withText("Artistas")).perform(click())
        
        Thread.sleep(timeToWait)
    }
} 