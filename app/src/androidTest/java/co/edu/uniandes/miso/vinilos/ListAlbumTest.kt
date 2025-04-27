package co.edu.uniandes.miso.vinilos


import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.assertion.ViewAssertions.matches
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
class ListAlbumTest {

    private val timeToWait = 1000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun anAlbumExistsInTheList() {

        Thread.sleep(timeToWait)
        // Given
        val name = "Buscando América"
        val author = "Rubén Blades Bellido de Luna"

        // When
        val albumToValidate = getListAlbumItem(name, author)

        // Then
        albumToValidate.check(matches(isDisplayed()))
    }

    @Test
    fun anotherAlbumExistsInTheList() {

        Thread.sleep(timeToWait)
        // Given
        val name = "A Night at the Opera"
        val author = "Queen"

        // When
        val albumToValidate = getListAlbumItem(name, author)

        // Then
        albumToValidate.check(matches(isDisplayed()))
    }

    private fun getListAlbumItem(albumTitle:String, albumAuthor:String): ViewInteraction = onView(
        allOf(
            IsInstanceOf.instanceOf(FrameLayout::class.java),
            withChild(
                allOf(
                    IsInstanceOf.instanceOf(LinearLayout::class.java),
                    withChild(
                        allOf(
                            IsInstanceOf.instanceOf(ImageView::class.java),
                            withId(R.id.album_cover)
                        )
                    ),
                    withChild(
                        allOf(
                            IsInstanceOf.instanceOf(LinearLayout::class.java),
                            withChild(
                                allOf(
                                    IsInstanceOf.instanceOf(TextView::class.java),
                                    withId(R.id.album_title),
                                    withText(albumTitle)
                                )
                            ),
                            withChild(
                                allOf(
                                    IsInstanceOf.instanceOf(TextView::class.java),
                                    withId(R.id.album_description),
                                    withText(albumAuthor)
                                )
                            )
                        )
                    )
                )
            )
        )
    )
}
