/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.playground;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Joerg Baesner
 */
public class JBossClientLogging {

	private static final Handler HANDLER = new ConsoleHandler();
	private static final Formatter FORMATTER = new PlaygroundFormatter();
	
	private static Level GLOBAL_OFF = Level.OFF;
	
	public static void configure() {

		// let the handler use our own fancy playground format...
		HANDLER.setFormatter(FORMATTER);
		
		// disable logging of 'org.xnio'
		Logger.getLogger("org.xnio").addHandler(HANDLER);
		Logger.getLogger("org.xnio").setLevel(GLOBAL_OFF);
		
		// configure logging defined in 'org.jboss.ejb.client.Logs'
		Logger.getLogger("org.jboss.ejb.client").addHandler(HANDLER);
		Logger.getLogger("org.jboss.ejb.client").setLevel(GLOBAL_OFF);

		Logger.getLogger("org.jboss.ejb.client.remoting").addHandler(HANDLER);
		Logger.getLogger("org.jboss.ejb.client.remoting").setLevel(GLOBAL_OFF);

		Logger.getLogger("org.jboss.ejb.client.txn").addHandler(HANDLER);
		Logger.getLogger("org.jboss.ejb.client.txn").setLevel(GLOBAL_OFF);

		Logger.getLogger("org.jboss.remoting").addHandler(HANDLER);
		Logger.getLogger("org.jboss.remoting").setLevel(GLOBAL_OFF);
	}
	
	public static Handler getConsoleHandler() {
		return HANDLER;
	}
}
