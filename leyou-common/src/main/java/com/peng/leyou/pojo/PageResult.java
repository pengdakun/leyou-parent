package com.peng.leyou.pojo;

import java.util.List;

/**   
* 项目名称：leyou-common   
* 类名称：PageResult   
* 类描述：   分页返回对象
* 创建人：彭坤   
* 创建时间：2018年12月9日 下午9:12:49      
* @version     
*/
public class PageResult<T> {
  private Long total;// 总条数
  private Integer totalPage;// 总页数
  private List<T> items;// 当前页数据

  public PageResult() {
  }

  public PageResult(Long total, List<T> items) {
      this.total = total;
      this.items = items;
  }

  public PageResult(Long total, Integer totalPage, List<T> items) {
	super();
	this.total = total;
	this.totalPage = totalPage;
	this.items = items;
  }

public Long getTotal() {
      return total;
  }

  public void setTotal(Long total) {
      this.total = total;
  }

  public List<T> getItems() {
      return items;
  }

  public void setItems(List<T> items) {
      this.items = items;
  }

	public Integer getTotalPage() {
		return totalPage;
	}
	
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}
}