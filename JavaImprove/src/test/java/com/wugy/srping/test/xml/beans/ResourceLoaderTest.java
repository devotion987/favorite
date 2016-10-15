package com.wugy.srping.test.xml.beans;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;

import com.wugy.spring.xml.ioc.beans.io.Resource;
import com.wugy.spring.xml.ioc.beans.io.ResourceLoader;

public class ResourceLoaderTest {

	@Test
	public void testloader() {
		ResourceLoader resourceLoader = new ResourceLoader();
		Resource resource = resourceLoader.getResource("ioc.xml");
		try {
			InputStream inStream = resource.getInputStream();
			Assert.assertNotNull(inStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
