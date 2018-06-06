package com.adobe.people.ararat.datalayer.core.models;


import com.adobe.acs.commons.models.injectors.annotation.impl.AemObjectAnnotationProcessorFactory;
import com.adobe.acs.commons.models.injectors.impl.AemObjectInjector;
import io.wcm.testing.mock.aem.junit.AemContext;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataLayerTest{


    @Rule
    public final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    @Mock
    private SlingHttpServletRequest request;


    private static final String PAGE_PATH = "/content/website/en/homepage";

    private DataLayer mymodel;
    private AemObjectInjector aemObjectInjector;
    private AemObjectAnnotationProcessorFactory factory;

    @Before
    public final void setUp() throws Exception {
        context.addModelsForClasses(DataLayer.class);
        context.load().json("/page.json", PAGE_PATH);
        context.currentPage(PAGE_PATH);
        aemObjectInjector = new AemObjectInjector();
        context.registerService(AemObjectInjector.class, aemObjectInjector);
        request = context.request();
        mymodel = request.adaptTo(DataLayer.class);
    }

    @Test
    public void isEmptyStringTest() {
        Assert.assertNotNull(mymodel);
        Assert.assertNotSame("",mymodel.getDatalayer());
    }
    @Test
    public void isAnonymousUserTest() {
        Assert.assertNotNull(mymodel);
        Assert.assertNotSame("",mymodel.getDatalayer());
    }
}