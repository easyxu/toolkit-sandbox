package com.phoenix.common.resource;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public abstract class AbstractResource  implements Resource{

	public boolean exists() {
		// Try file existence: can we find the file in the file system?
		try {
			return getFile().exists();
		}
		catch (IOException ex) {
			// Fall back to stream existence: can we open the stream?
			try {
				InputStream is = getInputStream();
				is.close();
				return true;
			}
			catch (Throwable isEx) {
				return false;
			}
		}
	}

	/**
	 * This implementation always returns <code>false</code>.
	 */
	public boolean isOpen() {
		return false;
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that the resource cannot be resolved to a URL.
	 */
	public URL getURL() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to URL");
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that the resource cannot be resolved to an absolute file path.
	 */
	public File getFile() throws IOException {
		throw new FileNotFoundException(getDescription() + " cannot be resolved to absolute file path");
	}

	/**
	 * This implementation throws a FileNotFoundException, assuming
	 * that relative resources cannot be created for this resource.
	 */
	public Resource createRelative(String relativePath) throws IOException {
		throw new FileNotFoundException("Cannot create a relative resource for " + getDescription());
	}

	/**
	 * This implementation always throws IllegalStateException,
	 * assuming that the resource does not carry a filename.
	 */
	public String getFilename() throws IllegalStateException {
		throw new IllegalStateException(getDescription() + " does not carry a filename");
	}

	/**
	 * This abstract method declaration shadows the method in the Resource interface.
	 * This is necessary to make the <code>toString</code> implementation in this
	 * class work on Sun's JDK 1.3 classic VM, which can't find the method when
	 * executing <code>toString</code> else. Furthermore, <code>getDescription</code>
	 * is also called from <code>equals</code> and <code>hashCode</code>
	 * @see com.phoenix.common.resource.Resource#getDescription()
	 * @see #toString()
	 * @see #equals(Object)
	 * @see #hashCode()
	 */
	public abstract String getDescription();


	/**
	 * This implementation returns the description of this resource.
	 * @see #getDescription()
	 */
	public String toString() {
		return getDescription();
	}

	/**
	 * This implementation compares description strings.
	 * @see #getDescription()
	 */
	public boolean equals(Object obj) {
		return (obj == this ||
		    (obj instanceof Resource && ((Resource) obj).getDescription().equals(getDescription())));
	}

	/**
	 * This implementation returns the description's hash code.
	 * @see #getDescription()
	 */
	public int hashCode() {
		return getDescription().hashCode();
	}
}
