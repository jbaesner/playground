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

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joerg Baesner
 */
@PermitAll
public abstract class AbstractDispatcherBean {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Resource
	ConnectionFactory cf;
	@Resource(mappedName = "java:/jms/queue/PlaygroundQueue")
	private Queue playgroundQueue;

	public AbstractDispatcherBean() {
		super();
	}

	public void execute(int num) {
		
		logger.trace("execute start");
		
		String currentThread = Thread.currentThread().getName();

		long t0 = System.currentTimeMillis();

		JMSContext jmsContext = cf.createContext();

		Message message = jmsContext
				.createObjectMessage("a message from thread [" + currentThread + "]");

		JMSProducer producer = jmsContext.createProducer();

		producer.send(playgroundQueue, message);

		jmsContext.close();

		logger.debug("Sending to queue 'PlaygroundQueue' too {}ms, mode '{}'", (System.currentTimeMillis() - t0),
				getMode());
		
		logger.trace("execute start");
	}

	protected abstract String getMode();
}