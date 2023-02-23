package com.laundrybuoy.admin.adapter.notification

import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.laundrybuoy.admin.ui.SplashFragment
import com.laundrybuoy.admin.ui.notification.NotificationDynamicFragment
import com.laundrybuoy.admin.ui.notification.NotificationRootFragment
import com.laundrybuoy.admin.utils.DynamicViewPager

class DynamicNotificationPagerAdapter(
    fm: FragmentManager,
    val notificationHeading: MutableList<String>,
    val type: Int) :
    FragmentStatePagerAdapter(fm) {
    private var mCurrentPosition = -1

    companion object {
        const val TYPE_NOTIFICATION_FRAGMENT = 0
    }

    override fun getItem(position: Int): Fragment {

        return when (type) {
            TYPE_NOTIFICATION_FRAGMENT -> {
                NotificationDynamicFragment.newInstance(
                    notificationHeading[position])
            }
            else -> {
                SplashFragment()
            }
        }

    }

    override fun getCount(): Int {
        return if (notificationHeading.isNotEmpty())
            notificationHeading.size
        else
            1
    }

    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        super.setPrimaryItem(container, position, `object`)
        if (position != mCurrentPosition) {
            val fragment = `object` as Fragment
            mCurrentPosition = position
            if (container is DynamicViewPager) {
                val pager = container
                if (fragment.view != null) {
                    fragment.view?.let { pager.measureCurrentView(it) }
                }
            }
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return if (notificationHeading.isNotEmpty())
            notificationHeading[position]
        else ""

    }
}