package com.eningqu.params;

public class SmartBookParams {
	
	private Long id;
	
	 /**
     * 笔记本唯一记录id
     */
	private Integer bookInfoId;
	/**
     * 笔记本编码
     */
    private String noteBookId;
    /**
     * 页码
     */
    private Integer pageNumber;
    
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	/**
     * 笔记本类型(冗余)
     */
    private Integer type;
    
    public Integer getBookInfoId() {
		return bookInfoId;
	}
	public void setBookInfoId(Integer bookInfoId) {
		this.bookInfoId = bookInfoId;
	}
	public String getNoteBookId() {
		return noteBookId;
	}
	public void setNoteBookId(String noteBookId) {
		this.noteBookId = noteBookId;
	}
	public Integer getPageNumber() {
		return pageNumber;
	}
	public void setPageNumber(Integer pageNumber) {
		this.pageNumber = pageNumber;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
}
