package com.customerService.app.dto;

public enum Type {

        MOBILE("همراه"),
        HOME("ثابت"),
        FAX("فکس"),
        OTHER("سایر");
        private String value;

        Type(String value) {
                this.value = value;
        }

        public String getValue() {
                return value;
        }

        public void setValue(String value) {
                this.value = value;
        }
}
