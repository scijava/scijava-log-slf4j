/*-
 * #%L
 * SciJava SLF4J-based logging implementation.
 * %%
 * Copyright (C) 2013 - 2022 SciJava developers.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * #L%
 */
package org.scijava.log.slf4j;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import org.junit.Before;
import org.junit.Test;
import org.scijava.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SLF4JLogServiceTest {

	private SLF4JLogService log;
	private RecordingAppender recorder;
	private final String MESSAGE_TEXT = "Message logged as part of a unit test";

	@Before
	public void before() {
		Context context = new Context(SLF4JLogService.class);
		log = context.service(SLF4JLogService.class);
		recorder = new RecordingAppender(log.getLogger());
	}

	@Test
	public void testMessageLogging() {
		recorder.clear();
		log.info(MESSAGE_TEXT);
		ILoggingEvent event = recorder.getLastEvent();
		assertTrue(event.getMessage().contains(MESSAGE_TEXT));
		assertEquals(Level.INFO, event.getLevel());
	}

	@Test
	public void testSubLogger() {
		recorder.clear();
		String source = "one:two:three";
		log.subLogger(source).warn(MESSAGE_TEXT);
		ILoggingEvent event = recorder.getLastEvent();
		assertTrue(event.getMessage().contains(source));
	}

	@Test
	public void testLoggingThrowable() {
		recorder.clear();
		Throwable throwable = new Throwable("Throwable logged as part of a unit test");
		log.info(throwable);
		ILoggingEvent event = recorder.getLastEvent();
		assertEquals(throwable.getMessage(), event.getThrowableProxy().getMessage());
	}

	private static class RecordingAppender {

		private ILoggingEvent lastEvent = null;

		public RecordingAppender(Logger slf4jLogger) {
			LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
			Appender<ILoggingEvent> appender = new AppenderBase<ILoggingEvent>() {
				@Override
				protected void append(ILoggingEvent event) {
					lastEvent = event;
				}
			};
			appender.setContext(lc);
			appender.setName("test_recorder");
			appender.start();
			lc.getLogger(slf4jLogger.getName()).addAppender(appender);
		}

		public void clear() {
			lastEvent = null;
		}

		public ILoggingEvent getLastEvent() {
			if(!hasLastEvent())
				throw new IllegalStateException("No log message recorded.");
			return lastEvent;
		}

		private boolean hasLastEvent() {
			return lastEvent != null;
		}
	}
}
