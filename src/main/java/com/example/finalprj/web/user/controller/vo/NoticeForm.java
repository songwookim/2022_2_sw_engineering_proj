package com.example.finalprj.web.user.controller.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NoticeForm {
    @Nullable
    Long id;

    String subject;
    String content;
}
