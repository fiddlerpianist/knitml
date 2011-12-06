package com.knitml.validation.visitor.instruction.impl;

import java.util.Map;
import java.util.WeakHashMap;

import com.knitml.validation.visitor.instruction.NameResolver;
import com.knitml.validation.visitor.instruction.Visitor;



public class DefaultNameResolver implements NameResolver {
	
	private static final String DEFAULT_PACKAGE_NAME = "com.knitml.validation.visitor.instruction.model"; //$NON-NLS-1$
	private String packageName = DEFAULT_PACKAGE_NAME;
	private Map<String, Class<Visitor>> cache = new WeakHashMap<String, Class<Visitor>>();
	
	public DefaultNameResolver() {
	}
	
	public DefaultNameResolver(String packageName) {
		this.packageName = packageName;
	}
	
	/**
	 * @see com.knitml.validation.visitor.instruction.NameResolver#findVisitingClassFromClassName(java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	public Class<Visitor> findVisitingClassFromClassName(Object object) throws ClassNotFoundException {
		Class<Visitor> visitor = cache.get(object.getClass().getName());
		if (visitor == null) {
			visitor = (Class<Visitor>)Class.forName(packageName + "." + object.getClass().getSimpleName() + "Visitor"); //$NON-NLS-1$ //$NON-NLS-2$
			cache.put(object.getClass().getName(), visitor);
		}
		return visitor; 
	}
	
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
