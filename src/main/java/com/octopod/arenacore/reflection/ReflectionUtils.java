package com.octopod.arenacore.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtils {
	
	public static List<Field> getFields(Object instance) {
		List<Field> fields = new ArrayList<Field>();
		for(Field f: instance.getClass().getDeclaredFields()) {
			fields.add(f);
		}
		return fields;
	}
	
	public static <T> T setField(T instance, String fieldName, Object value) throws ReflectionException {
		return setField(instance, fieldName, value, true);
	}
	
	public static <T> T setField(T instance, String fieldName, Object value, boolean force) throws ReflectionException{

		Field field = null;
		
		try {
			field = instance.getClass().getDeclaredField(fieldName);
		} catch (NoSuchFieldException e) {
			throw new ReflectionException("The field \"" + fieldName + "\" was not found in class " + instance.getClass().getName());
		} catch (SecurityException e) {
			throw new ReflectionException(e.getMessage());
		}
		
		if(Modifier.isFinal(field.getModifiers())) {
			throw new ReflectionException("The field \"" + fieldName + "\" is final. You can't change this field!");
		}
		
		if(force) {
			try {
				field.setAccessible(true);
			} catch (SecurityException e) {
				throw new ReflectionException("Unable to make the field \"" + fieldName + "\" public");
			}
		}
		
		try {
			field.set(instance, value);
		} catch (IllegalArgumentException e) {
			throw new ReflectionException(
					"Unable to set field \"" + fieldName + "\" to value of type " + value.getClass().getSimpleName() + 
					" (was expecting " +  field.getType().getSimpleName() + ")"
			);
		} catch (IllegalAccessException e) {
			throw new ReflectionException(
					"The field \"" + fieldName + "\" is " + Modifier.toString(field.getModifiers()) + ". You can't change this field!"
			);
		}
		
		return instance;
		
	}

}
