package org.wotmud.service.api;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by lenore on 25/02/17.
 */
public class MainFinal {

	private static final int BUF_SIZE = 1024;
	private static BufferedReader buf;
	private static OutputStreamWriter osw;

	public void main(String[] args) throws Exception {
		Socket socket;
		DataInputStream input;


		BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));


		try {

			socket = new Socket("localhost", 1500);

			input = new DataInputStream(socket.getInputStream());
			osw = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");


			buf = new BufferedReader(new InputStreamReader(input));
			read();
			String sentence = "";
			while (!"#".equals(sentence)) {
				System.out.println("\n==> Enter your message (# to end connection):");
				sentence = inFromUser.readLine();
				write(sentence);

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

	private  void write(String s) throws IOException {
		System.out.println("Client: " + s);
		osw.write(s + "\0");
		osw.flush();
	}

	private  String read() throws IOException {
		char[] responseLine = new char[BUF_SIZE];
		buf.read(responseLine);
		String result = new String(responseLine).trim();
		System.out.println("Server: " + result);
		return result;
	}

}