package org.turter.musiccatalogue;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;
import org.turter.musiccatalogue.config.PersistenceConfig;
import org.turter.musiccatalogue.config.ValidationConfig;
import org.turter.musiccatalogue.config.WebConfig;

public class MusicCatalogueApplication extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {
                PersistenceConfig.class,
                ValidationConfig.class
        };
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] {
                WebConfig.class
        };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
