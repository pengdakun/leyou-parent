package com.peng.leyou.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.peng.leyou.service.PageService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PageTest {
	
	@Autowired
	private PageService pageService;
	
	@Test
	public void test() {
		pageService.createHtml(141L);
	}

}
