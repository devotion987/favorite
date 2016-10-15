package com.wugy.spring.xml.ioc.beans.io;

import java.io.IOException;
import java.io.InputStream;

public interface Resource {

	InputStream getInputStream() throws IOException;
}
