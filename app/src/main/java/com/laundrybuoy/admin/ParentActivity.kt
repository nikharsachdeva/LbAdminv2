package com.laundrybuoy.admin

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import java.util.*
import java.util.concurrent.TimeUnit

open class ParentActivity : AppCompatActivity() {

    var fragmentStack1: MutableList<String> = ArrayList()
    var fragmentStack2: MutableList<String> = ArrayList()
    var fragmentStack3: MutableList<String> = ArrayList()
    var fragmentStack4: MutableList<String> = ArrayList()
    var fragmentStack5: MutableList<String> = ArrayList()

    override fun onDestroy() {
        super.onDestroy()
        clearStackArrayList(0)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    open fun getHeaders(): Map<String, String>? {
        val map: MutableMap<String, String> = HashMap()
        map["Accept"] = "application/json"
        return map
    }


    open fun addFragment(isToAdd: Boolean, layoutPos: Int, fragment: Fragment) {
        var id = R.id.rlFragmentContainer1
        when (layoutPos) {
            0, 1 -> id = R.id.rlFragmentContainer1
            2 -> id = R.id.rlFragmentContainer2
            3 -> id = R.id.rlFragmentContainer3
            4 -> id = R.id.rlFragmentContainer4
            5 -> id = R.id.rlFragmentContainer5
        }
        findViewById<View>(id).visibility = View.VISIBLE
        val fm = supportFragmentManager
        val ft = fm.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_NONE)
        if (isToAdd) {
            val currFragment: Fragment? = getCurrentFragment()
            if (currFragment != null) {
                ft.hide(currFragment)
            }
            val tag = fragment.javaClass.canonicalName + "_" + System.currentTimeMillis()
            ft.add(id, fragment, tag)
            when (layoutPos) {
                0, 1 -> {
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                    fragmentStack1.add(tag)
                }
                2 -> {
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                    fragmentStack2.add(tag)
                }
                3 -> {
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                    fragmentStack3.add(tag)
                }
                4 -> {
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                    fragmentStack4.add(tag)
                }
                5 -> {
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    fragmentStack5.add(tag)
                }
            }
        } else {
            fm.popBackStack()
            val tag = fragment.javaClass.canonicalName + "_" + System.currentTimeMillis()
            ft.replace(id, fragment, tag)
            when (layoutPos) {
                0, 1 -> {
                    fragmentStack1.clear()
                    fragmentStack1.add(tag)
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                }
                2 -> {
                    fragmentStack2.clear()
                    fragmentStack2.add(tag)
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                }
                3 -> {
                    fragmentStack3.clear()
                    fragmentStack3.add(tag)
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                }
                4 -> {
                    fragmentStack4.clear()
                    fragmentStack4.add(tag)
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer5).visibility = View.GONE
                }
                5 -> {
                    fragmentStack5.clear()
                    fragmentStack5.add(tag)
                    findViewById<View>(R.id.rlFragmentContainer2).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer3).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer4).visibility = View.GONE
                    findViewById<View>(R.id.rlFragmentContainer1).visibility = View.GONE
                }
            }
        }
        ft.commit()
    }

    open fun getCurrentFragment(): Fragment? {
        val fm = supportFragmentManager
        val fragmentStack: MutableList<String> = ArrayList()
        when (getVisibleFrame()) {
            0 -> fragmentStack.addAll(fragmentStack1)
            1 -> fragmentStack.addAll(fragmentStack1)
            2 -> fragmentStack.addAll(fragmentStack2)
            3 -> fragmentStack.addAll(fragmentStack3)
            4 -> fragmentStack.addAll(fragmentStack4)
            5 -> fragmentStack.addAll(fragmentStack5)
        }
        return if (fragmentStack.size > 0) {
            fm.findFragmentByTag(fragmentStack[fragmentStack.size - 1])
        } else null
    }

    open fun getVisibleFrame(): Int {
        if (findViewById<View>(R.id.rlFragmentContainer1).visibility == View.VISIBLE) {
            return 1
        }
        if (findViewById<View>(R.id.rlFragmentContainer2).visibility == View.VISIBLE) {
            return 2
        }
        if (findViewById<View>(R.id.rlFragmentContainer3).visibility == View.VISIBLE) {
            return 3
        }
        if (findViewById<View>(R.id.rlFragmentContainer4).visibility == View.VISIBLE) {
            return 4
        }
        return if (findViewById<View>(R.id.rlFragmentContainer5).visibility == View.VISIBLE) {
            5
        } else 1
    }

    open fun getVisibleFrameStakList(): List<String?>? {
        if (findViewById<View>(R.id.rlFragmentContainer1).visibility == View.VISIBLE) {
            return fragmentStack1
        }
        if (findViewById<View>(R.id.rlFragmentContainer2).visibility == View.VISIBLE) {
            return fragmentStack2
        }
        if (findViewById<View>(R.id.rlFragmentContainer3).visibility == View.VISIBLE) {
            return fragmentStack3
        }
        if (findViewById<View>(R.id.rlFragmentContainer4).visibility == View.VISIBLE) {
            return fragmentStack4
        }
        return if (findViewById<View>(R.id.rlFragmentContainer5).visibility == View.VISIBLE) {
            fragmentStack5
        } else fragmentStack1
    }

    open fun getPreviousFragment(): Fragment? {
        val fm = supportFragmentManager
        val fragmentStack: MutableList<String> = ArrayList()
        when (getVisibleFrame()) {
            0 -> fragmentStack.addAll(fragmentStack1)
            1 -> fragmentStack.addAll(fragmentStack1)
            2 -> fragmentStack.addAll(fragmentStack2)
            3 -> fragmentStack.addAll(fragmentStack3)
            4 -> fragmentStack.addAll(fragmentStack4)
            5 -> fragmentStack.addAll(fragmentStack5)
        }
        return if (fragmentStack.size > 1) {
            fm.findFragmentByTag(fragmentStack[fragmentStack.size - 2])
        } else null
    }

    open fun removeCurrentFragment() {
        val fm = supportFragmentManager
        val fragmentStack: MutableList<String> = ArrayList()
        val visibleFrame = getVisibleFrame()
        when (visibleFrame) {
            0 -> fragmentStack.addAll(fragmentStack1)
            1 -> fragmentStack.addAll(fragmentStack1)
            2 -> fragmentStack.addAll(fragmentStack2)
            3 -> fragmentStack.addAll(fragmentStack3)
            4 -> fragmentStack.addAll(fragmentStack4)
            5 -> fragmentStack.addAll(fragmentStack5)
        }
        if (fragmentStack.size > 0) {
            var fragment = fm.findFragmentByTag(fragmentStack[fragmentStack.size - 1])
            if (fragment != null) {
                val ft = fm.beginTransaction()
                ft.remove(fragment)
                fragmentStack.removeAt(fragmentStack.size - 1)
                when (visibleFrame) {
                    0, 1 -> fragmentStack1.removeAt(fragmentStack1.size - 1)
                    2 -> fragmentStack2.removeAt(fragmentStack2.size - 1)
                    3 -> fragmentStack3.removeAt(fragmentStack3.size - 1)
                    4 -> fragmentStack4.removeAt(fragmentStack4.size - 1)
                    5 -> fragmentStack5.removeAt(fragmentStack5.size - 1)
                }
                if (fragmentStack.size > 0) {
                    fragment = fm.findFragmentByTag(fragmentStack[fragmentStack.size - 1])
                    ft.show(fragment!!)
                }
                ft.commit()
            }
        } else {
            finish()
        }
    }

    open fun clearBackStack(pos: Int) {
        val fm = supportFragmentManager
        val fragmentStack: MutableList<String> = ArrayList()
        when (pos) {
            0 -> {
                fragmentStack.addAll(fragmentStack1)
                fragmentStack.addAll(fragmentStack2)
                fragmentStack.addAll(fragmentStack3)
                fragmentStack.addAll(fragmentStack4)
                fragmentStack.addAll(fragmentStack5)
            }
            1 -> fragmentStack.addAll(fragmentStack1)
            2 -> fragmentStack.addAll(fragmentStack2)
            3 -> fragmentStack.addAll(fragmentStack3)
            4 -> fragmentStack.addAll(fragmentStack4)
            5 -> fragmentStack.addAll(fragmentStack5)
        }
        for (i in fragmentStack.indices) {
            val ft = fm.beginTransaction()
            ft.remove(fm.findFragmentByTag(fragmentStack[i])!!)
            ft.commit()
        }
        clearStackArrayList(pos)
    }


    open fun clearStackArrayList(i: Int) {
        when (i) {
            0 -> {
                fragmentStack1.clear()
                fragmentStack2.clear()
                fragmentStack3.clear()
                fragmentStack4.clear()
                fragmentStack5.clear()
            }
            1 -> fragmentStack1.clear()
            2 -> fragmentStack2.clear()
            3 -> fragmentStack3.clear()
            4 -> fragmentStack4.clear()
            5 -> fragmentStack5.clear()
        }
    }

    open fun getSomeFragment(tabPos: Int, nthFragmentFromCurrent: Int): Fragment? {
        val fm = supportFragmentManager
        val fragmentStack: MutableList<String> = ArrayList()
        when (tabPos) {
            0 -> fragmentStack.addAll(fragmentStack1)
            1 -> fragmentStack.addAll(fragmentStack1)
            2 -> fragmentStack.addAll(fragmentStack2)
            3 -> fragmentStack.addAll(fragmentStack3)
            4 -> fragmentStack.addAll(fragmentStack4)
            5 -> fragmentStack.addAll(fragmentStack5)
        }
        return if (fragmentStack.size > nthFragmentFromCurrent) {
            fm.findFragmentByTag(fragmentStack[fragmentStack.size - (nthFragmentFromCurrent + 1)])
        } else null
    }

    open fun hideKeyboard() {
        try {
            val view = currentFocus
            if (view != null) {
                view.clearFocus()
                val inputManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(
                    view.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun convertDpToPixels(dp: Float, context: Context): Int {
        val res = context.resources
        val metrics = res.displayMetrics
        val px = dp * (metrics.densityDpi / 160f)
        return px.toInt()
    }

    fun getTimeAgo(duration: Long): String? {
        val now = Date()
        val seconds = TimeUnit.MILLISECONDS.toSeconds(now.time - duration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(now.time - duration)
        val hours = TimeUnit.MILLISECONDS.toHours(now.time - duration)
        val days = TimeUnit.MILLISECONDS.toDays(now.time - duration)
        return if (seconds < 60) {
            "just now"
        } else if (minutes == 1L) {
            "a minute ago"
        } else if (minutes > 1 && minutes < 60) {
            "$minutes minutes ago"
        } else if (hours == 1L) {
            "an hour ago"
        } else if (hours > 1 && hours < 24) {
            "$hours hours ago"
        } else if (days == 1L) {
            "a day ago"
        } else {
            "$days days ago"
        }
    }

    open fun getDeviceWidth(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.widthPixels
    }

    open fun getDeviceHeight(): Int {
        val displayMetrics = resources.displayMetrics
        return displayMetrics.heightPixels
    }


}