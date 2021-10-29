package com.itechart.library.model.entity;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class ReaderEntity extends Entity {

    String email;
    String name;
}
