package com.example.madcamp_wk3.util

class Section {
    lateinit var section_title : String
    lateinit var items : MutableList<NewsItem>

    constructor(title : String, items : MutableList<NewsItem> ){
        this.section_title=title
        this.items=items
    }
}