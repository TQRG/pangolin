package pt.up.fe.pangolin.core.messaging;

import pt.up.fe.pangolin.core.events.EventListener;

public interface Service {

	EventListener getEventListener ();
    void interrupted ();
    void terminated ();
    
    public interface ServiceFactory {
    	Service create (String id);
    }
}
