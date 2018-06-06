package com.adobe.people.ararat.datalayer.core.utils;

import com.day.cq.wcm.api.Page;
import org.apache.jackrabbit.api.security.user.User;
import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.adobe.people.ararat.datalayer.core.utils.CONSTANTS.*;

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
    //User Utils
    public static String getUserHash(User currentUser){
        try {
            if(currentUser != null && !currentUser.getID().equals("anonymous")) {
                return String.valueOf(currentUser.getID().getBytes("UTF-8"));
            }
            else{return "";}
        }catch(Exception e){
            log.error("DataLayer:Exception on currentPage.getName() {}",e.getMessage());
            return "";
        }
    }

    //Page Utils
    public static String getInternalName(Page currentPage){
        try {
            String returnValue =  currentPage.getName();
            if(returnValue!= null){
                return returnValue;
            }else{
                return"";
            }
        }catch(Exception e){
            log.error("DataLayer:Exception on currentPage.getName() {}",e.getMessage());
            return "";
        }
    }
    public static String getVanityUrl(Page currentPage){
        try {
            String returnValue = currentPage.getVanityUrl();
            if(returnValue!= null){
                return returnValue;
            }else{
                return"";
            }
        }catch(Exception e){
            log.error("DataLayer:Exception on currentPage.getVanityUrl() {}",e.getMessage());
            return "";
        }
    }
    public static String getPageTitle(Page currentPage){
        try {
            String returnValue =  currentPage.getPageTitle();
            if(returnValue!= null){
                return returnValue;
            }else{
                return"";
            }
        }
        catch(Exception e){
            log.error("DataLayer:Exception on currentPage.getPageTitle() {}",e.getMessage());
            return "";
        }
    }
    public static String getPageName(Page currentPage) {
        //Set a caution value to be able to indicate homepage in case of an error
        String pageName = DEFAULT_PAGE_NAME;
        try {
            if (currentPage != null) {
                pageName = currentPage.getPath().substring(1).replace('/', ':');
                pageName = pageName.replace("content:", "");
            }
        }
        catch(Exception e){
            log.error("DataLayer:Exception whilst generating pageName {}",e.getMessage());
        }
        //Return the page name with the prefix of the site.
        return SITE_NAME + pageName;
    }
    //SiteName Utils
    public static String getSiteName(Page currentPage){
        String[] pagePaths = currentPage.getPath().split("/");
        if(null!=pagePaths&&pagePaths.length > 1){
            return pagePaths[2];
        }
        return SITE_NAME+DEFAULT_PAGE_NAME;
    }

    public static String getSection(Page currentPage){
        String[] pagePaths = currentPage.getPath().split("/");
        if(null!=pagePaths&&pagePaths.length > 4){
            return pagePaths[4];
        }
        return currentPage.getName();
    }

    public static String getLanguage(Page currentPage){
        String language = currentPage.getLanguage(false).getLanguage();
        if(language==""||language==null){
            return SITE_LANGUAGE;
        }
        return language;
    }

    public static String getLanguageCountry(Page currentPage){
        String languageCountry = currentPage.getLanguage(true).toLanguageTag();
        if(null != languageCountry){
            languageCountry = languageCountry.toLowerCase();
        }
        if(languageCountry==""||languageCountry==null){
            return SITE_COUNTRY;
        }
        return languageCountry;
    }
}
