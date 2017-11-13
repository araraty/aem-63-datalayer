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

## Page name and AnalyticsPageNameProvider implementation
This project also contains an AnalyticsPageNameProvider implementation to remove the /content/ from the path and add a site name through a constant, this is again a sample and prefrably should be extracted to a config.
Result of the datalayer and CustomPageNameProvider are expected to be "sitename:path:to:page"


## Testing
Currnet test covers the response with a string other then "" from teh data layer and does not check structure. this could be added to DataLayerTest class if needed.


## Misc
This is a sample project to introduce a very minimaly covering datalyer with light ammount of information to showcase a datalayer capability with Sling Models.
If you are heading to use this in your project you should be extending the model to represent important information on the page/user/cart/etc you wish to be easily available in a datalayer.