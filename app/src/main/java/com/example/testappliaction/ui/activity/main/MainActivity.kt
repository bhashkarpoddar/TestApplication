package com.example.testappliaction.ui.activity.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.testappliaction.R
import com.example.testappliaction.data.model.DataItem
import com.example.testappliaction.databinding.ActivityMainBinding
import com.example.testappliaction.ui.fragment.dynamicViewPagerFragment.DynamicViewPagerFragment
import com.example.testappliaction.util.ext.getViewModel
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private var viewPagerAdapter: ViewPagerAdapter? = null
    private var pagerList: MutableList<DataItem>? = null
    private var mFragmentList: MutableList<Fragment>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        viewModel = getViewModel { MainViewModel() }

        iniObserver()
        initView()
        lifecycleScope.launch {
            viewModel.getProductList()
        }
    }

    private fun iniObserver() {
        pagerList = mutableListOf()
        mFragmentList = mutableListOf()
        viewModel.progressBar.observe(this, {
            if (it){
                binding.progresssBar.visibility = View.VISIBLE
            }else binding.progresssBar.visibility = View.GONE
        })

        viewModel.success.observe(this, {
            viewModel.progressBar.value = false
        })

        viewModel.responseResult.observe(this, {result->
            result?.let {
                pagerList?.clear()
                mFragmentList?.clear()
                pagerList?.addAll(it.data)
                pagerList?.forEachIndexed { index, dataItem ->
                    mFragmentList?.add(DynamicViewPagerFragment.newInstance(viewModel, index))
                }
                TabLayoutMediator(binding.tab, binding.viewpager, false, true) { tab, position ->
                    tab.text = pagerList?.get(position)?.cat_name
                    /*binding.viewpager.setCurrentItem(tab.position, true)*/
                }.attach()
                viewPagerAdapter?.notifyDataSetChanged()
            }
        })

        viewModel.error.observe(this, {
            viewModel.progressBar.value = false
            Toast.makeText(this, it.second, Toast.LENGTH_LONG).show()
        })
    }

    private fun initView() {
        viewPagerAdapter = ViewPagerAdapter(this, mFragmentList!!)
        binding.viewpager.adapter = viewPagerAdapter
        binding.viewpager.currentItem = 0
    }
}