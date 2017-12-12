/*
 * #%L
 * SciJava SLF4J-based logging implementation.
 * %%
 * Copyright (C) 2013 - 2017 Board of Regents of the University of
 * Wisconsin-Madison.
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

import org.scijava.log.AbstractLogService;
import org.scijava.log.DefaultUncaughtExceptionHandler;
import org.scijava.log.LogLevel;
import org.scijava.log.LogMessage;
import org.scijava.plugin.Plugin;
import org.scijava.service.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logging service implemented using <a href="http://www.slf4j.org/">SLF4J</a>.
 * 
 * @author Curtis Rueden
 */
@Plugin(type = Service.class)
public final class SLF4JLogService extends AbstractLogService
{

	private Logger logger;

	// -- SLF4JLogService methods --

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(final Logger logger) {
		this.logger = logger;
	}

	// -- AbstractLogService methods --

	@Override
	protected void messageLogged(LogMessage message) {
		logSlf4j(message.level(), format(message), message.throwable());
	}

	// -- Service methods --

	@Override
	public void initialize() {
		// HACK: Dirty, because every time a new SciJava context is created with an
		// SLF4JLogService, it will "steal" the default exception handling.
		DefaultUncaughtExceptionHandler.install(this);

		logger = LoggerFactory.getLogger(SLF4JLogService.class);
	}

	// -- Helper methods --

	private String format(LogMessage message) {
		String source = message.source().toString();
		String text = message.text();
		if (source.isEmpty())
			return text;
		else
			return source + " - " + text;
	}

	private void logSlf4j(int level, String text, Throwable t) {
		switch (level) {
			case LogLevel.ERROR:
				logger.error(text, t);
				return;
			case LogLevel.WARN:
				logger.warn(text, t);
				return;
			case LogLevel.INFO:
				logger.info(text, t);
				return;
			case LogLevel.DEBUG:
				logger.debug(text, t);
				return;
			case LogLevel.TRACE:
				logger.trace(text, t);
				return;
			default:
				if (level <= LogLevel.ERROR)
					logger.error(text, t);
				else
					logger.trace(text, t);
		}
	}

	private String s(final Object o) {
		return o == null ? null : o.toString();
	}
}
