package com.customer.validate.reader;

import com.customer.validate.constant.FileLoaderConstant;
import com.customer.validate.model.SrcFileConfigurationBean;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.input.BOMInputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;

public class XmlReader implements ItemReader<Object>, StepExecutionListener {
    private static final Logger LOG = LoggerFactory.getLogger(XmlReader.class);


    private static int BUFFER_SIZE = 1024 *1024 *10;
    private NodeList nodeList = null;
    private static  int count =0;
    @Autowired
    private SrcFileConfigurationBean srcFileConfigurationBean ;

    @Override
    public void beforeStep(StepExecution stepExecution) {
        JobParameters jobParameter = null;
        try{
            jobParameter = stepExecution.getJobExecution().getJobParameters();
            srcFileConfigurationBean = (SrcFileConfigurationBean) jobParameter.getParameters().get(FileLoaderConstant.FILENAME).getValue();
            readFileHeader(srcFileConfigurationBean.getFile());
        }catch(Exception e){
            LOG.error("Error Occured in before step method ", e);

        }
    }

    /**
     * Methed to read header from the file.
     * @param file
     * @return headers
     */
    private void readFileHeader(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(srcFileConfigurationBean.getFile());
        nodeList = doc.getElementsByTagName("record");
    }


    @Override
    public Object read() {
        Object pojoObj = null;
        BeanInfo beanInfo = null;
        String setter = null;
        Class <?> parameterType = null;
        Method setterMethod = null;
        Class<?> modelClassName = getClassName();
        try {
            if(nodeList != null){
                pojoObj= modelClassName.newInstance();
                String[] header = {"reference","accountNumber","description","startBalance","mutation","endBalance"};
                String[] data = getData(count);
                count++;
                for(int j = 0; j < header.length ; j++){
                    beanInfo = Introspector.getBeanInfo(modelClassName);
                    for(PropertyDescriptor propertyDesc: beanInfo.getPropertyDescriptors()){
                        if(propertyDesc.getName().toString().equalsIgnoreCase(header[j])){
                            setter = propertyDesc.getWriteMethod().getName();
                            parameterType = propertyDesc.getPropertyType();
                            setterMethod =  modelClassName.getDeclaredMethod(setter,parameterType);
                            setterMethod.invoke(pojoObj, data[j]);
                            break;
                        }
                    }
                }
            if(count == nodeList.getLength()){
                return null;
            }
            }

        }catch(Exception e){
            LOG.error("Exception Occurred in read method of flatFileReader",e);
        }
        return pojoObj;
    }

    private String[] getData(int count) {
        for(int value = count ; value < nodeList.getLength(); value++){
            Node node = nodeList.item(value);
            if(node.getNodeType() == Node.ELEMENT_NODE){
                Element element = (Element) node;
                String[] data = {element.getAttribute("reference"),
                        element.getElementsByTagName("accountNumber").item(0).getTextContent(),
                        element.getElementsByTagName("description").item(0).getTextContent(),
                        element.getElementsByTagName("startBalance").item(0).getTextContent(),
                        element.getElementsByTagName("mutation").item(0).getTextContent(),
                        element.getElementsByTagName("endBalance").item(0).getTextContent()};
                return data;
            }
        }

        return null;
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return ExitStatus.COMPLETED;
    }

    /**
     * Method to retrieve model class.
     * @return model class
     */
    public Class<?> getClassName(){
        Class<?> className = null;
        try{
            className = Class.forName(srcFileConfigurationBean.getEntity());
        }catch(ClassNotFoundException e ){
            LOG.error("Exception Occurred in getClass method" ,e);
        }
        return className;
    }
}
