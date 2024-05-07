package controller;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import scheduler.CardDeadlineScheduler;

@Path("/scheduler")
public class SchedulerResource {

    @EJB
    private CardDeadlineScheduler scheduler;

    @GET
    @Path("/trigger")
    public Response triggerScheduler() {
        scheduler.checkCardDeadlines();
        return Response.ok("Scheduler triggered successfully").build();
    }
}
