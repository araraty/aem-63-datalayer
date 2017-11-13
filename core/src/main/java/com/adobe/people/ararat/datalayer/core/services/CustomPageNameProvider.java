package com.adobe.people.ararat.datalayer.core.services;

import com.day.cq.analytics.sitecatalyst.AnalyticsPageNameContext;
import com.day.cq.analytics.sitecatalyst.AnalyticsPageNameProvider;
import com.day.cq.analytics.sitecatalyst.Framework;
import com.day.cq.wcm.api.Page;
import com.adobe.people.ararat.datalayer.core.utils.PageUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.osgi.framework.Constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.day.cq.analytics.sitecatalyst.AnalyticsPageNameContext.S_PAGE_NAME;
import static com.adobe.people.ararat.datalayer.core.utils.CONSTANTS.SITE_NAME;

/**
 * Default implementation of {@link AnalyticsPageNameProvider} that resolves
 * page title, path or navTitle if mapped in {@link Framework}.
 */

@Service
@Component(metatype = false)
@Properties({
        @Property(name = Constants.SERVICE_DESCRIPTION, value = "Custom Page Name Resolver implementation"),
        @Property(name = Constants.SERVICE_RANKING, intValue = 200, propertyPrivate = false) })
public class CustomPageNameProvider implements AnalyticsPageNameProvider {

    private static final Logger log = LoggerFactory.getLogger(CustomPageNameProvider.class);


	@Override
	public String getPageName(AnalyticsPageNameContext context) {


	    String pageName = null;

        Framework framework = context.getFramework();
        Resource resource = context.getResource();
        Page page = null;
        if (resource != null
                && framework != null
                && framework.mapsSCVariable(S_PAGE_NAME)) {
            String cqVar = framework.getMapping(S_PAGE_NAME);
            page = resource.adaptTo(Page.class);
            log.info("CustomPageNameProvider:Starting Function getPageName for pageName {}", page.getName());
            PageUtils pageUtils = new PageUtils();
            if (page != null && cqVar.equals("pagedata.analyticsPageName")){
                pageName= pageUtils.pageAnalyticsPathFromPage(page);
            }
            log.info("CustomPageNameProvider:getPageName found a pageName {}",pageName);
        }
        if(pageName !=null && pageName.contains("content:")) {
            pageName.replace("content:", "");
        }
        else
        {
            log.info("CustomPageNameProvider:pageName is not analytics Enabled. will calculate based on path {}", page.getName());
            if(page==null){
                page = resource.adaptTo(Page.class);
            }
            pageName = page.getPath().substring(1).replace('/',':');
            pageName = pageName.replace("content:", "");
         }
        return SITE_NAME + pageName;
    }

	@Override
	public Resource getResource(AnalyticsPageNameContext context) {
		String pageName = context.getPageName();
		
		log.info("CustomPageNameProvider:Starting Function getResource for pageName {}", pageName);
		
		ResourceResolver rr = context.getResourceResolver();
		
		if ( ! pageName.startsWith("content")) {
			pageName = "content:" + pageName;
		}
		pageName = "/" + StringUtils.replace(pageName, ":", "/");
		
		log.info("CustomPageNameProvider:getResource Search for page {}", pageName);
		
		Resource resource = rr.resolve(pageName);
		if ( resource.adaptTo(Page.class) != null) {
			log.info("CustomPageNameProvider:getResource Page found");
			return resource;
		} else {
			log.info("CustomPageNameProvider:getResource Page not found");
		}
		
		return null;
	}
}
