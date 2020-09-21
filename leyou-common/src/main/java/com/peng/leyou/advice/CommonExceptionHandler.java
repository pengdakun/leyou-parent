package com.peng.leyou.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.peng.leyou.enums.ExceptionEnum;
import com.peng.leyou.exception.LeyouException;
import com.peng.leyou.vo.ExceptionResult;

/**   
* 项目名称：leyou-common   
* 类名称：CommonExceptionHandler   
* 类描述：   全局异常处理
* 创建人：彭坤   
* 创建时间：2018年12月6日 下午9:37:23      
* @version     
*/
@ControllerAdvice//controller的切面，拦截controller
public class CommonExceptionHandler {

	@ExceptionHandler(LeyouException.class)//拦截运行时异常
	public ResponseEntity<ExceptionResult> handlerException(LeyouException e){
		ExceptionEnum exceptionEnum = e.getExceptionEnum();
		return ResponseEntity.status(exceptionEnum.getCode()).body(new ExceptionResult(exceptionEnum));
	}
	
	
}
