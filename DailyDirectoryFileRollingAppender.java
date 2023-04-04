package th.co.ncr.connector.utils;

import ch.qos.logback.core.rolling.RollingFileAppender;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DailyDirectoryFileRollingAppender<E> extends RollingFileAppender<E> {
	
	private SimpleDateFormat dateformatter = new SimpleDateFormat("yyyyMMdd");
	
	private String rootDirectory;
	private String directoryDatePattern;
	
	private String completeDir;
	private String orgFileName;
	
	public String getRootDirectory() {
		return rootDirectory;
	}
	public void setRootDirectory(String rootDirectory) {
		this.rootDirectory = rootDirectory;
	}
	public String getDirectoryDatePattern() {
		return directoryDatePattern;
	}
	public void setDirectoryDatePattern(String directoryDatePattern) {
		this.directoryDatePattern = directoryDatePattern;
	}
	
	@Override
	public void start() {
		orgFileName = fileName;
		dateformatter = new SimpleDateFormat(directoryDatePattern);
		doCreateDirectoryIfNecessary();
		super.start();
	}
	
	@Override
	public void rollover() {
		doCreateDirectoryIfNecessary();
		super.rollover();
    }
    
	private void doCreateDirectoryIfNecessary() {
		completeDir = rootDirectory + File.separator + dateformatter.format(new Date());
		fileName = completeDir + File.separator + orgFileName;
		if(!new File(completeDir).exists()) {
			new File(completeDir).mkdir();
		}
	}
}