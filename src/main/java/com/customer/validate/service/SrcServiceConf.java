package com.customer.validate.service;

import com.customer.validate.model.SrcFileConfigurationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class SrcServiceConf {

    @Value(("${records.class}"))
    private String recordsClass;


    @Autowired
    private SrcFileConfigurationBean fileConfigBean;

    private static final Logger LOG= LoggerFactory.getLogger(SrcFileConfigurationBean.class);


    /**
     * Method responsible for retrieve list of file from source directory
     * @param sourceName
     * @return lstConfigurationBeans
     */
    public List<SrcFileConfigurationBean> readSourceFiles(String sourceName) {
        List<SrcFileConfigurationBean> lstConfigurationBeans = null;
        List<File> dirFileList = null;
        String extension = "";
        try {
            lstConfigurationBeans = new ArrayList<SrcFileConfigurationBean>();
            dirFileList = new ArrayList<File>();
            dirFileList = getFileList(dirFileList);
            for(File file : dirFileList){
                int split = file.getName().lastIndexOf('.');
                if(split > 0){
                    extension = file.getName().substring(split +1);
                }
                String[] sub = file.getName().split("/");
                fileConfigBean = new SrcFileConfigurationBean();
                fileConfigBean.setFileName(file.getName());
                fileConfigBean.setFile(file);
                fileConfigBean.setType(extension);
                fileConfigBean.setEntity(recordsClass);
                lstConfigurationBeans.add(fileConfigBean);
            }

        } catch (Exception e) {
            LOG.error("Error in readSourceFile Method");
        }
        return lstConfigurationBeans;

    }

    private List<SrcFileConfigurationBean> createOrderOflist(List<SrcFileConfigurationBean> lstConfigurationBeans) {
        for(int i = 0; i< lstConfigurationBeans.size() ;i++){
            String[] subString = lstConfigurationBeans.get(i).getFileName().split("/");
            if(!subString[0].equals("airports.csv"));{
                Collections.reverse(lstConfigurationBeans);
                break;
            }
        }
        return lstConfigurationBeans;
    }


    /**
     * Method access and retrive the filelist
     * @param dirFileList
     * @return dirFileList
     */
    private List<File> getFileList(List<File> dirFileList) {
        File directory = null;
        File[] fList = null;
        try{
            directory = new File("src/main/resources/input/");
            fList= directory.listFiles();
            for(File file: fList){
                if(file.isFile()){
                    dirFileList.add(file);
                }else{
                    getFileList(dirFileList);
                }
            }

        }catch(Exception e){
            LOG.error("Failed to fetch the fileleat", e);
        }
        return dirFileList;
    }
}

