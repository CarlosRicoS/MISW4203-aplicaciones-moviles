package co.edu.uniandes.miso.vinilos


import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class ListCollectorTest {

    private val timeToWait = 1000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun aCollectorExistsInTheList() {

        // Given
        navigateToCollectorsTab()

        val name = "Jaime Monsalve"
        val email = "jmonsalve@rtvc.com.co"

        // When
        val collectorToValidate = getListCollectorItem(name, email)

        // Then
        collectorToValidate.check(matches(isDisplayed()))
    }

    @Test
    fun anotherCollectorExistsInTheList() {

        // Given
        navigateToCollectorsTab()

        val name = "Manolo Bellon"
        val email = "manollo@caracol.com.co"

        // When
        val collectorToValidate = getListCollectorItem(name, email)

        // Then
        collectorToValidate.check(matches(isDisplayed()))
    }

    private fun navigateToCollectorsTab() {
        Thread.sleep(timeToWait)

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open())
        Thread.sleep(500)

        onView(withText("Coleccionistas")).perform(click())

        Thread.sleep(timeToWait)
    }

    private fun getListCollectorItem(collectorName:String, collectorEmail:String): ViewInteraction = onView(
        allOf(
            IsInstanceOf.instanceOf(FrameLayout::class.java),
            withChild(
                allOf(
                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                    withChild(
                        allOf(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            withChild(
                                allOf(
                                    IsInstanceOf.instanceOf(TextView::class.java),
                                    withId(R.id.collector_name),
                                    withText(collectorName)
                                )
                            ),
                            withChild(
                                allOf(
                                    IsInstanceOf.instanceOf(TextView::class.java),
                                    withId(R.id.collector_email),
                                    withText(collectorEmail)
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}
