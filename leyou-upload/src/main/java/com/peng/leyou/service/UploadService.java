package com.peng.leyou.service;

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.peng.leyou.config.UploadProperties;
import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@EnableConfigurationProperties(value= {UploadProperties.class})
public class UploadService {
	
	@Autowired
	private UploadProperties uploadProperties;
	
	@Autowired
	private FastFileStorageClient storageClient;

	public String uploadImage(MultipartFile file) {
		
		try {
			//校验文件类型
			String contentType = file.getContentType();
			if (!uploadProperties.getAllowTypes().contains(contentType)) {
				throw new LeyouException(ExceptionEnum.INVALID_FILE_TYPE);
			}
			
			//校验文件内容
			BufferedImage read = ImageIO.read(file.getInputStream());
			if (read==null) {
				throw new LeyouException(ExceptionEnum.INVALID_FILE_TYPE);
			}
			String originalFilename = StringUtils.substringAfterLast(file.getOriginalFilename(),".");
			StorePath storePath = this.storageClient.uploadFile(file.getInputStream(), file.getSize(),originalFilename, null);
			return uploadProperties.getBaseUrl()+storePath.getFullPath();
		} catch (Exception e) {
			log.error("上传文件失败",e);
			throw new LeyouException(ExceptionEnum.FILE_UPLOAD_ERROR);
		}
		
		
	}

}
