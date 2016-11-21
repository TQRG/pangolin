package pt.up.fe.pangolin.eclipse.core.messaging;

import pt.up.fe.pangolin.core.events.EventListener;
import pt.up.fe.pangolin.core.messaging.Service;
import pt.up.fe.pangolin.eclipse.core.Configuration;

public class ServiceHandler implements Service {
	
	private EventListener eventListener;
	
	public ServiceHandler(EventListener eventListener) {
		this.eventListener = eventListener;
	}

	@Override
	public EventListener getEventListener() {
		return eventListener;
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
			return new ServiceHandler(Configuration.get().getEventListener());
		}
	}

}
