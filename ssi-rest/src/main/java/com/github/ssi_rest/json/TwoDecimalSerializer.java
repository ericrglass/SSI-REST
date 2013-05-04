package com.github.ssi_rest.json;

import java.io.IOException;
import java.math.BigDecimal;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;

public class TwoDecimalSerializer extends JsonSerializer<BigDecimal> {

	/**
	 * @see org.codehaus.jackson.map.JsonSerializer#serialize(java.lang.Object,
	 *      org.codehaus.jackson.JsonGenerator,
	 *      org.codehaus.jackson.map.SerializerProvider)
	 */
	@Override
	public void serialize(BigDecimal value, JsonGenerator jsonGen,
			SerializerProvider provider) throws IOException,
			JsonProcessingException {
		jsonGen.writeString(value.setScale(2, BigDecimal.ROUND_HALF_UP)
				.toString());
	}
}
