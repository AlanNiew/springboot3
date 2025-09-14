package com.example.entity;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author: Niu
 * @Date: 2025/4/25 16:01
 * @Description:
 */
@Data
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MyMsgObject<T>  implements Serializable {
    @Serial
    private static final long serialVersionUID = 7535987143246731L;
    private T message;

    private String traceId;
}
