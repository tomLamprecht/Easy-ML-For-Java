package de.fhws.ai.utility;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class WarningLogger
{
	public static Logger createWarningLogger(String className) {
		Logger logger = Logger.getLogger(className);
		logger.setLevel(Level.WARNING);
		return logger;
	}

}
