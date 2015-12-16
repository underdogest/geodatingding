package com.example.maddy.dating;

import com.example.maddy.dating.NearByPeopleEntryShort;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Admin on 13.12.2015.
 */
public final class PeopleCache
{

	public static final String serverUrl = "http://192.168.137.1:8080/users";


	public static void start(Context ctx)
	{

	}

	public static List<NearByPeopleEntryShort> getListFixedLocation(Context ctx)
	{
		Location location = new Location("myself");
		location.setLatitude(52.1809617);
		location.setLongitude(10.5580299);
		return getList(ctx, location);
	}

	public static List<NearByPeopleEntryShort> getList(Context ctx, Location locMe)
	{
		List<NearByPeopleEntryShort> ret = new ArrayList<NearByPeopleEntryShort>();
		try
		{
			String zipcode;
			Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
			List<Address> addresses = geocoder.getFromLocation(locMe.getLatitude(), locMe.getLongitude(), 1);
			if(addresses.size() > 0)
				zipcode = addresses.get(0).getPostalCode();
			else
				zipcode = "38302";

			{
				URL url = new URL(serverUrl + "/find/" + zipcode);
				URLConnection urlConnection = url.openConnection();
				InputStream in = new BufferedInputStream(urlConnection.getInputStream());
				JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

				reader.beginArray();
				while (reader.hasNext())
				{
					NearByPeopleEntryShort current = new NearByPeopleEntryShort();
					Location locationCurrent = new Location("point A");
					reader.beginObject();
					while (reader.hasNext())
					{
						String name = reader.nextName();
						if(reader.peek() != JsonToken.NULL)
						{
							if (name.equals("FirstName"))
								current.name = reader.nextString();
							else if (name.equals("Id"))
								current.userID = new Integer(reader.nextString());
							 else if (name.equals("CurrentLocationLAT"))
								locationCurrent.setLatitude(new Double(reader.nextString()));
							else if (name.equals("CurrentLocationLNG"))
								locationCurrent.setLongitude(new Double(reader.nextString()));
							else
								reader.skipValue();
						}
						else
							reader.skipValue();
					}
					reader.endObject();
					current.distance = Math.round(locMe.distanceTo(locationCurrent));
					current.imageUrl = serverUrl + "/image/" + current.userID.toString();
					ret.add(current);
				}
				reader.endArray();
			}
		} catch (IOException e)
		{

		}
		return ret;
	}

	public static PeopleProfileFull loadDetails(Integer userID)
	{
		PeopleProfileFull ret = new PeopleProfileFull(userID);
		try
		{
			URL url = new URL(serverUrl + "/profile/" + userID.toString());
			URLConnection urlConnection = url.openConnection();
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if(reader.peek() != JsonToken.NULL)
				{
					if (name.equals("FirstName"))
						ret.firstName = reader.nextString();
					 else if (name.equals("LastName"))
						ret.lastName = reader.nextString();
					 else if (name.equals("Username"))
						ret.name = reader.nextString();
					 else if (name.equals("ExtraProfileData"))
						ret.extraProfile = reader.nextString();
					 else if (name.equals("CurrentLocationLAT"))
						ret.positionLat = reader.nextString();
					 else if (name.equals("CurrentLocationLNG"))
						ret.positionLong = reader.nextString();
					 else
						reader.skipValue();
				}
				else
					reader.skipValue();
			}
			reader.endObject();
			ret.imageUrl = serverUrl + "/image/" + userID.toString();

		} catch (MalformedURLException e)
		{
			e.printStackTrace();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
}
