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

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Joerg Baesner
 */
public class GreeterEJBClient {

	private static final Logger logger = Logger.getLogger("org.jboss.playground"); 
	
	static {
		// initialize JDK logging
		JBossClientLogging.configure();
		
		logger.addHandler(JBossClientLogging.getConsoleHandler());
		logger.setLevel(Level.FINEST);
		logger.setUseParentHandlers(false);
	}
	
    public static void main(String[] args) throws Exception {
        invokeBean();
    }

    private static void invokeBean() throws NamingException {
    	final RemoteGreeter greeter = lookup();

        // call a method
        logger.info(greeter.sayHello("World"));
    }

   
    private static RemoteGreeter lookup() throws NamingException {

    	final Properties jndiProperties = new Properties();
    	jndiProperties.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
    	jndiProperties.put("jboss.naming.client.ejb.context", true);
    	jndiProperties.put("java.naming.factory.initial", "org.jboss.naming.remote.client.InitialContextFactory");
    	jndiProperties.put("java.naming.provider.url", "http-remoting://localhost:8080");
    	jndiProperties.put(Context.SECURITY_PRINCIPAL,"guest");  // add this user as ApplicationUser via script add-user.sh
    	jndiProperties.put(Context.SECURITY_CREDENTIALS, "guest");

    	jndiProperties.put("jboss.naming.client.connect.options.org.xnio.Options.SASL_POLICY_NOPLAINTEXT", "false");

    	
    	
        final Context ctx = new InitialContext(jndiProperties);
    	
        return (RemoteGreeter) ctx.lookup("custom/jndi/SpecialJndiLocation");
    }
    
}