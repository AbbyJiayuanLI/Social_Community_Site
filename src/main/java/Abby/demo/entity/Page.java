package Abby.demo.entity;

import java.awt.List;

/*
 * 封装分页相关信息
 * 
 * 
 */
public class Page {
	
	//当前页码
	private int current = 1;
	//显示上限
	private int limit = 10;
	//数据总数（用于计算总页数，显示的页数范围）
	private int rows;
	//查询路径（复用分页链接？？）
	private String path;
	public int getCurrent() {
		return current;
	}
	public void setCurrent(int current) {
		if (current>=1) {
			this.current = current;
		}
	}
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		if (limit>=1 && limit<=100) {
			this.limit = limit;
		}
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		if (rows>=0) {
			this.rows = rows;
		}
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	public int getOffset() {
		// 通过当前页页码得到起始行，用于数据库查询
		// (current-1)*limit
		return (current-1)*limit;
	}
	
	public int getTotal() {
		// 总页数
		if (rows%limit==0) {
			return rows/limit;
		}else {
			return rows/limit+1;
		}
	}
	
	public int getFrom() {
		int from = current-2;
		return from<1 ? 1:from;
	}
	
	public int getTo() {
		int to = current+2;
		int total = getTotal();		
		return to>total ? total:to;
		
	}
	
	
	
	
	

}
