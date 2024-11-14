package com.example.final_project_assignment_hansenbillyramades.presentation.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.HomeFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyCartFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyProfileFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyTransactionsFragment

class ViewPager2Adapter (fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> MyCartFragment()
            2 -> MyTransactionsFragment()
            3 -> MyProfileFragment()
            else -> throw IllegalStateException("Invalid tab position ${position}")
        }
    }

}