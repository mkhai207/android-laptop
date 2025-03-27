package com.example.android_doan.data.model.request;

public class AddToCartRequest {
    private String quantity;
    private Product product;

    public AddToCartRequest(String quantity, String id) {
        this.quantity = quantity;
        this.product = new Product(id);
    }

    public static class Product{
        private String id;
        public Product(String id){
            this.id = id;
        }
    }

}
