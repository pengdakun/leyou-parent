package com.peng.leyou.exception;

import com.peng.leyou.enums.ExceptionEnum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeyouException extends RuntimeException {
	
	private ExceptionEnum exceptionEnum;
	

}
