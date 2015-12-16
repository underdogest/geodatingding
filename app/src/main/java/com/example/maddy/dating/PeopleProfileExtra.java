package com.example.maddy.dating;

import android.util.JsonReader;
import android.util.JsonToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Created by Admin on 16.12.2015.
 */
public class PeopleProfileExtra
{

	public String getGenderString()
	{
		if(this.male)
			return "Männlich";
		else
			return "Weiblich";
	}

	public String getLookingForString()
	{
		switch(this.lookingFor)
		{
			case FEMALE:
				if(this.male)
					return "Mann sucht Frau";
				else
					return "Frau sucht Frau";
			case MALE:
				if(this.male)
					return "Mann sucht Mann";
				else
					return "Frau sucht Mann";
			case MALE_FEMALE:
				if(this.male)
					return "Mann sucht Mann&Frau";
				else
					return "Frau sucht Mann&Frau";
		}
		return "";
	}

	public String getInterestsString()
	{
		return interests;
	}

	public enum LookingFor
	{
		MALE, MALE_FEMALE, FEMALE

	}
	public Integer alter;
	public Boolean male;
	public LookingFor lookingFor;
	public String interests;

	public PeopleProfileExtra(String jsonExtra)
	{
		JsonReader reader = new JsonReader(new StringReader(jsonExtra));
		try
		{
			reader.beginObject();
			while (reader.hasNext()) {
				String name = reader.nextName();
				if(reader.peek() != JsonToken.NULL)
				{
					if(reader.peek() != JsonToken.NULL)
					{
						if (name.equals("Age"))
							this.alter = reader.nextInt();
						else if (name.equals("Male"))
							this.male = reader.nextBoolean();
						else if (name.equals("LookingFor"))
							this.lookingFor = LookingFor.valueOf(reader.nextString());
						else if (name.equals("Interests"))
							this.interests = reader.nextString();
						else reader.skipValue();
					}
				}
				else
					reader.skipValue();
			}
			reader.endObject();
		} catch (IOException e)
		{
			e.printStackTrace();
		}

	}
}
