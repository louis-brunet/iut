package fr.iutfbleau.zerotohero.registries;

import java.util.LinkedList;
import java.util.List;

public class Registry<T extends Registerable<P>, P extends Properties> {
	private RegistryList<T, P>[] classes;
	
	@SafeVarargs
	public <Type extends RegistryList<T, P>> Registry(Type... classes) {
		this.classes = classes;
	}
	
	public void register(String itemId, P properties) {
		for (RegistryList<T, P> clazz : classes) {
			clazz.getList(clazz).forEach((element) -> {
				if (element.getId().contentEquals(itemId)) {
					element.setProperties(properties);
					System.out.println("Info : "+properties.getName()+" was registered.");
				}
			});
		}
	}
	
	public T getRegisterable(P properties) {
		return getRegisterable(properties.getId());
	}
	
	public T getRegisterable(String name) {
		for (RegistryList<T, P> clazz : classes) {
			for (T element : clazz.getList(clazz)) {
				if (element.getId().contentEquals(name))
					return element;
			};
		}
		return null;
	}
	
	public P getProperties(Registerable<P> item) {
		return getProperties(item.getId());
	}
	
	public P getProperties(String id) {
		for (RegistryList<T, P> clazz : classes) {
			for (T element : clazz.getList(clazz)) {
				if (element.getId().contentEquals(id))
					return element.getProperties();
			};
		}
		return null;
	}

	public List<P> getAllProperties() {
		List<P> res = new LinkedList<>();

		for (RegistryList<T, P> clazz : classes) {
//			res.addAll(clazz.getList(clazz));
			for (T element : clazz.getList(clazz)) {
				res.add(element.getProperties());
			};
		}

		return res;
	}
	
	public RegistryList<T, P>[] getClasses() {
		return classes;
	}
}
