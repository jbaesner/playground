/*
 * Copyright 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.playground;

import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import javax.ejb.ConcurrencyManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class SingletonDispatcherBeanFactory {

	private static SingletonDispatcherBeanFactory instance;

	private static InitialContext initialContext;

	private AtomicReference<MessageDispatcher> beanDispatcher = new AtomicReference<MessageDispatcher>();
	private AtomicReference<MessageDispatcher> containerDispatcher = new AtomicReference<MessageDispatcher>();

	static {
		try {
			SingletonDispatcherBeanFactory.instance = new SingletonDispatcherBeanFactory();

			Properties props = new Properties();
			props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			initialContext = new InitialContext(props);

		} catch (NamingException e) {
			throw new IllegalStateException(e);
		}
	}

	public static SingletonDispatcherBeanFactory getInstance() {
		return SingletonDispatcherBeanFactory.instance;
	}

	public MessageDispatcher getService(ConcurrencyManagementType type) {

		try {
			MessageDispatcher existingDispatcher;

			switch (type) {
			
			case BEAN:
				existingDispatcher = beanDispatcher.get();

				if (existingDispatcher != null) {
					return existingDispatcher;
				}

				// no beanDispatcher created yet, let's do it...
				final MessageDispatcher newBeanDispatcher = (MessageDispatcher) initialContext.lookup(
						"ejb:playground/playground-ejb/BeanDispatcherBean!org.jboss.playground.MessageDispatcher");

				if (!beanDispatcher.compareAndSet(null, newBeanDispatcher)) {
					// another thread was faster, return that value
					return beanDispatcher.get();
				}

				return newBeanDispatcher;

			case CONTAINER:

				existingDispatcher = containerDispatcher.get();

				if (existingDispatcher != null) {
					return existingDispatcher;
				}

				// no containerDispatcher created yet, let's do it...
				final MessageDispatcher newContainerDispatcher = (MessageDispatcher) initialContext.lookup(
						"ejb:playground/playground-ejb/ContainerDispatcherBean!org.jboss.playground.MessageDispatcher");

				if (!containerDispatcher.compareAndSet(null, newContainerDispatcher)) {
					// another thread was faster, return that value
					return containerDispatcher.get();
				}
				return newContainerDispatcher;
			default:
				throw new IllegalStateException("Type '" + type + "' not handled");
			}
		} catch (NamingException e) {
			throw new RuntimeException("Unable to lookup 'MessageDispatcher' of type '" + type + "'", e);
		}
	}
}
