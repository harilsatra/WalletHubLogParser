README

Assumptions:

	1. There is no database or table present before running the tool. So I check if the database and table are already present then I do not do anything else I create them.

	2. The tool can be run multiple times on the same file and so I do not persist the data stored in the log table and truncate it everytime the tool displays and stores the blocked IPs.

	3. The table consisting of blocked IPs is important and so I do not truncate it.

Requirements:

	1. MySQL
	2. Java

Steps Before Executing the Tool:

	1. Have the MySQL server up and running.

	2. Store the credentials of the MySQL server in a file called "Credentials.txt" in the following format (A sample file is provided):

		Username = ___________ 
		Password = ___________
		Address = ____________

		where Username and Password are the credentials of the MySQL server you set up and Address is localhost if being run on a local machine or specify the IP address.

	3. After storing the credentials lace this "Credentials.txt" in the same directory as Parser.jar.

	4. Run the tool from the command line.

		Example:  java -cp "parser.jar" com.ef.Parser --accesslog=/path/to/file --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 

		Where "accesslog" "startDate", "duration" and "threshold" are command line arguments."accesslog" is the path to the input log file, "startDate" is of "yyyy-MM-dd.HH:mm:ss" format, "duration" can take only "hourly", "daily" as inputs and "threshold" can be an integer.