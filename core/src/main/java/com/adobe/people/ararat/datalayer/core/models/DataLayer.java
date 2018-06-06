package com.adobe.people.ararat.datalayer.core.models;


import com.adobe.acs.commons.models.injectors.annotation.AemObject;
import com.adobe.people.ararat.datalayer.core.utils.PageUtils;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.jackrabbit.api.security.user.User;
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
                                    .add("siteName", PageUtils.getSiteName(currentPage))
                                    .add("section", PageUtils.getSection(currentPage))
                                    .add("pageName", PageUtils.getPageName(currentPage))
                                    .add("title", PageUtils.getPageTitle(currentPage))
                                    .add("internalPageName", PageUtils.getInternalName(currentPage))
                                    .add("vanityURL", PageUtils.getVanityUrl(currentPage))
                                    .build())
                            .add("attributes", Json.createObjectBuilder()
                                    .add("languageCountry", PageUtils.getLanguageCountry(currentPage))
                                    .add("language",PageUtils.getLanguage(currentPage))
                                    .build())
                            .build()
                    )
                    .add("user", Json.createObjectBuilder()
                            .add("profile", Json.createObjectBuilder()
                                    .add("attributes", Json.createObjectBuilder()
                                            .add("loggedIn", (user != null && !user.getID().equals("anonymous")))
                                            .add("username", PageUtils.getUserHash(user)))
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


}
