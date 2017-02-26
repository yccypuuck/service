/**
 * Created by lenore on 23/02/17.
 */
package org.wotmud.service.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

//@Service
public class SocketServiceImpl implements SocketService {

	private final int BUF_SIZE = 1024;
	private String hostname = "localhost";
	private int portnum = 1500;
	private BufferedReader buf;
	private OutputStreamWriter osw;
	Socket socket;

//	@Autowired
	public SocketServiceImpl() throws Exception {
		DataInputStream input;

		try {
			socket = new Socket(hostname, portnum);

			osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

			input = new DataInputStream(socket.getInputStream());
			buf = new BufferedReader(new InputStreamReader(input));

			read();
			String sentence = "Hello";
			for (int i = 0; i < 2; i++) {
				System.out.println("\n==> Enter your message (# to end connection):");
				write(sentence + i);
				read();
			}

			// closing connection
			osw.close();
			input.close();
			socket.close();
		} catch (UnknownHostException e) {
			System.err.println("Don't know about host: hostname");
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	private void write(String s) throws IOException {
		System.out.println("Client: " + s);
		osw.write(s + "\0");
		osw.flush();
	}
	private String read() throws IOException {
		char[] responseLine = new char[BUF_SIZE];
		buf.read(responseLine);
		String result = new String(responseLine).trim();
		System.out.println("Server: " + result) ;
		return result;
	}

}
