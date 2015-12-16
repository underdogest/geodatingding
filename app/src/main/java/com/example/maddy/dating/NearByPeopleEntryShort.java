package com.example.maddy.dating;

/**
 * Created by Maddy on 03.12.2015.
 */
public class NearByPeopleEntryShort
{
    public String name;
    public Integer distance;
    public String imageUrl;
    public Integer userID;

    public NearByPeopleEntryShort()
    {
        this.name = "unknown";
        this.distance = 666;
        this.imageUrl = "";
        this.userID = 0;
    }

    public int getTitle() {
        return 0;
    }
}
