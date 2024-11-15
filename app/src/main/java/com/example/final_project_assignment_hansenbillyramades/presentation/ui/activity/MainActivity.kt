package com.example.final_project_assignment_hansenbillyramades.presentation.ui.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.final_project_assignment_hansenbillyramades.R
import com.example.final_project_assignment_hansenbillyramades.databinding.ActivityMainBinding
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.HomeFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyCartFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyProfileFragment
import com.example.final_project_assignment_hansenbillyramades.presentation.ui.fragment.MyTransactionsFragment
import com.google.android.material.navigation.NavigationBarView.OnItemSelectedListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.bottomNav.elevation = 120f
        binding.bottomNav.setOnItemSelectedListener(object : OnItemSelectedListener {
            override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_home -> {
                        replaceFragment(HomeFragment())
                        true
                    }

                    R.id.menu_cart -> {
                        replaceFragment(MyCartFragment())
                        true
                    }

                    R.id.menu_transaction -> {
                        replaceFragment(MyTransactionsFragment())
                        true
                    }

                    R.id.menu_profile -> {
                        replaceFragment(MyProfileFragment())
                        true
                    }

                    else -> false
                }
            }
        })

        val navigateTo = intent.getStringExtra("navigateTo")
        if (navigateTo == "MyCartFragment") {
            binding.bottomNav.selectedItemId = R.id.menu_cart
        } else {
            replaceFragment(HomeFragment())
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}