package com.example.testappliaction.ui.fragment.dynamicViewPagerFragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.testappliaction.data.model.Product
import com.example.testappliaction.databinding.FragmentViewPagerBinding
import com.example.testappliaction.ui.activity.main.MainViewModel

class DynamicViewPagerFragment : Fragment() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentViewPagerBinding
    private var indexValue: Int = 0
    private var adapter: ProductAdapter? = null
    private var productList: MutableList<Product>? = null

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ViewPagerFragment.
         */
        @JvmStatic
        fun newInstance(mainViewModel: MainViewModel, index: Int) =
            DynamicViewPagerFragment().apply {
                viewModel = mainViewModel
                indexValue = index
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        productList = mutableListOf()
        adapter = ProductAdapter(productList!!, object : ProductAdapter.Interaction{
            override fun onItemSelected(position: Int, item: Product) {

            }
        })
        binding.productRv.adapter = adapter
        viewModel.responseResult.value?.data?.get(indexValue)?.let {
            productList?.clear()
            productList?.addAll(it.items)
            adapter?.notifyDataSetChanged()
        }
    }

}