package com.github.ssi_rest.jaxrs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.map.ObjectMapper;

import com.github.ssi_rest.SsiRestLogger;

public class JsonMessageProvider implements MessageBodyReader<Object>,
		MessageBodyWriter<Object> {

	@Override
	public long getSize(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return -1;
	}

	@Override
	public boolean isWriteable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isJsonValid(type, mediaType);
	}

	@Override
	public void writeTo(Object t, Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, Object> httpHeaders,
			OutputStream entityStream) throws IOException,
			WebApplicationException {
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapper.writeValue(entityStream, t);
		} catch (Exception ex) {
			SsiRestLogger.LOGGER.log(Level.SEVERE,
					"JsonMessageProvider ObjectMapper.writeValue Exception: "
							+ ex.getMessage(), ex);
			throw new WebApplicationException(
					new Exception(
							"JsonMessageProvider ObjectMapper.writeValue Exception",
							ex), 500);
		}
	}

	@Override
	public boolean isReadable(Class<?> type, Type genericType,
			Annotation[] annotations, MediaType mediaType) {
		return isJsonValid(type, mediaType);
	}

	@Override
	public Object readFrom(Class<Object> type, Type genericType,
			Annotation[] annotations, MediaType mediaType,
			MultivaluedMap<String, String> httpHeaders, InputStream entityStream)
			throws IOException, WebApplicationException {
		Object mapperObj = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			mapperObj = mapper.readValue(entityStream, type);
		} catch (Exception ex) {
			SsiRestLogger.LOGGER.log(Level.SEVERE,
					"JsonMessageProvider ObjectMapper.readValue Exception: "
							+ ex.getMessage(), ex);
			throw new WebApplicationException(
					new Exception(
							"JsonMessageProvider ObjectMapper.readValue Exception",
							ex), 400);
		}

		return mapperObj;
	}

	private boolean isJsonValid(Class<?> type, MediaType mediaType) {
		boolean hasAnnotation = false;

		if (List.class.isAssignableFrom(type)
				|| Map.class.isAssignableFrom(type)) {
			hasAnnotation = true;
		} else if ((type.getAnnotations() != null)
				&& (type.getAnnotations().length > 0)) {
			for (Annotation annotation : type.getAnnotations()) {
				if (annotation.annotationType().toString().toLowerCase()
						.contains(".json")) {
					hasAnnotation = true;
					break;
				}
			}
		}

		return (hasAnnotation && MediaType.APPLICATION_JSON_TYPE
				.equals(mediaType));
	}

}
