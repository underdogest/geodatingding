package com.example.maddy.dating;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PeopleProfileFull
{
	public String name;
	public String firstName;
	public String lastName;
	public String positionLat;
	public String positionLong;

	public String extraProfile;
	public Integer distance;
	public String imageUrl;
	public Integer userID;
	public Bitmap bitmap;

	public PeopleProfileExtra profileExtra;

	public int getTitle() {
		return 0;
	}

	public PeopleProfileFull(Integer userID)
	{
		this.userID = userID;
	}

	public void loadFromWeb()
	{
		PeopleProfileFull profile = PeopleCache.loadDetails(userID);
		this.name = profile.name;
		this.firstName = profile.firstName;
		this.lastName = profile.lastName;
		this.positionLat = profile.positionLat;
		this.positionLong = profile.positionLong;
		this.extraProfile = profile.extraProfile;
		this.distance = profile.distance;
		this.imageUrl = profile.imageUrl;
		this.userID = profile.userID;
		this.profileExtra = new PeopleProfileExtra(this.extraProfile);

		try
		{
			this.bitmap = BitmapFactory.decodeStream((InputStream)new URL(this.imageUrl).getContent());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
