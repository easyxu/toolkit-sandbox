package com.phoenix.common.jackson;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk7.Jdk7Module;
public class Jackson {

	private Jackson() {}
	public static ObjectMapper newObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();

        return configure(mapper);
    }
	
	 public static ObjectMapper newObjectMapper(JsonFactory jsonFactory) {
	        final ObjectMapper mapper = new ObjectMapper(jsonFactory);

	        return configure(mapper);
	    }

	    private static ObjectMapper configure(ObjectMapper mapper) {
	        mapper.registerModule(new Jdk7Module());

	        return mapper;
	    }
}
