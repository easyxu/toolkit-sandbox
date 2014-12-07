package com.phoenix.common.jackson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {
	private static final ObjectMapper mapper;

	static {
		mapper = Jackson.newObjectMapper();
		// 设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		// 回车
		mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
	}

	private JSON() {

	}

	public static ObjectMapper getInstance() {

		return mapper;
	}

	public static String json(Object obj) throws IOException {
		StringWriter sw = new StringWriter();
		JsonGenerator gen = new JsonFactory().createGenerator(sw);
		mapper.writeValue(gen, obj);
		gen.close();
		return sw.toString();
	}
	public static <T> T parse(String source, Class<T> type) {
		try {
			return mapper.readValue(source, type);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static String toJson(Object obj) throws JsonProcessingException {
		return mapper.writeValueAsString(obj);
	}

	public static <T> List<T> toList(String json, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		List<T> list = mapper.readValue(json, mapper.getTypeFactory()
				.constructCollectionType(ArrayList.class, clazz));
		return list;
	}

	

	public static <T> T toObj(JsonNode node, Class<T> clazz)
			throws JsonParseException, JsonMappingException, IOException {
		return mapper.readValue(node.toString(), clazz);
	}

	public static JsonNode toNode(String json) throws JsonProcessingException,
			IOException {
		return mapper.readTree(json);
	}

	public static JsonNode toNode(Object obj) throws JsonProcessingException,
			IOException {
		String json = toJson(obj);
		return toNode(json);
	}

	@SuppressWarnings("rawtypes")
	public static Map parse(String source) throws IOException {
		return mapper.readValue(source, Map.class);
	}
}
