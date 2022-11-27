package com.example.finalprj.web.user.controller.vo;

import lombok.* ;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaqForm {
    @Nullable
    Long id;

    String subject;
    String content;
}
