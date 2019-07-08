/**
 * 
 */
package com.russ.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

import com.ansys.cluster.monitor.settings.SGE_MonitorProp;

/**
 * @author rmartine
 *
 */
public class FileStructure {

	private String sourceClass = this.getClass().getName();
	private final Logger logger = Logger.getLogger(sourceClass);
	private SGE_MonitorProp monitorProp = null;

	public FileStructure(SGE_MonitorProp monitorProp) {

		this.monitorProp = monitorProp;

	}

	public void checkAndCreate(String strFilePath, boolean create) throws IOException {

		Path filePath = Paths.get(strFilePath);
		checkAndCreate(filePath, create);
	}

	public void checkAndCreate(Path filePath, boolean create) throws IOException {
		logger.fine("Checking for the existance of " + filePath);

		if (!Files.exists(filePath)) {

			logger.info(filePath + " does not exist.");

			if (create) {

				Files.createDirectories(filePath);
				logger.info("Created " + filePath);

			} else {

				logger.fine("Not creating " + filePath);
			}

		} else {

			logger.fine(filePath + " exists.");

		}

	}

	public void FileStructureCheck() throws IOException {

		Path filePath = Paths.get(monitorProp.getLogFileHandlerPattern());
		checkAndCreate(filePath.getParent(), true);
		checkAndCreate(monitorProp.getDirEtc(), true);

	}

}
