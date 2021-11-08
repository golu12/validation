package com.customer.validate.controller;

import com.customer.validate.batchConfig.CustomJobParameter;
import com.customer.validate.constant.FileLoaderConstant;
import com.customer.validate.model.SrcFileConfigurationBean;
import com.customer.validate.service.SrcServiceConf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path ="/fileloader")
public class JobInvokeController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job flatFileJob;

    @Autowired
    private Job xmlJob;

    @Autowired
    private SrcServiceConf srcServiceConf;

    private static final Logger LOG = LoggerFactory.getLogger(JobInvokeController.class);


    @RequestMapping(value = "/invokeJob")
    public String handle(@RequestParam String sourceName) throws Exception{

        JobExecution jobExecution = null;
        JobParameters jobParameter = null;
        List<SrcFileConfigurationBean> lstSrcFileConfigurationBeans = null;
        String jobStatus = null;

        try {
            lstSrcFileConfigurationBeans = srcServiceConf.readSourceFiles(sourceName);
            jobStatus = FileLoaderConstant.JOB_STATUS_COMPLETED;
            for(SrcFileConfigurationBean file : lstSrcFileConfigurationBeans){
                jobParameter = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
                        .addParameter(FileLoaderConstant.FILENAME, new CustomJobParameter<SrcFileConfigurationBean>(file)).toJobParameters();

                if(file.getType().equals("xml")){
                    jobExecution= jobLauncher.run(xmlJob, jobParameter);
                    LOG.info("Xml File Processed : " + jobExecution.getStatus().toString());
                } else {
                    jobExecution= jobLauncher.run(flatFileJob, jobParameter);
                    LOG.info("Csv File Processed : " + jobExecution.getStatus().toString());
                }

            }
        }catch (Exception e){
            LOG.error("Error Occured in handle execution method", e.getMessage());
        }
        return jobStatus;
    }
}

