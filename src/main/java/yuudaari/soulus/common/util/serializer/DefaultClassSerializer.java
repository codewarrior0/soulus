package yuudaari.soulus.common.util.serializer;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.annotation.Nullable;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import yuudaari.soulus.common.config.CaseConversion;
import yuudaari.soulus.common.util.Logger;
import yuudaari.soulus.common.util.serializer.ClassSerializationEventHandlers.DeserializationEventHandler;
import yuudaari.soulus.common.util.serializer.ClassSerializationEventHandlers.SerializationEventHandler;
import yuudaari.soulus.common.util.serializer.SerializationHandlers.IFieldDeserializationHandler;
import yuudaari.soulus.common.util.serializer.SerializationHandlers.IFieldSerializationHandler;

public class DefaultClassSerializer extends ClassSerializer<Object> {

	/**
	 * Serialize an object into the given JsonObject.
	 */
	@Override
	public void serialize (final Object instance, final JsonObject object) {
		if (object == null) {
			Logger.warn("Did not receive a Json object to serialize into");
			return;
		}

		final Class<?> cls = instance.getClass();

		for (final Field field : cls.getFields()) {
			trySerializeField(instance, field, object);
		}

		for (final Method method : cls.getMethods()) {
			if (method.isAnnotationPresent(SerializationEventHandler.class)) {
				try {
					method.invoke(null, instance, object);
				} catch (final InvocationTargetException | IllegalAccessException e) {
					Logger.warn("Failed to run class serialization handler:");
					Logger.error(e);
				}
			}
		}
	}

	/**
	 * Deserialize a JsonElement into a given object instance.
	 */
	@Override
	public Object deserialize (@Nullable final Object instance, final JsonElement element) {
		if (instance == null) {
			Logger.warn("Not instantiated");
			return null;
		}

		if (element == null || !element.isJsonObject()) {
			Logger.warn("Json value must be an object. Using base instance.");
			return instance;
		}

		final Class<?> cls = instance.getClass();

		for (final Field field : cls.getFields()) {
			tryDeserializeField(field, instance, element.getAsJsonObject());
		}

		for (final Method method : cls.getMethods()) {
			if (method.isAnnotationPresent(DeserializationEventHandler.class)) {
				try {
					method.invoke(null, instance, element.getAsJsonObject());
				} catch (final InvocationTargetException | IllegalAccessException e) {
					Logger.warn("Failed to run class serialization handler:");
					Logger.error(e);
				}
			}
		}

		return instance;
	}

	/**
	 * Deserialize a field into the instance object
	 */
	private static void trySerializeField (final Object instance, final Field field, JsonObject containingObject) {
		Logger.scopes.push(field.getName());

		final IFieldSerializationHandler<Object> serializer = getFieldSerializer(field);
		if (serializer != null) {
			final String jsonFieldName = CaseConversion.toSnakeCase(field.getName());
			try {

				final Object value = field.get(instance);

				JsonElement serializedValue = JsonNull.INSTANCE;

				if (value == null) {
					final boolean isPrimitive = field.getType().isPrimitive();
					if (!field.isAnnotationPresent(Nullable.class)) {
						throw new Exception("Recieved null, field cannot be null.");

					} else if (isPrimitive) {
						throw new Exception("Recieved null, primitive fields cannot be null.");
					}
				} else {
					serializedValue = serializer.serialize(field.getType(), value);
					if (serializedValue == null) {
						throw new Exception("Cannot serialize null");
					}
				}

				containingObject.add(jsonFieldName, serializedValue);

			} catch (final Exception e) {
				final boolean isNormalException = e.getClass() == Exception.class;
				Logger.warn("Could not serialize field: " + (isNormalException ? e.getMessage() : ""));
				if (!isNormalException) {
					Logger.error(e);
				}
			}
		}

		Logger.scopes.pop();
	}

	/**
	 * Deserialize a field into the instance object
	 */
	private static void tryDeserializeField (final Field field, final Object instance, JsonObject containingObject) {
		Logger.scopes.push(field.getName());

		final IFieldDeserializationHandler<Object> deserializer = getFieldDeserializer(field);
		if (deserializer != null) {
			final String jsonFieldName = CaseConversion.toSnakeCase(field.getName());
			try {
				final JsonElement jsonValue = containingObject.get(jsonFieldName);
				Object deserializedValue = null;

				if (jsonValue == null || jsonValue.isJsonNull()) {
					final boolean isPrimitive = field.getType().isPrimitive();
					if (!field.isAnnotationPresent(Nullable.class)) {
						throw new Exception("Recieved null, field cannot be null.");

					} else if (isPrimitive) {
						throw new Exception("Recieved null, primitive fields cannot be null.");
					}
				} else {
					deserializedValue = deserializer.deserialize(field.getType(), jsonValue);
					if (deserializedValue == null && !field.isAnnotationPresent(Nullable.class)) {
						throw new Exception("Recieved null, field cannot be null.");
					}
				}

				field.set(instance, deserializedValue);

			} catch (final Exception e) {
				final boolean isNormalException = e.getClass() == Exception.class;
				Logger.warn("Could not deserialize field: " + (isNormalException ? e.getMessage() : ""));
				if (!isNormalException) {
					Logger.error(e);
				}
			}
		}

		Logger.scopes.pop();
	}


	/**
	 * Instantiate a class. Returns null if it's unable to be instantiated.
	 */
	@Override
	@Nullable
	public Object instantiate (final Class<?> cls) {
		Object instance = null;
		try {
			instance = cls.newInstance();
		} catch (final InstantiationException | IllegalAccessException e) {
			Logger.warn("Could not instantiate: " + e);
		}

		return instance;
	}

	/**
	 * Gets the deserialization handler for a field, returns null if the field is not serializable, or the deserializer errors
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	private static IFieldSerializationHandler<Object> getFieldSerializer (final Field field) {
		final Serialized serializableClassAnnotation = field.getAnnotation(Serialized.class);
		if (serializableClassAnnotation == null) return null;

		@SuppressWarnings("rawtypes")
		Class<? extends IFieldSerializationHandler> serializerClass = serializableClassAnnotation.serializer();
		// use "value" if that's set but the deserializer isn't
		if (serializerClass == DefaultFieldSerializer.class && serializableClassAnnotation
			.value() != DefaultFieldSerializer.class) {
			serializerClass = (Class<DefaultFieldSerializer>) serializableClassAnnotation.value();
		}

		try {
			return serializerClass.newInstance();

		} catch (final InstantiationException | IllegalAccessException e) {
			Logger.warn("Unable to instantiate serializer");
			Logger.error(e);
			return null;
		}
	}

	/**
	 * Gets the deserialization handler for a field, returns null if the field is not serializable, or the deserializer errors
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	private static IFieldDeserializationHandler<Object> getFieldDeserializer (final Field field) {
		final Serialized serializableClassAnnotation = field.getAnnotation(Serialized.class);
		if (serializableClassAnnotation == null) return null;

		@SuppressWarnings("rawtypes")
		Class<? extends IFieldDeserializationHandler> deserializerClass = serializableClassAnnotation.deserializer();
		// use "value" if that's set but the deserializer isn't
		if (deserializerClass == DefaultFieldSerializer.class && serializableClassAnnotation
			.value() != DefaultFieldSerializer.class) {
			deserializerClass = (Class<DefaultFieldSerializer>) serializableClassAnnotation.value();
		}

		try {
			return deserializerClass.newInstance();

		} catch (final InstantiationException | IllegalAccessException e) {
			Logger.warn("Unable to instantiate deserializer");
			Logger.error(e);
			return null;
		}
	}
}