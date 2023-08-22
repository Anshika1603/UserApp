package com.nashtech.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("az-user-collection")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    Integer userid;

    String firstName;

    String lastName;
}
