package com.kzksmarthome.common.lib.util;

import java.lang.reflect.Type;
import java.util.Date;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class GsonHelper {
	static JsonSerializer<Date> ser = new JsonSerializer<Date>() {
		public JsonElement serialize(Date src, Type typeOfSrc,
				JsonSerializationContext context) {
			return src == null ? null : new JsonPrimitive(src.getTime());
		}
	};

	static JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
		public Date deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			return json == null ? null : new Date(json.getAsLong());
		}
	};
	
	public static Gson getGson() {
		Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, ser)
				.registerTypeAdapter(Date.class, deser).create();
		return gson;
	}
}
