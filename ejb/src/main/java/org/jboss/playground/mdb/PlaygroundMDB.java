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
package org.jboss.playground.mdb;

import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.security.PermitAll;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jbaesner
 */
@MessageDriven(name = "PlaygroundMDB", activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "/queue/PlaygroundQueue"),
		@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
		@ActivationConfigProperty(propertyName = "maxSession", propertyValue = "50"),
		@ActivationConfigProperty(propertyName = "useDLQ", propertyValue = "false") })
@PermitAll
public class PlaygroundMDB implements MessageListener {

	Logger logger = LoggerFactory.getLogger(PlaygroundMDB.class);

	@PostConstruct
	public void initialize() {
		logger.info("PlaygroundMDB <PostConstruct>");
	}

	@PreDestroy
	public void cleanup() {
		logger.info("PlaygroundMDB <PreDestroy>");
	}

	@Override
	public void onMessage(final Message msg) {
		try {
			final ObjectMessage objectMessage = (ObjectMessage) msg;
			String str = (String) objectMessage.getObject();
			logger.info("PlaygroundMDB received '{}'", str);
			TimeUnit.MICROSECONDS.sleep(60);
		} catch (final Exception e) {
			throw new RuntimeException("An unexpected Exception occured.", e);
		}
	}
}
