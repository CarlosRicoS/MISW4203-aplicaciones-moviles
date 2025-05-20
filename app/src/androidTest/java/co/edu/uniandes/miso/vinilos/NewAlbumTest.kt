package co.edu.uniandes.miso.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NewAlbumTest {
    
    private val timeToWait = 2000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun verifyNewAlbumFormDisplayedTest() {
        // Given
        navigateToCollectorUserFlow()
        navigateToAlbumsTab()
        
        // When
        onView(withId(R.id.new_album)).perform(click())
        Thread.sleep(timeToWait)

        // Then
        onView(withId(R.id.name))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.cover))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.release_date))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.description))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.genre))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.record_label))
            .check(matches(isDisplayed()))
            
        onView(withId(R.id.performer))
            .check(matches(isDisplayed()))
    }
    
    @Test
    fun createNewAlbumWithValidDataTest() {
        // Given
        navigateToCollectorUserFlow()
        navigateToAlbumsTab()
        
        // When
        onView(withId(R.id.new_album)).perform(click())
        Thread.sleep(timeToWait)
        
        onView(withId(R.id.name))
            .perform(typeText("Test Album Name"), closeSoftKeyboard())
        
        onView(withId(R.id.cover))
            .perform(typeText("https://example.com/album-cover.jpg"), closeSoftKeyboard())
        
        onView(withId(R.id.release_date))
            .perform(replaceText("May 19, 2025"), closeSoftKeyboard())
        
        onView(withId(R.id.description))
            .perform(typeText("Test description"), closeSoftKeyboard())
        
        onView(withId(R.id.genre)).perform(click())
        Thread.sleep(timeToWait / 2)
        onView(withText("Rock"))
            .inRoot(isPlatformPopup())
            .perform(click())
        Thread.sleep(timeToWait / 2)
        
        onView(withId(R.id.record_label)).perform(click())
        Thread.sleep(timeToWait / 2)
        onView(withText("Sony Music"))
            .inRoot(isPlatformPopup())
            .perform(click())
        Thread.sleep(timeToWait / 2)
        
        onView(withId(R.id.performer)).perform(click())
        Thread.sleep(timeToWait / 2)
        onView(withText("Queen"))
            .inRoot(isPlatformPopup())
            .perform(click())
        Thread.sleep(timeToWait / 2)
        
        onView(withContentDescription("Navigate up")).perform(click())
        
        // Then
        Thread.sleep(timeToWait)
        onView(withId(R.id.albumsRv))
            .check(matches(isDisplayed()))
    }
    
    private fun navigateToCollectorUserFlow() {
        Thread.sleep(timeToWait)
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(1000)
        
        onView(allOf(withText("Usuario"), withId(R.id.title))).perform(click())
        Thread.sleep(timeToWait)
        
        onView(withText("Coleccionista")).perform(click())
        Thread.sleep(timeToWait)
        
        onView(withText("Jaime Monsalve")).perform(click())
        Thread.sleep(timeToWait)
    }
    
    private fun navigateToAlbumsTab() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(1000)
        
        onView(allOf(withText("Albumes"), withId(R.id.title))).perform(click())
        Thread.sleep(timeToWait * 2)
    }
} 