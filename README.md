# validation

Download the project in your IDE.

Run mvn install to resolve the dependency of the POM.xml

Run the SpringBatchApplication.java class of the project.

After succesfully starting of spring application please run below command. http://localhost:8443/fileloader/invokeJob?sourceName=test

After runing the above URL you check the output please login the below in memory DB.
and run below three querry.


Login the H2 DB by below url

http://localhost:8443/h2-console/login.do?jsessionid=94ecc2af057c65dc73d995a93af1a207

Query 1st : 
This table gives you all validated records of both(xml and csv) the files

select * from records_mapper

Query 2nd :
This table gives you duplicate reference Id records
 select * from duplicateRefernece;
 
Query 3rd:

This table gives you incorrect endbalance
select * from wrong_sums_records

