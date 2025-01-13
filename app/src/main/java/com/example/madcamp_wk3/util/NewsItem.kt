package com.example.madcamp_wk3.util
class NewsItem {
    lateinit var imageUrl: String;
    lateinit var newsTitle : String;

    constructor(title : String, image : String){
        this.newsTitle=title
        this.imageUrl=image
    }

}