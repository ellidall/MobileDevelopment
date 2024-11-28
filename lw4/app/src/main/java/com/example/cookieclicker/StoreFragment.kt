package com.example.cookieclicker

import CookieClickerViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cookieclicker.databinding.FragmentStoreBinding

class StoreFragment : Fragment() {

    private var _bindingStore: FragmentStoreBinding? = null
    private val bindingStore get() = _bindingStore!!
    
    private lateinit var adapter: ItemBuildingAdapter
    private val viewModel: CookieClickerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _bindingStore = FragmentStoreBinding.inflate(inflater, container, false)
        return bindingStore.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ItemBuildingAdapter { building ->
            viewModel.buyBuilding(building)
        }
        bindingStore.recyclerViewBuildings.layoutManager = LinearLayoutManager(requireContext())
        bindingStore.recyclerViewBuildings.adapter = adapter
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect { state ->
                adapter.submitList(state.buildings)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.toastMessages.collect { message ->
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindingStore = null
    }
}
