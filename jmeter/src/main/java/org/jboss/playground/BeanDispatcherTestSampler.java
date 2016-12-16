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

import javax.ejb.ConcurrencyManagementType;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;

public class BeanDispatcherTestSampler extends AbstractJavaSamplerClient {

	private static final Logger log = LoggingManager.getLoggerForClass();

	public SampleResult runTest(JavaSamplerContext context) {

		final String thread = Thread.currentThread().getName();

		SampleResult results = new SampleResult();

		SingletonDispatcherBeanFactory factory = SingletonDispatcherBeanFactory.getInstance();

		MessageDispatcher service = factory.getService(ConcurrencyManagementType.BEAN);

		int count = context.getIntParameter("count");

		results.sampleStart();

		log.debug("[" + thread + "], called with a count of [" + count + "]");

		service.execute(count);

		results.sampleEnd();
		return results;
	}

	@Override
	public Arguments getDefaultParameters() {
		Arguments args = new Arguments();
		args.addArgument("count", "10");
		return args;
	}
}