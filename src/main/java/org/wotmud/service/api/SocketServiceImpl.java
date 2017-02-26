/**
 * Created by lenore on 23/02/17.
 */
package org.wotmud.service.api;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.*;
import java.net.Socket;

@Service
public class SocketServiceImpl implements SocketService {

	private final int BUF_SIZE = 1024;
	private final int TIMEOUT = 5000;
	private final String hostname = "localhost";
	private final int portnum = 1500;

	private Socket socket;
	private BufferedReader buf;
	private OutputStreamWriter osw;
	private DataInputStream input;

	private Logger logger = LoggerFactory.getLogger(SocketServiceImpl.class);

	@PostConstruct
	public void init() throws InterruptedException {
		boolean connected = false;
		do {
			try {
				logger.info("Connecting to the server {}:{}", hostname, portnum);
				socket = new Socket(hostname, portnum);
				osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				input = new DataInputStream(socket.getInputStream());
				buf = new BufferedReader(new InputStreamReader(input));
				read();

				logger.info("Server {} connected", hostname);
				connected = true;
			} catch (IOException e) {
				logger.warn("Don't know about host: {}, waiting {}ms to reconnect", hostname, TIMEOUT);
				Thread.sleep(TIMEOUT);
			}
		} while (!connected);
	}

	@Override
	public String send(String s) {
		try {
			if (!socket.isConnected()) {
				logger.info("reconnecting to {}:{}", hostname, portnum);
				init();
			}
			logger.info("Sending {} to server", s);
			write(s);
			return read();
		} catch (IOException | InterruptedException e) {
			logger.warn("Sending message to host: {} has failed", hostname);
		}
		return "Error sending the message";
	}

	private void write(String s) throws IOException {
		logger.info("Client: {} ", s);
		osw.write(s + "\0");
		osw.flush();
	}

	private String read() throws IOException {
		char[] responseLine = new char[BUF_SIZE];
		buf.read(responseLine);
		String result = new String(responseLine).trim();
		logger.info("Server: {} ", result);
		return result;
	}

	@PreDestroy
	public void close() throws IOException {
		if (socket.isConnected()) {
			logger.info("Closing connection {}:{}", hostname, portnum);
			send("#");
			osw.close();
			input.close();
			socket.close();
		}
	}

}
