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
