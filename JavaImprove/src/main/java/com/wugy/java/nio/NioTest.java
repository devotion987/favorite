package com.wugy.java.nio;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

public class NioTest {

	private static final int BUFFER_SIZE = 1024;

	/**
	 * 这段程序将Server端的监听连接请求的事件和处理的事件放在一个线程中，但是在事件应用中，通常会把它们放在两个线程中：<br>
	 * 一个线程专门负责监听客户端的连接请求，而且是以阻塞方式执行的；另一个线程专门负责处理请求，这个专门负责请求的线程才会<br>
	 * 真正采用NIO方式，像WEB服务器Tomcat和Jetty都是使用这个处理方式。
	 */
	@Test
	public void testSelector() {
		ByteBuffer byteBuffer = ByteBuffer.allocate(BUFFER_SIZE);
		try {
			Selector selector = Selector.open();
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.configureBlocking(false);
			ssc.socket().bind(new InetSocketAddress(8080));
			ssc.register(selector, SelectionKey.OP_ACCEPT);
			while (true) {
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> it = selectedKeys.iterator();
				while (it.hasNext()) {
					SelectionKey key = it.next();
					if ((key.readyOps() & SelectionKey.OP_ACCEPT) == SelectionKey.OP_ACCEPT) {
						ServerSocketChannel ssChannel = (ServerSocketChannel) key.channel();
						SocketChannel sc = ssChannel.accept();
						sc.configureBlocking(false);
						sc.register(selector, SelectionKey.OP_READ);
					} else if ((key.readyOps() & SelectionKey.OP_READ) == SelectionKey.OP_READ) {
						SocketChannel sc = (SocketChannel) key.channel();
						while (true) {
							byteBuffer.clear();
							int n = sc.read(byteBuffer);
							if (n <= 0)
								break;
							byteBuffer.flip();
						}
					}
					it.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * FileChannel.map将文件按照一定大小块映射为内存区域，当程序访问这个内存区域时将直接操作这个文件数据，<br>
	 * 这种方式省去了数据从内核空间向用户空间复制的损耗
	 */
	@Test
	public void testFileChannelMap() {
		String fileName = "test.db";
		long fileLength = new File(fileName).length();
		int bufferCount = 1 + (int) (fileLength / BUFFER_SIZE);
		MappedByteBuffer[] buffers = new MappedByteBuffer[bufferCount];
		long remaining = fileLength;
		try {
			for (int i = 0; i < bufferCount; i++) {
				RandomAccessFile randomFile = new RandomAccessFile(fileName, "r");
				buffers[i] = randomFile.getChannel().map(FileChannel.MapMode.READ_ONLY, i * BUFFER_SIZE,
						(int) Math.min(remaining, BUFFER_SIZE));
				remaining -= BUFFER_SIZE;
				System.out.println(randomFile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
