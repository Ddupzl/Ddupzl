package com.example.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PageUtils<E> {
    int totalCount;
    int size;
    List<E> list;
}
