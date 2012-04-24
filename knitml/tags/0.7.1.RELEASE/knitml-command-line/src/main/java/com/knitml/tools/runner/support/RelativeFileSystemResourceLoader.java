package com.knitml.tools.runner.support;

import java.io.File;

import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;

public class RelativeFileSystemResourceLoader extends FileSystemResourceLoader {

	private File targetFileDirectory;
	
	public RelativeFileSystemResourceLoader(File targetFile) {
		if (targetFile.isFile()) {
			this.targetFileDirectory = targetFile.getParentFile();
		} else if (targetFile.isDirectory()) {
			this.targetFileDirectory = targetFile;
		} else {
			throw new IllegalArgumentException("Constructor argument must be a valid file name or directory");
		}
	}
	
	@Override
	protected Resource getResourceByPath(String path) {
		return super.getResourceByPath(new File(targetFileDirectory, path).getPath());
	}

}
