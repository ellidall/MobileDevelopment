package com.example.cookieclicker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.cookieclicker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setFragment(CookieFragment(), CookieFragment::class.java.simpleName)
        }

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_clicker -> {
                    showFragment(CookieFragment(), CookieFragment::class.java.simpleName)
                    true
                }
                R.id.nav_store -> {
                    showFragment(StoreFragment(), StoreFragment::class.java.simpleName)
                    true
                }
                else -> false
            }
        }
    }

    private fun showFragment(fragment: Fragment, tag: String) {
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        val transaction = supportFragmentManager.beginTransaction()

        if (existingFragment == null) {
            transaction.add(binding.fragmentContainer.id, fragment, tag)
        } else {
            transaction.show(existingFragment)
        }

        supportFragmentManager.fragments.forEach {
            if (it != existingFragment) {
                transaction.hide(it)
            }
        }

        transaction.commit()
    }

    private fun setFragment(fragment: Fragment, tag: String) {
        supportFragmentManager.beginTransaction()
            .replace(binding.fragmentContainer.id, fragment, tag)
            .commit()
    }
}