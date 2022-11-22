package com.noslen.videoservice.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(unique = true)
    private String name;

    @Lob
    private byte[] data;

    public Video(String name, byte[] data) {
        this.name = name;
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }
}
