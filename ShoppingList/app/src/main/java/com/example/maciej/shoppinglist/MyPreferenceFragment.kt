package com.example.maciej.shoppinglist

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.preference.PreferenceFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

class MyPreferenceFragment() : PreferenceFragment(){

    private lateinit var myDataset: ArrayList<Product>
    private lateinit var viewAdapter: MyAdapter

    @SuppressLint("ValidFragment")
    constructor( dataset: ArrayList<Product>, adapter: MyAdapter) : this(){
        myDataset = dataset
        viewAdapter = adapter
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addPreferencesFromResource(R.xml.settings)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var switchPreference = preferenceManager.findPreference(this.getString(R.string.sort_setting_key))
        switchPreference.setOnPreferenceChangeListener { _, newValue ->
            if(newValue == true) {
                myDataset.sortBy { it.Desc }
                viewAdapter.notifyDataSetChanged()
            }
            else{
                myDataset.sortBy { it.Id }
                viewAdapter.notifyDataSetChanged()
            }
            true
        }

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        view.setBackgroundColor(Color.WHITE)
        super.onActivityCreated(savedInstanceState)
    }
}

