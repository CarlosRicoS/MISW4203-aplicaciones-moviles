package co.edu.uniandes.miso.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.*
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class DetailedAlbumTest {
    
    private val timeToWait = 1000L

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun detailedAlbumFirstTabTest() {
        Thread.sleep(timeToWait)
        // Given
        val recyclerView = onView(
            allOf(
                withId(R.id.albumsRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )

        // When
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(timeToWait)

        // Then
        val textView = onView(
            allOf(
                withId(R.id.año), withText("1984"),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1984")))

        val imageView = onView(
            allOf(
                withId(R.id.album_cover),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.titulo), withText("Buscando América"),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Buscando América")))

        val textView3 = onView(
            allOf(
                withId(R.id.descripcion),
                withText("Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983."),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.")))

        val textView4 = onView(
            allOf(
                withId(R.id.genero), withText("Salsa"),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Salsa")))

        val textView5 = onView(
            allOf(
                withId(R.id.recordLabel), withText("Elektra"),
                isDisplayed()
            )
        )
        textView5.check(matches(withText("Elektra")))
    }

    @Test
    fun detailedAlbumSecondTabTest() {

        Thread.sleep(timeToWait)
        // Given
        val recyclerView = onView(
            allOf(
                withId(R.id.albumsRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(timeToWait)

        val tabView = onView(
            allOf(
                withContentDescription("ARTISTAS"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.albumDetailTabs),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )

        // When
        tabView.perform(click())
        Thread.sleep(timeToWait)


        // Then
        val imageView = onView(
            allOf(
                withId(R.id.performer_image),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.performer_name), withText("Rubén Blades Bellido de Luna"),
                withParent(withParent(IsInstanceOf.instanceOf(androidx.recyclerview.widget.RecyclerView::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Rubén Blades Bellido de Luna")))

        val textView2 = onView(
            allOf(
                withId(R.id.description),
                withText("Es un cantante, compositor, músico, actor, abogado, político y activista panameño. Ha desarrollado gran parte de su carrera artística en la ciudad de Nueva York."),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Es un cantante, compositor, músico, actor, abogado, político y activista panameño. Ha desarrollado gran parte de su carrera artística en la ciudad de Nueva York.")))

    }

    @Test
    fun detailedAlbumThirdTabTest() {

        Thread.sleep(timeToWait)

        // Given
        val recyclerView = onView(
            allOf(
                withId(R.id.albumsRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(0, click()))
        Thread.sleep(timeToWait)

        val tabView = onView(
            allOf(
                withContentDescription("COMENTARIOS"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.albumDetailTabs),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        // When
        tabView.perform(click())
        Thread.sleep(timeToWait)

        // Then
        val textView = onView(
            allOf(
                withId(R.id.comment_description),
                withText("The most relevant album of Ruben Blades"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("The most relevant album of Ruben Blades")))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
