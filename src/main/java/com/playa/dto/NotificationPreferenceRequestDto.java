package com.playa.dto;

import lombok.Data;

@Data
public class NotificationPreferenceRequestDto {
    private Boolean enabledComments=true;
    private Boolean enableSistem=true;
    private Boolean enableNewRelease=true;
    private Boolean enableFollowers=true;
}
