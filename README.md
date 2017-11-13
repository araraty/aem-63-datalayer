# aem-63-datalayer
AEM 6.3 Datalayer sample implementation with matching AnalyticsPageNameProvider implementation to support the AEM import and content insight support when using a custom pageName.
##Data Layer
A sling Model implementation supporting page and user details to show the ability to use resource and request in relation to the page information.
Works with JSONP implementation javax.json.JsonObject to align with the removal of JSONObject from AEM.
##AnalyticsPageNameProvider implementation
This changes the page name mechanism.

##Building the project
Will build and deploy using maven.
Will require implementation to a demo page which i have not included.
Supports:
    mvn clean install
and
    mvn clean install -PautoInstallPackage

Or to deploy it to a publish instance

    mvn clean install -PautoInstallPackagePublish

And Author

    mvn clean install -PautoInstallBundle

## Testing
Currnet test covers the response with a string other then "" from teh data layer and does not check structure. this could be added to DataLayerTest class if needed.

## Misc
This is a sample and introduces very minimal information cover to showcase a datalayer capability with Sling Models. this should be extended to each project and represent important information on the page/user/cart/etc you wish to be easily available in a datalayer.