package co.edu.uniandes.miso.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.RootMatchers.isPlatformPopup
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withChild
import androidx.test.espresso.matcher.ViewMatchers.withClassName
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
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
import java.util.Date

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
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        Thread.sleep(timeToWait)

        // Then
        val textView = onView(
            allOf(
                withId(R.id.album_year), withText("1984"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("1984")))

        val imageView = onView(
            allOf(
                withId(R.id.album_cover),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView2 = onView(
            allOf(
                withId(R.id.album_title), withText("Buscando América"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Buscando América")))

        val textView3 = onView(
            allOf(
                withId(R.id.album_description),
                withText("Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983."),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("Buscando América es el primer álbum de la banda de Rubén Blades y Seis del Solar lanzado en 1984. La producción, bajo el sello Elektra, fusiona diferentes ritmos musicales tales como la salsa, reggae, rock, y el jazz latino. El disco fue grabado en Eurosound Studios en Nueva York entre mayo y agosto de 1983.")))

        val textView4 = onView(
            allOf(
                withId(R.id.album_genre), withText("Salsa"),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Salsa")))

        val textView5 = onView(
            allOf(
                withId(R.id.album_recordLabel), withText("Elektra"),
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
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
        Thread.sleep(timeToWait)

        val tabView = onView(
            allOf(
                withContentDescription("ARTISTA"),
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
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.performer_name), withText("Rubén Blades Bellido de Luna"),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.FrameLayout::class.java))),
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
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
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

    @Test
    fun addCommentTest() {

        // Given
        val newComment = "New Comment at -> " + Date().time
        navigateToCollectorUserFlow()
        navigateToAlbumsTab()
        val recyclerView = onView(
            allOf(
                withId(R.id.albumsRv),
                childAtPosition(
                    withClassName(`is`("android.widget.FrameLayout")),
                    1
                )
            )
        )
        recyclerView.perform(actionOnItemAtPosition<ViewHolder>(2, click()))
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

        tabView.perform(click())
        Thread.sleep(timeToWait)

        onView(withId(R.id.commentRating)).perform(click())
        Thread.sleep(timeToWait / 2)
        onView(withText("4"))
            .inRoot(isPlatformPopup())
            .perform(click())
        Thread.sleep(timeToWait / 2)

        val textInputEditText = onView(
            allOf(
                withId(R.id.contentInput),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("com.google.android.material.textfield.TextInputLayout")),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        textInputEditText.perform(replaceText(newComment), closeSoftKeyboard())

        Thread.sleep(timeToWait)

        val materialButton2 = onView(
            allOf(
                withId(R.id.addCommentButton), withText("AGREGAR"),
                childAtPosition(
                    allOf(
                        withId(R.id.newCommentForm),
                        childAtPosition(
                            withClassName(`is`("android.widget.LinearLayout")),
                            1
                        )
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        // When
        materialButton2.perform(click())
        Thread.sleep(timeToWait)

        // Then
        val commentView = onView(
            allOf(
                IsInstanceOf.instanceOf(android.widget.LinearLayout::class.java),
                withChild(
                    allOf(
                        IsInstanceOf.instanceOf(android.widget.TextView::class.java),
                        withId(R.id.collector_name), withText("Coleccionista")
                    )
                ),
                withChild(
                    allOf(
                        withChild(
                            allOf(
                                IsInstanceOf.instanceOf(android.widget.TextView::class.java),
                                withId(R.id.comment_rating), withText("4")
                            )
                        ),
                        withChild(
                            allOf(
                                IsInstanceOf.instanceOf(android.widget.TextView::class.java),
                                withId(R.id.comment_description), withText(newComment)
                            )
                        )
                    )
                )
            )
        )

        commentView.check(matches(isDisplayed()))
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
