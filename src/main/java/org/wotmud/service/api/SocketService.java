package org.wotmud.service.api;

import java.io.IOException;

/**
 * Created by lenore on 26/02/17.
 */
public interface SocketService {

	String send(String s);

	void close() throws IOException;

}
