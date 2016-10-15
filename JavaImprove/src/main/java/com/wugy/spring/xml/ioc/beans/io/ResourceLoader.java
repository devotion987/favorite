package com.wugy.spring.xml.ioc.beans.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class ResourceLoader {

	public Resource getResource(String location) {
		URL resource = this.getClass().getClassLoader().getResource(location);
		return new UrlResource(resource);
	}

	private class UrlResource implements Resource {

		private final URL url;

		public UrlResource(URL resource) {
			url = resource;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			URLConnection urlConnection = url.openConnection();
			urlConnection.connect();
			return urlConnection.getInputStream();
		}

	}
}
