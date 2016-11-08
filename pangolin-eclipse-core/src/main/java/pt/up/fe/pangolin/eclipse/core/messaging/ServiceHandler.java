package pt.up.fe.pangolin.eclipse.core.messaging;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.events.VerboseEventListener;
import pt.up.fe.pangolin.core.messaging.Service;

public class ServiceHandler implements Service {

	@Override
	public EventListener getEventListener() {
		return new VerboseEventListener();
	}

	@Override
	public void interrupted() {
	}

	@Override
	public void terminated() {
	}
	
	public static class ServiceHandlerFactory implements ServiceFactory {
		
		@Override
		public Service create(String id) {
			return new ServiceHandler();
		}
	}

}
