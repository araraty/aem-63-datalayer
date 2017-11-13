package com.adobe.people.ararat.datalayer.core.utils;

import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageUtils {
//    @Reference
//    private static ResourceResolverFactory resourceResolverFactory;
//
    private static final String PN_CUSTOM_PAGENAME = "analyticsPageName";
    private static final Logger log = LoggerFactory.getLogger(PageUtils.class);

    public PageUtils(){}

    public static final String pageAnalyticsPathFromPage(Page page)
    {
        String pageName = "";
        if(page != null) {
            log.info("PageUtils:pageAnalyticsPathFromPage starting {}", page.getName());
            ValueMap properties = page.getProperties();
            pageName = properties.get(PN_CUSTOM_PAGENAME, null);
            log.info("PageUtils:pageAnalyticsPathFromPage found a pageName {}", pageName);
        }
        return pageName;
    }
}
