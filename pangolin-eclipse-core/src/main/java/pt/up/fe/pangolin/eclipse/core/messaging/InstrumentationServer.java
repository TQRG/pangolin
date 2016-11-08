package pt.up.fe.pangolin.eclipse.core.messaging;

import java.io.IOException;
import java.net.ServerSocket;

import pt.up.fe.pangolin.core.messaging.Server;
import pt.up.fe.pangolin.eclipse.core.messaging.ServiceHandler.ServiceHandlerFactory;

public class InstrumentationServer extends Server {
	
	public InstrumentationServer() throws IOException {
		super(new ServerSocket(0), new ServiceHandlerFactory());
	}

}
