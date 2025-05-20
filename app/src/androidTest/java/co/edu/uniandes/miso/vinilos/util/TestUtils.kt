package co.edu.uniandes.miso.vinilos.util

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.TypeSafeMatcher

/**
 * Utility functions for UI testing
 */
object TestUtils {
    
    /**
     * A ViewAction that disables all RecyclerViews within a view hierarchy to prevent layout issues
     */
    fun disableRecyclerViewAnimations(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isRoot()
            }

            override fun getDescription(): String {
                return "Disabling RecyclerView animations"
            }

            override fun perform(uiController: UiController, view: View) {
                disableRecyclerViewsInHierarchy(view)
                uiController.loopMainThreadUntilIdle()
            }
            
            private fun disableRecyclerViewsInHierarchy(view: View) {
                if (view is RecyclerView) {
                    view.itemAnimator = null
                    view.layoutManager = null
                }
                
                if (view is ViewGroup) {
                    for (i in 0 until view.childCount) {
                        disableRecyclerViewsInHierarchy(view.getChildAt(i))
                    }
                }
            }
        }
    }
    
    /**
     * Custom matcher to find a view by its ID within a parent of a specific type
     */
    fun withParentAndId(parentMatcher: Matcher<View>, viewId: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("with parent matching: ")
                parentMatcher.describeTo(description)
                description.appendText(" and with id: $viewId")
            }

            override fun matchesSafely(view: View): Boolean {
                val parent = view.parent as? View
                return parent != null && parentMatcher.matches(parent) && view.id == viewId
            }
        }
    }
} 