package com.example.visitus.data;

public class place_data {
     String image,name,city,longitude,latitude,about,id,image_id;

    public place_data(String image, String name, String city, String longitude, String latitude, String about, String id,String image_id) {
        this.image = image;
        this.name = name;
        this.city = city;
        this.longitude = longitude;
        this.latitude = latitude;
        this.about = about;
        this.id = id;
        this.image_id=image_id;
    }

    public String getImage_id() {
        return image_id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getAbout() {
        return about;
    }

    public String getId() {
        return id;
    }
}
