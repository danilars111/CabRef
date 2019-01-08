package se.kau.cs.serg.cabref.server;

import org.pac4j.core.context.HttpConstants;
import org.pac4j.sparkjava.DefaultHttpActionAdapter;
import org.pac4j.sparkjava.SparkWebContext;
import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

import java.util.HashMap;

public class CabRefHttpActionAdapter extends DefaultHttpActionAdapter {

    private final ThymeleafTemplateEngine templateEngine;

    public CabRefHttpActionAdapter(final ThymeleafTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public Object adapt(int code, SparkWebContext context) {
        if (code == HttpConstants.UNAUTHORIZED) {
        	System.out.println("Unauthorized");
            stop(401, templateEngine.render(new ModelAndView(new HashMap<>(), "error")));
        } else if (code == HttpConstants.FORBIDDEN) {
        	System.out.println("Forbidden");
            stop(403, templateEngine.render(new ModelAndView(new HashMap<>(), "error")));
        } else {
            return super.adapt(code, context);
        }
        return null;
    }
}