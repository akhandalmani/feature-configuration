//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.05.19 at 01:01:07 PM IST 
//


package com.getusroi.datacontext.jaxb;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.getusroi.datacontext.jaxb package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.getusroi.datacontext.jaxb
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link FeatureDataContext }
     * 
     */
    public FeatureDataContext createFeatureDataContext() {
        return new FeatureDataContext();
    }

    /**
     * Create an instance of {@link DataContexts }
     * 
     */
    public DataContexts createDataContexts() {
        return new DataContexts();
    }

    /**
     * Create an instance of {@link RefDataContexts }
     * 
     */
    public RefDataContexts createRefDataContexts() {
        return new RefDataContexts();
    }

    /**
     * Create an instance of {@link RefDataContext }
     * 
     */
    public RefDataContext createRefDataContext() {
        return new RefDataContext();
    }

    /**
     * Create an instance of {@link DataContext }
     * 
     */
    public DataContext createDataContext() {
        return new DataContext();
    }

}
