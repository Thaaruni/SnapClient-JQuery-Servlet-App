package SnapClient.backend.entity;

public record Customer(Integer id, String name,
                       String address, String profilePicture) {}