package com.customerService.app.dto;

import java.util.List;


public class MenuItmDto {

    private MenuItemType type;

    private String title;

    private UIPageDto page;

    private List<MenuItmDto> children;


    public MenuItmDto() {
    }

    public MenuItmDto(MenuItemType type, String title, UIPageDto page, List<MenuItmDto> children) {
        this.type = type;
        this.title = title;
        this.page = page;
        this.children = children;
    }

    public MenuItemType getType() {
        return type;
    }

    public void setType(MenuItemType type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public UIPageDto getPage() {
        return page;
    }

    public void setPage(UIPageDto page) {
        this.page = page;
    }

    public List<MenuItmDto> getChildren() {
        return children;
    }

    public void setChildren(List<MenuItmDto> children) {
        this.children = children;
    }
}
