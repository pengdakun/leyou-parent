package com.peng.leyou.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.peng.leyou.service.UploadService;

/**   
* 项目名称：leyou-upload   
* 类名称：UploadController   
* 类描述：   文件上传
* 创建人：彭坤   
* 创建时间：2018年12月10日 下午9:43:28      
* @version     
*/
@RestController
@RequestMapping("/upload")
public class UploadController {
	
	@Autowired
	private UploadService uploadService;
	
	/** 
	 * @param @param file
	 * @param @return 
	 * @return ResponseEntity<String>  
	 * @Description 上传图片
	 * @author 彭坤
	 * @date 2018年12月10日 下午9:45:42
	 */
	@PostMapping("/image")
	public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file){
		return ResponseEntity.ok(uploadService.uploadImage(file));
	}
	

}
