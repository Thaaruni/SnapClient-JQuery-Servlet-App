package SnapClient.backend.to;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTo<T> {
    private String id;
    private String name;
    private String address;
    private T profilePicture;
}