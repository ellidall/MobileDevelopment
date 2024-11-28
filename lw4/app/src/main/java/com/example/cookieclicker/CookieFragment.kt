package com.example.cookieclicker

import CookieClickerViewModel
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.cookieclicker.databinding.FragmentClickerBinding

class CookieFragment : Fragment() {

    private var _binding: FragmentClickerBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CookieClickerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        binding.btnClicker.setOnClickListener {
            viewModel.clickCookie()
            animateButtonClick(it)
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                binding.tvCookieCount.text = state.cookieCount.toString()
                binding.tvCookiesPerSecond.text = state.cookiesPerSecond.toString()
                binding.tvCookiesPerMinute.text = "${state.cookiesPerSecond * 60}"
                binding.tvTimeElapsed.text = state.elapsedTime.toString()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.toastMessages.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun animateButtonClick(view: View) {
        val scaleDownX = ObjectAnimator.ofFloat(view, "scaleX", 0.95f)
        val scaleDownY = ObjectAnimator.ofFloat(view, "scaleY", 0.95f)
        scaleDownX.duration = 100
        scaleDownY.duration = 100

        val scaleUpX = ObjectAnimator.ofFloat(view, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(view, "scaleY", 1f)
        scaleUpX.duration = 100
        scaleUpY.duration = 100

        val scaleDown = AnimatorSet()
        scaleDown.playTogether(scaleDownX, scaleDownY)

        val scaleUp = AnimatorSet()
        scaleUp.playTogether(scaleUpX, scaleUpY)

        val animationSet = AnimatorSet()
        animationSet.playSequentially(scaleDown, scaleUp)
        animationSet.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

