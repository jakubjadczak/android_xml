package edu.put.pro2_151849

class GamesDB {
    var name: String? = null
    var yearPublished: String? = null
    var image: String? = null
    var thumbnail: String? = null

    constructor(name: String, yearPublished: String, image: String, thumbnail: String){
        this.name = name
        this.yearPublished = yearPublished
        this.image = image
        this.thumbnail = thumbnail
    }

}