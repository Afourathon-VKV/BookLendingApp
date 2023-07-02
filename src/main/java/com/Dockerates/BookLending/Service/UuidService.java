package com.Dockerates.BookLending.Service;

import com.fasterxml.uuid.Generators;

public class UuidService {
    public static String getUUID() {
        return Generators.timeBasedEpochGenerator().generate().toString();
    }
}
