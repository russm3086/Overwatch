/**
 * 
 */
package com.russ.util.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;

/**
 * @author rmartine
 *
 */
public final class ResourceLoader {
	/**
	 * The name of current class. To be used with the logging subsystem.
	 */
	private final static String sourceClass = ResourceLoader.class.getName();

	/**
	 * The Logger instance. All log messages from this class are handle here.
	 */
	private static Logger logger = Logger.getLogger(sourceClass);

	/**
	 * @throws IOException
	 * 
	 */

	public static URL load(String path) throws IOException {
		logger.entering(sourceClass, "load", path);
		logger.finer("Loading " + path);
		URL input = ResourceLoader.class.getResource(path);

		if (input == null) {

			input = ResourceLoader.class.getResource("/" + path);

			if (input == null)
				throw new IOException("Could not find " + path);

		}

		logger.finer("Loaded " + path);
		logger.exiting(sourceClass, "load", input);
		return input;
	}

	public static Path readFile(String filePath) throws IOException, URISyntaxException {

		Path file = Paths.get(filePath);
		return getFile(file, false);
	}

	public static File readFile(Path filePath) throws IOException, URISyntaxException {

		Path path = getFile(filePath, false);
		return path.toFile();
	}

	public static Path writeFile(Path filePath, boolean overWrite) throws IOException {
		String errMsg = null;

		if (Files.exists(filePath)) {

			if (Files.isWritable(filePath)) {

				if (Files.exists(filePath) && !overWrite) {
					errMsg = "Error with file: " + filePath + " File exist: " + (Files.exists(filePath))
							+ " cannot over write ";
				}

			} else {

				errMsg = "Error with file: " + filePath + " File exist: "
						+ (Files.exists(filePath) + " File writable: " + Files.isWritable(filePath));
			}

			if (errMsg != null)
				throw new IOException(errMsg);
		}

		return filePath;

	}

	public static Path getFile(Path filePath, boolean write) throws IOException, URISyntaxException {
		logger.entering(sourceClass, "load", filePath);
		logger.finer("Loading " + filePath);
		Path file = null;

		if (!(Files.exists(filePath))) {

			URL newPath = load(filePath.toString());
			filePath = Paths.get(newPath.toURI());

		}

		logger.finer("File " + filePath + " has been found");

		if (write) {
			if (Files.isReadable(filePath) && Files.isWritable(filePath)) {

				file = filePath;
			}

		} else if (Files.isReadable(filePath)) {

			file = filePath;
		}

		if (file == null) {

			throw new IOException(
					"Error with file: " + filePath + " File exist: " + (Files.exists(filePath) + " File readable: "
							+ Files.isReadable(filePath) + " File writable: " + Files.isWritable(filePath)));
		}

		logger.finer("Loaded " + filePath);
		logger.exiting(sourceClass, "load", filePath);
		return file;
	}

	public static void saveSerial(Object object, String filePath) throws IOException {

		FileOutputStream fos = null;
		ObjectOutputStream out = null;

		fos = new FileOutputStream(filePath);
		out = new ObjectOutputStream(fos);
		out.writeObject(object);

		out.close();
	}

	public static Object loadSerial(String filePath) throws IOException, ClassNotFoundException {

		FileInputStream fis = null;
		ObjectInputStream in = null;

		fis = new FileInputStream(filePath);
		in = new ObjectInputStream(fis);
		Object object = in.readObject();
		in.close();

		return object;
	}

}
