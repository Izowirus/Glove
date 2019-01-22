package pl;

import akka.actor.AbstractActor;

public abstract class LoggerActor extends AbstractActor {

    @Override
    public void preStart() throws Exception {
        System.out.println(this.getClass().getSimpleName() + " started");
    }

    @Override
    public void postStop() throws Exception {
        System.out.println(this.getClass().getSimpleName() + " stopped");
    }
}
