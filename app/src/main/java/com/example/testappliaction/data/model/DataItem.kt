package com.example.testappliaction.data.model

data class DataItem(
    val cat_id: String,
    val cat_name: String,
    val items: MutableList<Product>
)