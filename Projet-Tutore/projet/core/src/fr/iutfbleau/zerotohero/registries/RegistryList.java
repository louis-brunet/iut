package fr.iutfbleau.zerotohero.registries;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class RegistryList<T extends Registerable<P>, P extends Properties> {
	private Class<T> type;
	
	public RegistryList(Class<T> type) {
		this.type = type;
	}
	
	@SuppressWarnings("unchecked")
	protected List<T> getList(Object clazz) {
		if (!(clazz instanceof RegistryList))
			throw new IllegalArgumentException();
		
		Field[] declaredFields = clazz.getClass().getDeclaredFields();
		List<Field> staticFields = new ArrayList<Field>();
		List<T> list = new ArrayList<T>();
		
		for (Field field : declaredFields) {
		    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
		        staticFields.add(field);
		    }
		}
		
		staticFields.forEach((field) -> {
			Object element = null;
			try {
				element = field.get(this);
			} catch (IllegalArgumentException|IllegalAccessException e) {
				e.printStackTrace();
			}
			
			if (element == null)
				System.err.println("Warning : "+field.getName()+" is null, it will not be registered !");
			else {
				if (type.isAssignableFrom(element.getClass())) {
					list.add((T) element);
				}
			}
		});
		
		return list;
	}
	
	public T fromId(String id) {
		for (T registered : getList(this)) {
			if (registered.getId() == id)
				return registered;
		}
		return null;
	}
}
