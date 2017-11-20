package com.adobe.people.ararat.datalayer.core.models;


import org.apache.jackrabbit.api.security.user.User;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.adobe.acs.commons.models.injectors.annotation.AemObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;

import static com.adobe.people.ararat.datalayer.core.utils.CONSTANTS.*;

@Model(adaptables = {SlingHttpServletRequest.class})
public class DataLayer{
    //single function to return the JSON stream of all the page data layer
    private static final Logger log = LoggerFactory.getLogger(DataLayer.class);

//    @Inject
//    @Via("resource")
//    @Optional
    private String datalayer;

    public String getDatalayer() {
        datalayer = getPageData();
        //TODO: Add user and cart data
        return datalayer;
    }

    //used to get data from the current page i am on such as title etc.
    private Resource resource;

    @Inject
    @Source("sling-object")
    private ResourceResolver resourceResolver;

    //Page object that is also adressable from the test
    @AemObject
    private Page currentPage;

    //Get user related data to the datalayer.
    private User user;


    //Important starting point if we wish to address elements such as Commerce Session ID etc.
    @Inject
    @Self
    private SlingHttpServletRequest slingHttpServletRequest;

    @PostConstruct
    private void init() {
        resource = slingHttpServletRequest.getResource();
        resourceResolver = slingHttpServletRequest.getResourceResolver();
        currentPage = resourceResolver.adaptTo(PageManager.class).getContainingPage(resource);
        user = resourceResolver.adaptTo(User.class);
    }

    /*
    * Function to collect all data on page and create a JSON object representation of the data
    * CEDDL format will be the output stringifiedish
    */
    private String getPageData(){

        JsonObject data;
        log.info("DataLayer:Entry to JsonObject builder for pageData");
        try {
            data = Json.createObjectBuilder()
                    .add("page", Json.createObjectBuilder()
                            .add("pageInfo", Json.createObjectBuilder()
                                    .add("siteName", this.getSiteName(currentPage))
                                    .add("section", this.getSection(currentPage))
                                    .add("pageName", this.getPageName(currentPage))
                                    .add("title", this.getPageTitle(currentPage))
                                    .add("internalPageName", this.getInternalName(currentPage))
                                    .add("vanityURL", this.getVanityUrl(currentPage))
                                    .build())
                            .add("attributes", Json.createObjectBuilder()
                                    .add("languageCountry", this.getLanguageCountry(currentPage))
                                    .add("language",this.getLanguage(currentPage))
                                    .build())
                            .build()
                    )
                    .add("user", Json.createObjectBuilder()
                            .add("profile", Json.createObjectBuilder()
                                    .add("attributes", Json.createObjectBuilder()
                                            .add("loggedIn", (user != null && !user.getID().equals("anonymous")))
                                            .add("username", getUserHash(user)))
                                    .build()
                                )
                            .build()
                        )
                    .build();
            log.info("DataLayer:Finished JsonObject builder for pageData");
        }catch(Exception e)
        {
            log.error("DataLayer:Exception has occurred in getPageData of {}",e.getMessage());
            e.printStackTrace();
            return("");
        }

        return(data.toString());
    }
    //User Utils
    private String getUserHash(User currentUser){
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
    private String getInternalName(Page currentPage){
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
    private String getVanityUrl(Page currentPage){
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
    private String getPageTitle(Page currentPage){
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
    private String getPageName(Page currentPage) {
        //Set a caution value to be able to indicate homepage in case of an error
        String pageName = "aem-63-datalayer";
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

    private String getSiteName(Page currentPage){
        String[] pagePaths = currentPage.getPath().split("/");
        if(null!=pagePaths&&pagePaths.length > 1){
            return pagePaths[2];
        }
        return SITE_NAME+"aem-63-datalayer";
    }

    private String getSection(Page currentPage){
        String[] pagePaths = currentPage.getPath().split("/");
        if(null!=pagePaths&&pagePaths.length > 4){
            return pagePaths[4];
        }
        return currentPage.getName();
    }

    private String getLanguage(Page currentPage){
        String language = currentPage.getLanguage(false).getLanguage();
        if(language==""||language==null){
            return SITE_LANGUAGE;
        }
        return language;
    }

    private String getLanguageCountry(Page currentPage){
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
